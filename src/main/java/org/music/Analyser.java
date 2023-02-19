package org.music;

import java.io.FileInputStream;
import java.io.IOException;
import org.jflac.FrameListener;
import org.jflac.FLACDecoder;
import org.jflac.frame.Frame;
import org.jflac.metadata.Metadata;
import org.jflac.metadata.VorbisComment;

/**
 * Reads the meta data from a FLAC file and returns it
 */
public class Analyser implements FrameListener {

    /**
     * Analyes the file in order to retreive the metadata for it
     * @param inFileName the file to extract the metadata from
     * @return A TrackInfo record with the metadata
     * @throws IOException if a problem occurred reading the file
     */
    public TrackInfo analyse(String inFileName) throws IOException {
        System.out.println("FLAX Analysis for " + inFileName);

        try (FileInputStream is = new FileInputStream(inFileName)) {
            FLACDecoder decoder = new FLACDecoder(is);
            Metadata[] metadata = decoder.readMetadata();
            for (Metadata metadataValue : metadata) {
                if (metadataValue instanceof VorbisComment vorbisComment) {
                    return extractTrackInfo(vorbisComment);
                }
            }
            return null;
        }
    }

    /**
     * Extract the track information from the vorbis comment as a TrackInfo record
     * @param vorbisComment the comment data
     * @return TrackInfo record
     */
    private TrackInfo extractTrackInfo(VorbisComment vorbisComment) {
        if (vorbisComment == null || vorbisComment.getNumComments() == 0) {
            return null;
        }
        String album = nullOrValue(vorbisComment.getCommentByName("ALBUM"));
        String title = nullOrValue(vorbisComment.getCommentByName("TITLE"));
        String artist = nullOrValue(vorbisComment.getCommentByName("ARTIST"));
        String trackNumber = nullOrValue(vorbisComment.getCommentByName("TRACKNUMBER"));
        String trackTotal = nullOrValue(vorbisComment.getCommentByName("TRACKTOTAL"));
        return new TrackInfo(album, artist, trackNumber, title, trackTotal);
    }

    private String nullOrValue(String[] value) {
        return value.length > 0 ? value[0] : null;
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
        System.out.println("Process Frame");
    }

    /**
     * Called for each frame error detected.
     * @param msg   The error message
     */
    public void processError(String msg) {
        System.out.println("Frame Error: " + msg);
    }

}
