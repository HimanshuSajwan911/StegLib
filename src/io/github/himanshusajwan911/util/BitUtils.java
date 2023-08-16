/**
 * Author: Himanshu Sajwan
 * Github: https://github.com/HimanshuSajwan911
 */

package io.github.himanshusajwan911.util;

/**
 * The {@code BitUtils} class provides utility methods for performing bit-level
 * manipulations on various data types. It offers methods to insert, extract,
 * and modify individual bits within bytes and other data types.
 *
 */
public class BitUtils {

    /**
     * Inserts a specified bit value at the given position within a byte.
     *
     * @param target The original byte where the bit is to be inserted.
     * @param bitValue The bit value (0 or 1) to be inserted.
     * @param position The position (0 to 7) where the bit should be inserted.
     *
     * @return The modified byte with the specified bit value inserted at the
     * given position.
     *
     * @throws IllegalArgumentException If the position is out of the valid
     * range (0 to 7) or if the bitValue is not 0 or 1.
     */
    public static byte insertBitAt(byte target, int bitValue, int position) {

        if (position < 0 || position > 7 ) {
            throw new IllegalArgumentException("Invalid position");
        }
        
        if((bitValue != 0 && bitValue != 1)){
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

}
