/**
 * Author: Himanshu Sajwan
 * GitHub: https://github.com/HimanshuSajwan911
 */

package io.github.himanshusajwan911.steglib.lsb.core;

import io.github.himanshusajwan911.util.BitUtils;
import io.github.himanshusajwan911.util.BitUtils.Endian;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

public class Steganography {

    protected static final int BUFFER_SIZE = 8192; // 8 KB
    
    public static final int ENCODING_SUCCESSFUL = 0;
    public static final int ENCODING_FAILED = 1;
    public static final int DECODING_SUCCESSFUL = 2;
    public static final int INVALID_PASSWORD = 3;

    

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
