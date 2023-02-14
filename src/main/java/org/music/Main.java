package org.music;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {

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
        Path dir = Paths.get(startPath);
        try (Stream<Path> pathStream = Files.walk(dir)) {
            pathStream.forEach(path -> {
                try {
                    showFile(path.toFile());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public static void showFile(File file) throws IOException {
        if (!file.isDirectory()) {
            //System.out.println("File: " + file.getAbsolutePath());
            if (file.getName().endsWith(".flac")) {
                analyser.analyse(file.getAbsolutePath());
            }
        }
    }
}