/**
 * Author: Himanshu Sajwan
 * Github: https://github.com/HimanshuSajwan911
 */

package io.github.himanshusajwan911.steglib.lsb.core.util;

/**
 * Stores the result of validation for the encoding process. This class encapsulates information
 * such as the number of data blocks, total byte skip, password size, total bytes required, and
 * cover file size.
 */
public class EncodeValidationResult {

    private int numberOfDataBlock;
    private int totalByteSkip;
    private int passwordSize;
    private long totalBytesRequired;
    private long coverFileSize;

    public int getNumberOfDataBlock() {
        return numberOfDataBlock;
    }

    public void setNumberOfDataBlock(int numberOfDataBlock) {
        this.numberOfDataBlock = numberOfDataBlock;
    }

    public int getTotalByteSkip() {
        return totalByteSkip;
    }

    public void setTotalByteSkip(int totalByteSkip) {
        this.totalByteSkip = totalByteSkip;
    }

    public int getPasswordSize() {
        return passwordSize;
    }

    public void setPasswordSize(int passwordSize) {
        this.passwordSize = passwordSize;
    }

    public long getTotalBytesRequired() {
        return totalBytesRequired;
    }

    public void setTotalBytesRequired(long totalBytesRequired) {
        this.totalBytesRequired = totalBytesRequired;
    }

    public long getCoverFileSize() {
        return coverFileSize;
    }

    public void setCoverFileSize(long coverFileSize) {
        this.coverFileSize = coverFileSize;
    }

}
