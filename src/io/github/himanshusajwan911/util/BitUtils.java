/**
 * Author: Himanshu Sajwan
 * Github: https://github.com/HimanshuSajwan911
 */

package io.github.himanshusajwan911.util;

import io.github.himanshusajwan911.util.exceptions.InsufficientBytesException;

/**
 * The {@code BitUtils} class provides utility methods for performing bit-level manipulations on
 * various data types. It offers methods to insert, extract, and modify individual bits within bytes
 * and other data types.
 *
 */
public class BitUtils {

    /**
     * Enumeration representing byte order (endianess) for multi-byte data storage and
     * interpretation.
     * <br>
     * Use {@link #BIG_ENDIAN} for most significant byte first, and {@link #LITTLE_ENDIAN} for least
     * significant byte first.
     */
    public enum Endian {
        /**
         * Represents big-endian byte order, where the most significant byte is stored first.
         */
        BIG_ENDIAN,
        /**
         * Represents little-endian byte order, where the least significant byte is stored first.
         */
        LITTLE_ENDIAN
    }

    /**
     * Inserts a specified bit value at the given position within a byte.
     *
     * @param target The original byte where the bit is to be inserted.
     * @param bitValue The bit value (0 or 1) to be inserted.
     * @param position The position (0 to 7) where the bit should be inserted.
     *
     * @return The modified byte with the specified bit value inserted at the given position.
     *
     * @throws IllegalArgumentException If the position is out of the valid range (0 to 7) or if the
     * bitValue is not 0 or 1.
     */
    public static byte insertBitAt(byte target, int bitValue, int position) {

        if (position < 0 || position > 7) {
            throw new IllegalArgumentException("Invalid position");
        }

        if ((bitValue != 0 && bitValue != 1)) {
            throw new IllegalArgumentException("Invalid source bit value");
        }

        // Create a mask to clear the bit at the specified position in the target byte.
        byte bitRemoverMask = (byte) ~(1 << position);
        target = (byte) (target & bitRemoverMask);

        // Create a mask to set the bit to the specified value at the specified position.
        byte bitSetterMask = (byte) (bitValue << position);
        target = (byte) (target | bitSetterMask);

        return target;
    }

    /**
     * Inserts all the bits from the bytes in the {@code sourceArray} (starting from
     * {@code sourceStartIndex} to {@code sourcEndIndex}) into the bytes of the {@code targetArray}
     * (starting from {@code targetStartIndex} to {@code targetEndIndex}) at the specified
     * {@code position}.
     * <p>
     * Note: The number of bytes in the {@code targetArray} should be at least 8 times that in the
     * {@code sourceArray} for the given start and end indices. (For example, if the
     * {@code sourceArray} contains 2 bytes, the {@code targetArray} should have at least 8 * 2 = 16
     * bytes).</p>
     *
     * @param targetArray The byte array where bits will be inserted.
     * @param targetStartIndex The starting index in the target array for insertion.
     * @param sourceArray The byte array from which bits will be taken for insertion.
     * @param sourceStartIndex The starting index in the {@code sourceArray} for taking bits.
     * @param sourceEndIndex The ending index in the {@code sourceArray} for taking
     * bits.<b>[inclusive]</b>
     * @param bitPosition The position within each byte of the target array where bits will be
     * inserted.
     * @param endian The endian format for bits of {@code sourceArray}.
     * <br>
     * Use {@link BitUtils.Endian#BIG_ENDIAN} for big-endian and
     * {@link BitUtils.Endian#LITTLE_ENDIAN} for little-endian.
     *
     * @throws IllegalArgumentException If any of the provided index values are out of bounds.
     * @throws InsufficientBytesException If the {@code targetArray} does not have enough capacity
     * for the required bits.
     */
    public static void insertBitsAt(byte[] targetArray, int targetStartIndex,
            byte[] sourceArray, int sourceStartIndex, int sourceEndIndex, int bitPosition, Endian endian) {

        if (targetStartIndex < 0 || targetStartIndex >= targetArray.length
                || sourceStartIndex < 0 || sourceStartIndex >= sourceArray.length
                || sourceEndIndex < 0 || sourceEndIndex >= sourceArray.length) {
            throw new IllegalArgumentException("Invalid index values: Ensure that target and source indices are within valid bounds.");
        }

        int targetCapacity = targetArray.length - targetStartIndex;
        int sourceSize = sourceEndIndex - sourceStartIndex + 1;

        if (targetCapacity < (sourceSize * 8)) {
            throw new InsufficientBytesException("The target array does not have enough capacity to accommodate the required number of bits.");
        }

        for (int i = sourceStartIndex; i <= sourceEndIndex; ++i) {

            byte sourceByte = sourceArray[i];
            for (int j = 7; j >= 0; --j, ++targetStartIndex) {
                if (endian == Endian.BIG_ENDIAN) {
                    targetArray[targetStartIndex] = insertBitAt(targetArray[targetStartIndex], ((sourceByte >>> j) & 1), bitPosition);
                } else if (endian == Endian.LITTLE_ENDIAN) {
                    targetArray[targetStartIndex] = insertBitAt(targetArray[targetStartIndex], ((sourceByte >>> (7 - j)) & 1), bitPosition);
                }
            }
        }
    }

    /**
     * Extracts {@code amount} number of bytes from {@code sourceArray} starting from
     * {@code startIndex}.
     * <br>
     * 1 bit is extracted from 1 byte at specified {@code bitPosition} and 8 such bits are combined
     * together according to specified {@code endian} to make 1 extracted byte.
     *
     * @param sourceArray The source byte array from which bits will be extracted.
     * @param startIndex The index in the source array to start extracting bits from.
     * @param amount The number of bytes to be extracted (each byte contains 8 bits).
     * @param bitPosition The starting bit position within each source byte for extraction.
     * @param endian The endianness (BIG_ENDIAN or LITTLE_ENDIAN) to determine bit order.
     *
     * @return A new byte array containing the extracted bytes.
     *
     * @throws InsufficientBytesException If the source array does not contain enough bytes to
     * fulfill the extraction.
     */
    public static byte[] extractBitsAt(byte[] sourceArray, int startIndex, int amount, int bitPosition, Endian endian) {

        if ((sourceArray.length - startIndex + 1) < amount * 8) {
            throw new InsufficientBytesException("source does not contain enough bytes.");
        }

        byte[] extractedBytesArray = new byte[amount];

        for (int i = 0; i < amount; ++i) {
            byte extractedByte = 0;
            for (int j = 0; j <= 7; ++j, ++startIndex) {
                if (endian == Endian.BIG_ENDIAN) {
                    extractedByte = (byte) ((extractedByte << 1) | (sourceArray[startIndex] >>> bitPosition) & 1);
                } else if (endian == Endian.LITTLE_ENDIAN) {
                    extractedByte = (byte) (extractedByte | ((sourceArray[startIndex] >>> bitPosition) & 1) << j);
                }
            }
            extractedBytesArray[i] = extractedByte;
        }

        return extractedBytesArray;
    }

}
