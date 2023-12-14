/**
 * Author: Himanshu Sajwan
 * Github: https://github.com/HimanshuSajwan911
 */

package io.github.himanshusajwan911.steglib.lsb.core.util;

import io.github.himanshusajwan911.steglib.lsb.core.StegOptions;

/**
 * Parameter class used for grouping encoded file path, and StegOptions instance into one entity for
 * passing to the encode method. This class facilitates decoding a data file from multiple encoded
 * cover files.
 */
public class MultiDecodeSteg {

    private String encodedFilePath;
    private StegOptions options;

    public MultiDecodeSteg() {
        this.options = new StegOptions();
    }

    public MultiDecodeSteg(String encodedFilePath, StegOptions options) {
        this.encodedFilePath = encodedFilePath;
        this.options = options;
    }

    public MultiDecodeSteg(String encodedFilePath) {
        this.encodedFilePath = encodedFilePath;
        this.options = new StegOptions();
    }

    public String getEncodedFilePath() {
        return encodedFilePath;
    }

    public void setEncodedFilePath(String encodedFilePath) {
        this.encodedFilePath = encodedFilePath;
    }

    public StegOptions getOptions() {
        return options;
    }

    public void setOptions(StegOptions options) {
        this.options = options;
    }
}
