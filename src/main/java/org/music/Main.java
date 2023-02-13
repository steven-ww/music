package org.music;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {


    public static void main(String... args) throws Exception {
        String startPath;
        if (args.length < 1) {
            System.out.println("Not directory specificed, using current directory");
            startPath = ".";
        }
        else {
            startPath = args[0];
        }
        Path dir = Paths.get(startPath);
        Files.walk(dir).forEach(path -> showFile(path.toFile()));
    }

    public static void showFile(File file) {
        if (file.isDirectory()) {
            System.out.println("Directory: " + file.getAbsolutePath());
        } else {
            System.out.println("File: " + file.getAbsolutePath());
        }
    }
}