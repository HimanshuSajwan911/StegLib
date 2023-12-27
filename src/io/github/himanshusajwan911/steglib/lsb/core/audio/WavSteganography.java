/**
 * Author: Himanshu Sajwan
 * Github: https://github.com/HimanshuSajwan911
 */
package io.github.himanshusajwan911.steglib.lsb.core.audio;

import io.github.himanshusajwan911.steglib.lsb.core.StegOptions;
import io.github.himanshusajwan911.steglib.lsb.core.Steganography;
import static io.github.himanshusajwan911.steglib.lsb.core.Steganography.DECODING_SUCCESSFUL;
import static io.github.himanshusajwan911.steglib.lsb.core.Steganography.ENCODING_SUCCESSFUL;
import static io.github.himanshusajwan911.steglib.lsb.core.Steganography.INVALID_PASSWORD;
import io.github.himanshusajwan911.steglib.lsb.core.util.MultiDecodeSteg;
import io.github.himanshusajwan911.steglib.lsb.core.util.MultiEncodeSteg;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class WavSteganography extends Steganography {

    @Override
    public int encode(String coverFilePath, String dataFilePath, String destinationPath, StegOptions options) throws FileNotFoundException, IOException {

        try (
                BufferedInputStream dataBufferedInputStream = new BufferedInputStream(new FileInputStream(dataFilePath))) {

            File dataFile = new File(dataFilePath);
            long size = dataFile.length();

            int waveHeaderSize = 44;

            StegOptions newStegOptions = new StegOptions(options);
            newStegOptions.setInitialOffset(newStegOptions.getInitialOffset() + waveHeaderSize);

            return encode(coverFilePath, dataBufferedInputStream, size, destinationPath, newStegOptions);
        }
    }

    @Override
    public int encode(ArrayList<MultiEncodeSteg> multiEncodeList, String dataFilePath) throws FileNotFoundException, IOException {

        File dataFile = new File(dataFilePath);
        long dataSize = dataFile.length();

        long totalDataAmountToEncode = 0;
        for (MultiEncodeSteg mes : multiEncodeList) {
            totalDataAmountToEncode += mes.getDataAmountToEncode();
        }

        if (totalDataAmountToEncode > dataSize) {
            throw new IllegalArgumentException("dataAmountToEncode cannot exceed data file size.");
        }

        try (
                BufferedInputStream dataBufferedInputStream = new BufferedInputStream(new FileInputStream(dataFile));) {

            for (MultiEncodeSteg mcs : multiEncodeList) {

                int waveHeaderSize = 44;
                StegOptions newStegOptions = new StegOptions(mcs.getOptions());
                newStegOptions.setInitialOffset(newStegOptions.getInitialOffset() + waveHeaderSize);

                encode(mcs.getCoverFilePath(), dataBufferedInputStream, mcs.getDataAmountToEncode(), mcs.getDestinationPath(), newStegOptions);
            }
        }

        return ENCODING_SUCCESSFUL;
    }

    @Override
    public int decode(String encodedFilePath, String destinationPath, StegOptions options) throws IOException {

        File encodedFile = new File(encodedFilePath);

        if (!encodedFile.exists()) {
            throw new FileNotFoundException("Cannot find the encoded file specified.");
        }

        try (
                BufferedOutputStream destinationBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(destinationPath))) {

            int waveHeaderSize = 44;
            StegOptions newStegOptions = new StegOptions(options);
            newStegOptions.setInitialOffset(newStegOptions.getInitialOffset() + waveHeaderSize);

            return decode(encodedFilePath, destinationBufferedOutputStream, newStegOptions);
        }
    }

    @Override
    public int decode(ArrayList<MultiDecodeSteg> multiDecodeList, String destinationPath) throws FileNotFoundException, IOException {

        try (
                BufferedOutputStream destinationBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(destinationPath));) {

            for (MultiDecodeSteg mds : multiDecodeList) {

                int waveHeaderSize = 44;
                StegOptions newStegOptions = new StegOptions(mds.getOptions());
                newStegOptions.setInitialOffset(newStegOptions.getInitialOffset() + waveHeaderSize);

                int decodingResult = decode(mds.getEncodedFilePath(), destinationBufferedOutputStream, newStegOptions);
                if (decodingResult == INVALID_PASSWORD) {
                    return INVALID_PASSWORD;
                }
            }
        }

        return DECODING_SUCCESSFUL;
    }

}
