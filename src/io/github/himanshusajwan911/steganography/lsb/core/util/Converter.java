/**
 * Author: Himanshu Sajwan
 * Github: https://github.com/HimanshuSajwan911
 */

package io.github.himanshusajwan911.steganography.lsb.core.util;

import io.github.himanshusajwan911.steganography.lsb.core.exceptions.InsufficientBytesException;

public class Converter {

    /**
     * Converts an integer <b>value</b> to its byte array representation.
     *
     * @param value the integer value to be converted to a byte array.
     * @return byte array representation of the integer value.
     */
    public static byte[] intToBytes(int value) {

        byte[] byte_array = new byte[4];

        byte_array[0] = (byte) ((value >>> 24) & 0xFF);
        byte_array[1] = (byte) ((value >>> 16) & 0xFF);
        byte_array[2] = (byte) ((value >>> 8) & 0xFF);
        byte_array[3] = (byte) ((value) & 0xFF);

        return byte_array;
    }

    /**
     * Converts a float <b>value</b> to its byte array representation.
     *
     * @param value the float value to be converted to a byte array.
     * @return byte array representation of the float value.
     */
    public static byte[] floatToBytes(float value) {

        return intToBytes(Float.floatToRawIntBits(value));
    }

    /**
     * Converts a long <b>value</b> to its byte array representation.
     *
     * @param value the long value to be converted to a byte array.
     * @return byte array representation of the long value.
     */
    public static byte[] longToBytes(long value) {

        byte[] byte_array = new byte[8];

        byte_array[0] = (byte) ((value >>> 56) & 0xFF);
        byte_array[1] = (byte) ((value >>> 48) & 0xFF);
        byte_array[2] = (byte) ((value >>> 40) & 0xFF);
        byte_array[3] = (byte) ((value >>> 32) & 0xFF);
        byte_array[4] = (byte) ((value >>> 24) & 0xFF);
        byte_array[5] = (byte) ((value >>> 16) & 0xFF);
        byte_array[6] = (byte) ((value >>> 8) & 0xFF);
        byte_array[7] = (byte) ((value) & 0xFF);

        return byte_array;
    }

    /**
     * Converts a double <b>value</b> to its byte array representation.
     *
     * @param value the double value to be converted to a byte array.
     * @return byte array representation of the double value.
     */
    public static byte[] doubleToBytes(double value) {

        return longToBytes(Double.doubleToRawLongBits(value));
    }

    /**
     * Converts a sequence of four bytes into an integer value (from index
     * <b>startIndex</b> to <b>startIndex + 3</b>).
     *
     * @param source array containing bytes that needs to be converted to int.
     * @param startIndex position from where bytes are taken.
     * <br><strong>Note:</strong> The array must contain at least 4 bytes.
     *
     * @return The integer value obtained by combining the specified bytes.
     *
     * @throws InsufficientBytesException If the byte array contains fewer than
     * 4 bytes.
     */
    public static int bytesToInt(byte[] source, int startIndex) {

        if ((source.length - startIndex) < 4) {
            throw new InsufficientBytesException("requires at least 4 bytes.");
        }

        return source[startIndex] << 24
                | (source[startIndex + 1] & 0xFF) << 16
                | (source[startIndex + 2] & 0xFF) << 8
                | (source[startIndex + 3] & 0xFF);
    }

    /**
     * Converts a sequence of four bytes from the specified byte array into an
     * integer value (from index <b>0</b> to <b>3</b>).
     *
     * @param source The byte array containing the bytes to be converted to an
     * integer.
     * <br><strong>Note:</strong> The array must contain at least 4 bytes.
     *
     * @return The integer value obtained by combining the specified bytes.
     * @throws InsufficientBytesException If the byte array contains fewer than
     * 4 bytes.
     */
    public static int bytesToInt(byte[] source) throws InsufficientBytesException {

        return bytesToInt(source, 0);
    }

    /**
     * Converts a sequence of four bytes into a floating-point value (from index
     * <b>startIndex</b> to <b>startIndex + 3</b>).
     *
     * @param source The byte array containing the bytes to be converted to a
     * float.
     * @param startIndex The position from which bytes are taken to perform the
     * conversion.
     * <br><strong>Note:</strong> The array must contain at least 4 bytes.
     *
     * @return The float value obtained by converting the specified bytes.
     *
     * @throws InsufficientBytesException If the byte array contains fewer than
     * 4 bytes.
     */
    public static float bytesToFloat(byte[] source, int startIndex) throws InsufficientBytesException {

        return Float.intBitsToFloat(bytesToInt(source, startIndex));
    }

