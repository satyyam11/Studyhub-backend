package com.studyhub.controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.studyhub.service.FirestoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/firestore")
public class FireStoreController {

    private final FirestoreService firestoreService;

    @Autowired
    public FireStoreController(FirestoreService firestoreService) {
        this.firestoreService = firestoreService;
    }

    @GetMapping("/document/{collectionName}/{documentId}")
    public ResponseEntity<?> getDocument(@PathVariable String collectionName, @PathVariable String documentId) {
        try {
            ApiFuture<DocumentSnapshot> future = firestoreService.getDocument(collectionName, documentId);
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                return ResponseEntity.ok(document.getData());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Document with ID " + documentId + " not found in collection " + collectionName);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving document: " + e.getMessage());
        }
    }

    @PostMapping("/document/{collectionName}/{documentId}")
    public ResponseEntity<?> setDocument(@PathVariable String collectionName, @PathVariable String documentId, @RequestBody Map<String, Object> data) {
        try {
            ApiFuture<WriteResult> future = firestoreService.setDocument(collectionName, documentId, data);
            WriteResult result = future.get();

            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error setting document: " + e.getMessage());
        }
    }

    @PostMapping("/document/{collectionName}")
    public ResponseEntity<?> createDocument(@PathVariable String collectionName, @RequestBody Map<String, Object> data) {
        try {
            ApiFuture<DocumentReference> future = firestoreService.createDocument(collectionName, data);
            DocumentReference documentRef = future.get();

            return ResponseEntity.status(HttpStatus.CREATED).body(documentRef.getId());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating document: " + e.getMessage());
        }
    }

    @PutMapping("/document/{collectionName}/{documentId}")
    public ResponseEntity<?> updateDocument(@PathVariable String collectionName, @PathVariable String documentId, @RequestBody Map<String, Object> data) {
        try {
            ApiFuture<WriteResult> future = firestoreService.updateDocument(collectionName, documentId, data);
            WriteResult result = future.get();

            return ResponseEntity.ok(result);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating document: " + e.getMessage());
        }
    }

    @DeleteMapping("/document/{collectionName}/{documentId}")
    public ResponseEntity<?> deleteDocument(@PathVariable String collectionName, @PathVariable String documentId) {
        try {
            ApiFuture<WriteResult> future = firestoreService.deleteDocument(collectionName, documentId);
            WriteResult result = future.get();

            return ResponseEntity.ok(result);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting document: " + e.getMessage());
        }
    }

    @GetMapping("/customQuery/{collectionName}")
    public ResponseEntity<?> customQuery(
            @PathVariable String collectionName,
            @RequestParam String field,
            @RequestParam String value) {
        try {
            ApiFuture<QuerySnapshot> future = firestoreService.customQuery(collectionName, field, value);
            QuerySnapshot querySnapshot = future.get();

            List<Map<String, Object>> documents = querySnapshot.getDocuments().stream()
                    .map(DocumentSnapshot::getData)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(documents);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error executing custom query: " + e.getMessage());
        }
    }
}
