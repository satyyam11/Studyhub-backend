package com.studyhub.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FileService {

    private final Storage storage;
    private final String bucketName = "studyhub-db.appspot.com";

    public FileService() throws IOException {
        // Initialize Firebase Storage with a specific file path
        String jsonPath = "C:/Program Files/Java/studyhub/src/main/resources/serviceAccountKey.json";
        InputStream serviceAccount = new FileInputStream(jsonPath);
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        this.storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("application/pdf").build();
        Blob blob = storage.create(blobInfo, file.getBytes());
        return blob.getMediaLink();
    }

    public byte[] downloadFile(String fileName) throws IOException {
        Blob blob = storage.get(BlobId.of(bucketName, fileName));
        if (blob == null) {
            throw new IOException("File not found");
        }
        return blob.getContent();
    }
}
