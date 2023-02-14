package org.music;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import org.jflac.FrameListener;
import org.jflac.FLACDecoder;
import org.jflac.frame.Frame;
import org.jflac.metadata.Metadata;

public class Analyser implements FrameListener {

    private int frameNum = 0;

    public void analyse(String inFileName) throws IOException {
        System.out.println("FLAX Analysis for " + inFileName);
        try (FileInputStream is = new FileInputStream(inFileName)) {
            FLACDecoder decoder = new FLACDecoder(is);
            System.out.println(Arrays.toString(decoder.readMetadata()));
        }
    }

    /**
     * Process metadata records.
     * @param metadata the metadata block
     */
    public void processMetadata(Metadata metadata) {
        System.out.println(metadata.toString());
    }

    /**
     * Process data frames.
     * @param frame the data frame
     */
    public void processFrame(Frame frame) {
        frameNum++;
        System.out.println(frameNum + " " + frame.toString());
    }

    /**
     * Called for each frame error detected.
     * @param msg   The error message
     */
    public void processError(String msg) {
        System.out.println("Frame Error: " + msg);
    }

}
