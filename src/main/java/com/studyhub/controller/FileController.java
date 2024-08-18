package com.studyhub.controller;

import com.studyhub.service.CloudStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
public class FileController {

    @Autowired
    private CloudStorageService cloudStorageService;

    @PostMapping("/files/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = cloudStorageService.uploadFile(file);
            return ResponseEntity.ok(fileUrl);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error uploading file: " + e.getMessage());
        }
    }

    @GetMapping("/files/download")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam("fileName") String fileName) {
        try {
            InputStream inputStream = cloudStorageService.downloadFile(fileName);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
            return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
