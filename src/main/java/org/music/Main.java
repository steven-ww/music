package org.music;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Main {

    ArrayList<FileInfo> fileInfoList = new ArrayList<>();
    Map<String, Map<String, List<String>>> artists = new HashMap<>();
    static Analyser analyser = new Analyser();

    public static void main(String[] args) throws Exception {
        String startPath;
        if (args.length < 1) {
            System.out.println("No directory specified, using current directory");
            startPath = ".";
        }
        else {
            startPath = args[0];
        }

        Main main = new Main();
        main.startProcessingPath(startPath);
        main.buildArtistHierarchy();
        main.writeToFile();
        System.out.println("Done");
    }

    public void startProcessingPath(String startPath) throws IOException {
        Path dir = Paths.get(startPath);
        try (Stream<Path> pathStream = Files.walk(dir)) {
            pathStream.forEach(path -> {
                try {
                    FileInfo fileInfo = parseFile(path.toFile());
                    if (fileInfo != null) {
                        fileInfoList.add(fileInfo);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private FileInfo parseFile(File file) throws IOException {
        if (!file.isDirectory()) {
            if (file.getName().endsWith(".flac")) {
                TrackInfo trackInfo = analyser.analyse(file.getAbsolutePath());
                return new FileInfo(file.getAbsolutePath(), trackInfo);
            }
        }
        return null;
    }

    private void buildArtistHierarchy() {
        for (FileInfo fileInfo : fileInfoList) {
            if (fileInfo.trackInfo() == null) {
                System.out.println("File " + fileInfo.absolutePath() + " has no meta data");
                continue;
            }

            Map<String, List<String>> albums = artists.get(fileInfo.trackInfo().artist());

            if (albums == null) {
                albums = new HashMap<>();
                artists.put(fileInfo.trackInfo().artist(), albums);
            }

            List<String> titles = albums.get(fileInfo.trackInfo().album());
            if (titles == null) {
                titles = new ArrayList<>();
                albums.put(fileInfo.trackInfo().album(), titles);
            }
            titles.add(fileInfo.trackInfo().title());
        }
    }

    private void writeToFile() {
        try (FileWriter myWriter = new FileWriter("musicLibrary.csv")){
            for (Map.Entry<String, Map<String, List<String>>> artist : artists.entrySet()) {
                myWriter.write(artist.getKey() + ", ");
                for (Map.Entry<String, List<String>> album : artist.getValue().entrySet()) {
                    myWriter.write(album.getKey() + ", ");
                    for (String title : album.getValue()) {
                        myWriter.write(title + System.lineSeparator());
                    }
                }
            }
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}