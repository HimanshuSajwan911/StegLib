/**
 * Author: Himanshu Sajwan
 * Github: https://github.com/HimanshuSajwan911
 */

package io.github.himanshusajwan911.steglib.lsb.core;

import io.github.himanshusajwan911.util.BitUtils.Endian;

/**
 * This class encapsulates various options used in the steganography process. These options
 * determine how data is encoded and decoded in the cover file.
 */
public class StegOptions {

    /**
     * Specifies the initial offset in the cover file where encoding should begin.
     */
    private int initialOffset;

    /**
     * Specifies the number of bytes to skip in the cover file before inserting each data block.
     */
    private int byteSkipPerBlock;

    /**
     * Specifies how many consecutive data bytes will be grouped for insertion into the cover file.
     */
    private int dataBlockSize;

    /**
     * Specifies which bit position of the cover byte to use for storing the data bit.
     * <p>
     * Allowed values: 0 to 7.
     * </p>
     */
    private int hiddenBitPosition;

    /**
     * Specifies the starting endian value for storing source data.
     * <p>
     * Use {@link io.github.himanshusajwan911.util.BitUtils.Endian#BIG_ENDIAN} for big-endian,<br>
     * and {@link io.github.himanshusajwan911.util.BitUtils.Endian#LITTLE_ENDIAN} for little-endian.
     * </p>
     */
    private Endian startingEndian;

    /**
     * Specifies the number of source blocks to insert before changing endianness.
     */
    private int endianChangeFrequency;

    /**
     * Embeds a password in the resulting data for identification purposes.
     */
    private String password;

    public StegOptions(){
        this.initialOffset = 0;
        this.byteSkipPerBlock = 0;
        this.dataBlockSize = 1;
        this.hiddenBitPosition = 0;
        this.startingEndian = Endian.BIG_ENDIAN;
        this.endianChangeFrequency = 0;
        this.password = "";
    }
    
    public StegOptions(int initialOffset, int byteSkipPerGroup, int sourceByteGroupSize, int hiddenBitPosition, Endian startingEndian, int endianChangeFrequency, String password) {
        this.initialOffset = initialOffset;
        this.byteSkipPerBlock = byteSkipPerGroup;
        this.dataBlockSize = sourceByteGroupSize;
        this.hiddenBitPosition = hiddenBitPosition;
        this.startingEndian = startingEndian;
        this.endianChangeFrequency = endianChangeFrequency;
        this.password = password;
    }
    
    public StegOptions(StegOptions options){
        this.initialOffset = options.initialOffset;
        this.byteSkipPerBlock = options.byteSkipPerBlock;
        this.dataBlockSize = options.dataBlockSize;
        this.hiddenBitPosition = options.hiddenBitPosition;
        this.startingEndian = options.startingEndian;
        this.endianChangeFrequency = options.endianChangeFrequency;
        this.password = options.password;
    }

    public int getInitialOffset() {
        return initialOffset;
    }

    public void setInitialOffset(int initialOffset) {
        this.initialOffset = initialOffset;
    }

    public int getByteSkipPerBlock() {
        return byteSkipPerBlock;
    }

    public void setByteSkipPerBlock(int byteSkipPerBlock) {
        this.byteSkipPerBlock = byteSkipPerBlock;
    }

    public int getDataBlockSize() {
        return dataBlockSize;
    }

    public void setDataBlockSize(int dataBlockSize) {
        if (dataBlockSize < 1) {
            throw new IllegalArgumentException("sourceByteGroupSize cannot be less than 1.");
        }
        this.dataBlockSize = dataBlockSize;
    }

    public int getHiddenBitPosition() {
        return hiddenBitPosition;
    }

    public void setHiddenBitPosition(int hiddenBitPosition) {
        if (hiddenBitPosition < 0 || hiddenBitPosition > 7) {
            throw new IllegalArgumentException("hiddenBitPosition should be between [0-7]");
        }
        this.hiddenBitPosition = hiddenBitPosition;
    }

    public Endian getStartingEndian() {
        return startingEndian;
    }

    public void setStartingEndian(Endian startingEndian) {
        this.startingEndian = startingEndian;
    }

    public int getEndianChangeFrequency() {
        return endianChangeFrequency;
    }

    public void setEndianChangeFrequency(int endianChangeFrequency) {
        this.endianChangeFrequency = endianChangeFrequency;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
