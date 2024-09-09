package com.studyhub.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.studyhub.model.Hut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class HutService {

    @Autowired
    private Firestore firestore;

    // Create a new Hut within a Hub
    public String createHut(String hubId, String name, String type) throws ExecutionException, InterruptedException {
        if (hubId == null || hubId.isEmpty()) {
            throw new IllegalArgumentException("Hub ID must not be null or empty");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Hut name must not be null or empty");
        }
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Hut type must not be null or empty");
        }

        CollectionReference hubCollection = firestore.collection("hubs");
        DocumentReference hubDoc = hubCollection.document(hubId);
        CollectionReference hutsCollection = hubDoc.collection("huts");

        DocumentReference newHut = hutsCollection.document();
        Hut hut = new Hut(name, type);
        ApiFuture<WriteResult> result = newHut.set(hut);

        return newHut.getId();
    }

    // Get all Huts within a Hub
    public List<Hut> getHuts(String hubId) throws ExecutionException, InterruptedException {
        if (hubId == null || hubId.isEmpty()) {
            throw new IllegalArgumentException("Hub ID must not be null or empty");
        }

        CollectionReference hubCollection = firestore.collection("hubs");
        DocumentReference hubDoc = hubCollection.document(hubId);
        CollectionReference hutsCollection = hubDoc.collection("huts");

        ApiFuture<QuerySnapshot> future = hutsCollection.get();
        QuerySnapshot querySnapshot = future.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        return documents.stream()
                .map(doc -> doc.toObject(Hut.class))
                .collect(Collectors.toList());
    }

    // Get a specific Hut by its ID within a Hub
    public Hut getHutById(String hubId, String hutId) throws ExecutionException, InterruptedException {
        if (hubId == null || hubId.isEmpty()) {
            throw new IllegalArgumentException("Hub ID must not be null or empty");
        }
        if (hutId == null || hutId.isEmpty()) {
            throw new IllegalArgumentException("Hut ID must not be null or empty");
        }

        CollectionReference hubCollection = firestore.collection("hubs");
        DocumentReference hubDoc = hubCollection.document(hubId);
        DocumentReference hutDoc = hubDoc.collection("huts").document(hutId);

        ApiFuture<com.google.cloud.firestore.DocumentSnapshot> future = hutDoc.get();
        com.google.cloud.firestore.DocumentSnapshot documentSnapshot = future.get();
        return documentSnapshot.toObject(Hut.class);
    }

    // Delete a Hut by its ID within a Hub
    public void deleteHut(String hubId, String hutId) throws ExecutionException, InterruptedException {
        if (hubId == null || hubId.isEmpty()) {
            throw new IllegalArgumentException("Hub ID must not be null or empty");
        }
        if (hutId == null || hutId.isEmpty()) {
            throw new IllegalArgumentException("Hut ID must not be null or empty");
        }

        CollectionReference hubCollection = firestore.collection("hubs");
        DocumentReference hubDoc = hubCollection.document(hubId);
        DocumentReference hutDoc = hubDoc.collection("huts").document(hutId);

        ApiFuture<WriteResult> result = hutDoc.delete();
    }
}
