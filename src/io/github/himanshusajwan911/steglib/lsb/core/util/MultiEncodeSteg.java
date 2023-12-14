/**
 * Author: Himanshu Sajwan
 * Github: https://github.com/HimanshuSajwan911
 */

package io.github.himanshusajwan911.steglib.lsb.core.util;

import io.github.himanshusajwan911.steglib.lsb.core.StegOptions;

/**
 * Parameter class used for grouping cover file path, destination file path, data amount to encode,
 * and StegOptions instance into one entity for passing to the encode method. This class facilitates
 * encoding a data file into multiple cover files.
 *
 */
public class MultiEncodeSteg {

    private String coverFilePath;
    private String destinationPath;
    private long dataAmountToEncode;
    private StegOptions options;

    public MultiEncodeSteg() {
        this.options = new StegOptions();
    }

    public MultiEncodeSteg(String coverFilePath, String destinationPath, long dataAmountToEncode, StegOptions options) {
        this.coverFilePath = coverFilePath;
        this.destinationPath = destinationPath;
        this.dataAmountToEncode = dataAmountToEncode;
        this.options = options;
    }

    public MultiEncodeSteg(String coverFilePath, String destinationPath, long dataAmountToEncode) {
        this.coverFilePath = coverFilePath;
        this.destinationPath = destinationPath;
        this.dataAmountToEncode = dataAmountToEncode;
        this.options = new StegOptions();
    }

    public String getCoverFilePath() {
        return coverFilePath;
    }

    public void setCoverFilePath(String coverFilePath) {
        this.coverFilePath = coverFilePath;
    }

    public String getDestinationPath() {
        return destinationPath;
    }

    public void setDestinationPath(String destinationPath) {
        this.destinationPath = destinationPath;
    }

    public long getDataAmountToEncode() {
        return dataAmountToEncode;
    }

    public void setDataAmountToEncode(long dataAmountToEncode) {
        this.dataAmountToEncode = dataAmountToEncode;
    }

    public StegOptions getOptions() {
        return options;
    }

    public void setOptions(StegOptions options) {
        this.options = options;
    }
}
