package com.studyhub.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LocalFileService {

    // Read file content
    public String readFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    // Write content to a file
    public void writeFile(String content, String filePath) throws IOException {
        Files.write(Paths.get(filePath), content.getBytes());
    }

    // Append content to a file
    public void appendToFile(String content, String filePath) throws IOException {
        Files.write(Paths.get(filePath), content.getBytes(), StandardOpenOption.APPEND);
    }

    // List files in a directory
    public List<String> listFiles(String directoryPath) throws IOException {
        try (Stream<Path> stream = Files.list(Paths.get(directoryPath))) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        }
    }

    // Copy a file
    public void copyFile(String sourcePath, String destinationPath) throws IOException {
        Files.copy(Paths.get(sourcePath), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
    }

    // Move a file
    public void moveFile(String sourcePath, String destinationPath) throws IOException {
        Files.move(Paths.get(sourcePath), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
    }

    // Delete a file
    public void deleteFile(String filePath) throws IOException {
        Files.delete(Paths.get(filePath));
    }

    // Check if a file exists
    public boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    // Get file size
    public long getFileSize(String filePath) throws IOException {
        return Files.size(Paths.get(filePath));
    }
}
