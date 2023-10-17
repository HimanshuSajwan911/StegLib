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
