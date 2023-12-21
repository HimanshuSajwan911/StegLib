/**
 * Author: Himanshu Sajwan
 * GitHub: https://github.com/HimanshuSajwan911
 */

package io.github.himanshusajwan911.steglib.lsb.core;

import io.github.himanshusajwan911.steglib.lsb.core.util.EncodeValidationResult;
import io.github.himanshusajwan911.steglib.lsb.core.util.MultiEncodeSteg;
import io.github.himanshusajwan911.util.BitUtils;
import io.github.himanshusajwan911.util.BitUtils.Endian;
import io.github.himanshusajwan911.util.exceptions.InsufficientBytesException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Steganography {

    protected static final int BUFFER_SIZE = 8192; // 8 KB
    
    public static final int ENCODING_SUCCESSFUL = 0;
    public static final int DECODING_SUCCESSFUL = 1;
    public static final int INVALID_PASSWORD = 2;
    
    
    /**
     * Encodes a specified amount of data bytes from the given BufferedInputStream object into the
     * provided cover file using the provided {@link StegOptions}. The encoded data is then written
     * to the specified destination.
     *
     * @param coverFilePath The path of the cover file to hide data within.
     * @param dataBufferedInputStream The BufferedInputStream containing the data to be hidden
     * within the cover file.
     * @param dataAmountToEncode The amount of data (in bytes) to encode from the
     * dataBufferedInputStream. This determines the size of the hidden payload in the Steganographic
     * result.
     * @param destinationPath The path of the destination file where the encoded file will be
     * stored.
     * @param options StegOptions containing parameters for encoding.
     *
     * @return {@link ENCODING_SUCCESSFUL} for successful encoding.
     *
     * @throws FileNotFoundException If the specified cover file is not found.
     * @throws IOException If an I/O error occurs while reading or writing files.
     */
    protected int encode(String coverFilePath, BufferedInputStream dataBufferedInputStream, long dataAmountToEncode, String destinationPath, StegOptions options) throws FileNotFoundException, IOException {
        
        int dataAvailable = dataBufferedInputStream.available();
        // setting dataAmountToEncode to minimum of dataAmountToEncode and dataAvailable to encode.
        dataAmountToEncode = Math.min(dataAmountToEncode, dataAvailable);
        
        EncodeValidationResult validator = validateEncoding(coverFilePath, dataAmountToEncode, options);
        
        if (validator.getTotalBytesRequired() > validator.getCoverFileSize()) {
            throw new InsufficientBytesException("Not enough bytes in Cover File, Total Bytes Required: " + validator.getTotalBytesRequired());
        }

        try (
                BufferedInputStream coverBufferedInputStream = new BufferedInputStream(new FileInputStream(coverFilePath)); 
                BufferedOutputStream destinationBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(destinationPath))) {

            //skipping offset amount of bytes from encoing.
            skip(coverBufferedInputStream, destinationBufferedOutputStream, options.getInitialOffset());
            
            if (!options.getPassword().isEmpty()) {
                // setting password bit as 1(true).
                setPasswordBit(coverBufferedInputStream, destinationBufferedOutputStream, 1, 0);
                //adding password to output file.
                encodePassword(coverBufferedInputStream, destinationBufferedOutputStream, options);
            }
            else{
                // setting password bit as 0(false).
                setPasswordBit(coverBufferedInputStream, destinationBufferedOutputStream, 0, 0);
            }
            
            //adding data file size to output file.
            encodeLong(coverBufferedInputStream, destinationBufferedOutputStream, options.getHiddenBitPosition(), options.getStartingEndian(), dataAmountToEncode);
            
            byte[] dataBuffer = new byte[options.getDataBlockSize()];
            byte[] coverBuffer = new byte[options.getDataBlockSize() * 8 + options.getByteSkipPerBlock()];
            
            int coverBytesRead = 0, dataBytesRead;
            int dataBlockCount = 0;
            Endian endian = options.getStartingEndian();
            long dataEncoded = 0;
            int coverBytesToRead;

            while(dataEncoded < dataAmountToEncode ){
                
                if((dataEncoded + dataBuffer.length) > dataAmountToEncode){
                    int newDataBufferSize = (int) (dataAmountToEncode - dataEncoded);
                    dataBuffer = new byte[newDataBufferSize];
                }
                
                dataBytesRead = dataBufferedInputStream.read(dataBuffer);
                if(dataBytesRead == -1){
                    break;
                }
                
                coverBytesToRead = dataBytesRead * 8 + options.getByteSkipPerBlock();
                coverBytesRead = coverBufferedInputStream.read(coverBuffer, 0, coverBytesToRead);
                
                BitUtils.insertBitsAt(coverBuffer, 0, dataBuffer, 0, dataBytesRead - 1, options.getHiddenBitPosition(), endian);
                destinationBufferedOutputStream.write(coverBuffer, 0, coverBytesRead);
                
                dataEncoded += dataBytesRead;

                // changing endianess according to given EndianChangeFrequency.
                if (++dataBlockCount == options.getEndianChangeFrequency()) {
                    if (endian == Endian.BIG_ENDIAN) {
                        endian = Endian.LITTLE_ENDIAN;
                    } else {
                        endian = Endian.BIG_ENDIAN;
                    }
                    
                    dataBlockCount = 0;
                }
            }
            
            coverBuffer = new byte[BUFFER_SIZE];
            // copying remaining byte from cover file.
            while ((coverBytesRead = coverBufferedInputStream.read(coverBuffer)) != -1) {
                destinationBufferedOutputStream.write(coverBuffer, 0, coverBytesRead);
            }
        }   
        
        return ENCODING_SUCCESSFUL;
    }
    
    /**
     * Calculates the amount of bytes which can be used for encoding process ie actual data bytes
     * not metadata of cover file.
     *
     * @param coverFilePath The file path of the cover file.
     *
     * @return The size of the cover file in bytes.
     *
     * @throws FileNotFoundException If the specified cover file is not found.
     */
    protected long getCoverFileSize(String coverFilePath) throws FileNotFoundException{
        File coverFile = new File(coverFilePath);
        
        if (!coverFile.exists()) {
            throw new FileNotFoundException("Cannot find the cover file specified.");
        }
        
        return coverFile.length();
    }
    
    /**
     * Validates encoding parameters based on the specified cover file, cover file size, and
     * StegOptions.
     *
     * @param coverFilePath The file path of the cover file.
     * @param dataFilePath The file path of the data file.
     * @param options The encoding options.
     *
     * @return An {@code EncodeValidationResult} containing calculated parameters for the encoding
     * process.
     * @throws FileNotFoundException If the specified cover file is not found.
     */
    public EncodeValidationResult validateEncoding(String coverFilePath, String dataFilePath, StegOptions options) throws FileNotFoundException {

        File dataFile = new File(dataFilePath);

        if (!dataFile.exists()) {
            throw new FileNotFoundException("Cannot find the data file specified.");
        }

        return validateEncoding(coverFilePath, dataFile.length(), options);
    }

    /**
     * Validates encoding parameters based on the specified cover file, data amount to encode, and
     * StegOptions.
     *
     * @param coverFilePath The file path of the cover file.
     * @param dataAmountToEncode The amount of data in bytes to be encoded.
     * @param options The encoding options.
     * @return An {@code EncodeValidationResult} containing calculated parameters for the encoding
     * process.
     * @throws FileNotFoundException If the specified cover file is not found.
     */
    public EncodeValidationResult validateEncoding(String coverFilePath, long dataAmountToEncode, StegOptions options) throws FileNotFoundException {

        long coverSize = getCoverFileSize(coverFilePath);

        // intial size 1 to store password bit.
        int passwordSize = 1;
        if (options.getPassword().length() > 0) {
            // +32 because password length is also stored using 32 bit integer.
            passwordSize = (options.getPassword().length() * 8 + 32);
        }

        int numberOfDataBlock = (int) ((dataAmountToEncode - 1) / options.getDataBlockSize() + 1);

        // (numberOfDataBlock - 1) Because, after processing the final data block, no bytes will be skipped.
        int totalByteSkip = (numberOfDataBlock - 1) * options.getByteSkipPerBlock();

        // +64 bytes to store data file size.
        long totalBytesRequired = (dataAmountToEncode * 8) + totalByteSkip + options.getInitialOffset() + passwordSize + 64;

        EncodeValidationResult result = new EncodeValidationResult();
        result.setNumberOfDataBlock(numberOfDataBlock);
        result.setPasswordSize(passwordSize);
        result.setTotalByteSkip(totalByteSkip);
        result.setTotalBytesRequired(totalBytesRequired);
        result.setCoverFileSize(coverSize);

        return result;
    }

    /**
     * Validates encoding parameters for a list of MultiEncodeSteg instances.
     *
     * @param multiEncodeList The list of MultiEncodeSteg instances containing cover file paths,
     * data amounts to encode, and encoding options.
     * 
     * @return An array of EncodeValidationResult instances, each corresponding to a
     * MultiEncodeSteg.
     * 
     * @throws FileNotFoundException If any specified cover file is not found.
     */
    public EncodeValidationResult[] validateEncoding(ArrayList<MultiEncodeSteg> multiEncodeList) throws FileNotFoundException {

        EncodeValidationResult[] result = new EncodeValidationResult[multiEncodeList.size()];

        for (int i = 0; i < multiEncodeList.size(); ++i) {
            MultiEncodeSteg mes = multiEncodeList.get(i);
            result[i] = validateEncoding(mes.getCoverFilePath(), mes.getDataAmountToEncode(), mes.getOptions());
        }

        return result;
    }
    
    /**
     * Skips a specified amount of data from a source BufferedInputStream and writes it to a target
     * BufferedOutputStream.
     *
     * @param input The source BufferedInputStream to read data from.
     * @param output The target BufferedOutputStream to write data to.
     * @param amount The number of bytes to skip and transfer.
     *
     * @throws IOException If an I/O error occurs during reading or writing operations.
     */
    protected void skip(BufferedInputStream input, BufferedOutputStream output, int amount) throws IOException {

        byte[] buffer = new byte[amount];

        if (input != null) {
            input.read(buffer);
        }

        if (output != null) {
            output.write(buffer);
        }

    }
    
    /**
     * Reads a byte from the input stream (referred to as the password byte), sets the specified
     * {@code bitValue} at the given {@code bitPosition} in the password byte, and writes this byte
     * to the output stream.
     *
     * @param input The input stream from which to read the original password byte.
     * @param output The output stream to which the modified password byte is written.
     * @param bitValue The value (0 or 1) to set at the specified bit position.
     * <br>1 indicates a password is set, 0 indicates no password is set.
     * @param bitPosition The position at which the password bit is set, ranging from 0 to 7.
     * @throws IOException If an I/O error occurs while reading.
     */
    protected void setPasswordBit(BufferedInputStream input, BufferedOutputStream output, int bitValue, int bitPosition) throws IOException {
        
        byte passwordByte = (byte) input.read();
        passwordByte = BitUtils.insertBitAt(passwordByte, bitValue, bitPosition);
        
        output.write(passwordByte);
    }
    
    /**
     * Reads a byte from the input stream (referred to as the password byte) and extracts the bit
     * value at the specified {@code bitPosition}.
     *
     * @param input The input stream from which to read the password byte.
     * @param bitPosition The position at which to extract the bit value, ranging from 0 to 7.
     * 
     * @return The extracted bit value (0 or 1) at the specified bit position.
     * <br>1 indicates a password is set, 0 indicates no password is set.
     * @throws IOException If an I/O error occurs while reading.
     */
    protected int getPasswordBit(BufferedInputStream input, int bitPosition) throws IOException{
        byte passwordByte = (byte) input.read();
        
        return BitUtils.extractBitAt(passwordByte, bitPosition);
    }

   /**
     * Encodes a password into a sequence of bytes using the provided StegOptions and writes them to
     * a BufferedOutputStream.
     *
     * @param input The source BufferedInputStream to read bytes from for additional data.
     * @param output The BufferedOutputStream to write the encoded password bytes to.
     * @param options The StegOptions containing configuration parameters for encoding.
     *
     * @throws IOException If an I/O error occurs during reading, encoding, or writing operations.
     */
    protected void encodePassword(BufferedInputStream input, BufferedOutputStream output, StegOptions options) throws IOException {

        int passwordLength = options.getPassword().length();
        encodeInteger(input, output, options.getHiddenBitPosition(), options.getStartingEndian(), passwordLength);
        
        byte[] passwordBytes = options.getPassword().getBytes();
        byte[] buffer = new byte[passwordLength * 8];
        input.read(buffer);
        
        BitUtils.insertBitsAt(buffer, 0, passwordBytes, 0, passwordBytes.length - 1, options.getHiddenBitPosition(), options.getStartingEndian());
        
        output.write(buffer);
    }
    
    /**
     * Decodes a password from a sequence of bytes using the provided StegOptions and returns it as
     * a String.
     *
     * @param input The source BufferedInputStream to read the encoded password bytes from.
     * @param options The StegOptions containing configuration parameters for decoding.
     *
     * @return The decoded password as a String.
     * @throws IOException If an I/O error occurs during reading or decoding operations.
     */
    protected String decodePassword(BufferedInputStream input, StegOptions options) throws IOException {

        int extractedPasswordLength = decodeInteger(input, options.getHiddenBitPosition(), options.getStartingEndian());

        byte[] buffer = new byte[extractedPasswordLength * 8];
        input.read(buffer);

        byte[] extractedPasswordBytes = BitUtils.extractBitsAt(buffer, 0, extractedPasswordLength, options.getHiddenBitPosition(), options.getStartingEndian());

        return new String(extractedPasswordBytes);
    }

    /**
     * Encodes an integer value into a sequence of bytes read from a source BufferedInputStream,
     * and writes the modified bytes to a target BufferedOutputStream.
     *
     * @param input The source BufferedInputStream to read bytes from.
     * @param output The target BufferedOutputStream to write the encoded bytes to.
     * @param bitPosition The bit position within each byte for encoding.
     * @param endian The byte order (endianess) for encoding.
     * @param value The integer value to encode.
     *
     * @throws IOException If an I/O error occurs during reading or writing operations.
     */
    protected void encodeInteger(BufferedInputStream input, BufferedOutputStream output, int bitPosition, Endian endian, int value) throws IOException {

        byte[] buffer = new byte[Integer.SIZE];
        input.read(buffer);
        BitUtils.insertIntegerAt(buffer, 0, bitPosition, value, endian);

        output.write(buffer);
    }

    /**
     * Decodes an integer value from a sequence of bytes read from a source BufferedInputStream.
     *
     * @param input The source BufferedInputStream to read bytes from.
     * @param bitPosition The bit position within each byte for decoding.
     * @param endian The byte order (endianess) for decoding.
     *
     * @return The decoded integer value.
     *
     * @throws IOException If an I/O error occurs during reading operations.
     */
    protected int decodeInteger(BufferedInputStream input, int bitPosition, Endian endian) throws IOException {

        byte[] buffer = new byte[Integer.SIZE];
        input.read(buffer);

        return BitUtils.extractIntegerAt(buffer, 0, bitPosition, endian);
    }

    /**
     * Encodes a long value into a sequence of bytes and writes it to a BufferedOutputStream.
     *
     * @param input The source BufferedInputStream to read bytes from.
     * @param output The target BufferedOutputStream to write the encoded bytes to.
     * @param bitPosition The bit position within each byte for encoding.
     * @param endian The byte order (endianess) for encoding.
     * @param value The long value to be encoded and written.
     *
     * @throws IOException If an I/O error occurs during reading or writing operations.
     */
    protected void encodeLong(BufferedInputStream input, BufferedOutputStream output, int bitPosition, Endian endian, long value) throws IOException {

        byte[] buffer = new byte[Long.SIZE];
        input.read(buffer);
        BitUtils.insertLongAt(buffer, 0, bitPosition, value, endian);

        output.write(buffer);
    }

    /**
     * Decodes a long value from a sequence of bytes read from a BufferedInputStream.
     *
     * @param input The source BufferedInputStream to read bytes from.
     * @param bitPosition The bit position within each byte for decoding.
     * @param endian The byte order (endianess) for decoding.
     *
     * @return The decoded long value.
     *
     * @throws IOException If an I/O error occurs during reading or decoding operations.
     */
    protected long decodeLong(BufferedInputStream input, int bitPosition, Endian endian) throws IOException {

        byte[] buffer = new byte[Long.SIZE];
        input.read(buffer);

        return BitUtils.extractLongAt(buffer, 0, bitPosition, endian);
    }

}