    /**
     * Converts a sequence of four bytes from the specified byte array into a
     * floating-point value (from index <b>0</b> to <b>3</b>).
     *
     * @param source The byte array containing the bytes to be converted to
     * float.
     * <br><strong>Note:</strong> The array must contain at least 4 bytes.
     *
     * @return The float value obtained by converting the specified bytes.
     * @throws InsufficientBytesException If the byte array contains fewer than
     * 4 bytes.
     */
    public static float bytesToFloat(byte[] source) throws InsufficientBytesException {

        return bytesToFloat(source, 0);
    }

    /**
     * Converts a sequence of eight bytes from the specified byte array into a
     * long integer value (from index <b>startIndex</b> to <b>startIndex +
     * 7</b>).
     *
     * @param source The byte array containing the bytes to be converted to a
     * long integer.
     * @param startIndex The position from which bytes are taken to perform the
     * conversion.
     * <br><strong>Note:</strong> The array must contain at least 8 bytes.
     *
     * @return The long integer value obtained by combining the specified bytes.
     *
     * @throws InsufficientBytesException If the byte array contains fewer than
     * 8 bytes.
     */
    public static long bytesToLong(byte[] source, int startIndex) throws InsufficientBytesException {

        if ((source.length - startIndex) < 8) {
            throw new InsufficientBytesException("requires at least 8 bytes.");
        }

        long byte0 = source[startIndex];
        long byte1 = source[startIndex + 1];
        long byte2 = source[startIndex + 2];
        long byte3 = source[startIndex + 3];
        long byte4 = source[startIndex + 4];
        long byte5 = source[startIndex + 5];
        long byte6 = source[startIndex + 6];
        long byte7 = source[startIndex + 7];

        return byte0 << 56 | (byte1 & 0xFF) << 48 | (byte2 & 0xFF) << 40 | (byte3 & 0xFF) << 32
                | (byte4 & 0xFF) << 24 | (byte5 & 0xFF) << 16 | (byte6 & 0xFF) << 8 | (byte7 & 0xFF);

    }

    /**
     * Converts a sequence of eight bytes from the specified byte array into a
     * long integer value (from index <b>0</b> to <b>7</b>).
     *
     * @param source The byte array containing the bytes to be converted to a
     * long integer.
     * <br><strong>Note:</strong> The array must contain at least 8 bytes.
     *
     * @return The long integer value obtained by combining the specified bytes.
     *
     * @throws InsufficientBytesException If the byte array contains fewer than
     * 8 bytes.
     */
    public static long bytesToLong(byte[] source) throws InsufficientBytesException {

        return bytesToLong(source, 0);
    }

    /**
     * Converts a sequence of eight bytes from the specified byte array into a
     * double-precision floating-point value (from index <b>startIndex</b> to
     * <b>startIndex + 7</b>).
     *
     * @param source The byte array containing the bytes to be converted to a
     * double.
     * @param startIndex The position from which bytes are taken to perform the
     * conversion.
     * <br><strong>Note:</strong> The array must contain at least 8 bytes.
     *
     * @return The double-precision floating-point value obtained by combining
     * the specified bytes.
     *
     * @throws InsufficientBytesException If the byte array contains fewer than
     * 8 bytes.
     */
    public static double bytesToDouble(byte[] source, int startIndex) throws InsufficientBytesException {

        return Double.longBitsToDouble(bytesToLong(source, startIndex));
    }

    /**
     * Converts a sequence of eight bytes from the specified byte array into a
     * double-precision floating-point value (from index <b>0</b> to <b>7</b>).
     *
     * @param source The byte array containing the bytes to be converted to a
     * double.
     * <br><strong>Note:</strong> The array must contain at least 8 bytes.
     *
     * @return The double-precision floating-point value obtained by combining
     * the specified bytes.
     *
     * @throws InsufficientBytesException If the byte array contains fewer than
     * 8 bytes.
     */
    public static double bytesToDouble(byte[] source) throws InsufficientBytesException {

        return bytesToDouble(source, 0);
    }

}
