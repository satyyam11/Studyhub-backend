package com.studyhub.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class HubService {

    @Autowired
    private Firestore firestore;

    // Create a new Hub with creator and aide roles
    public String createHub(String name, String creatorId, boolean isPublic) throws ExecutionException, InterruptedException {
        Query query = firestore.collection("hubs").whereEqualTo("name", name);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        if (!documents.isEmpty()) {
            return "Error: A Hub with this name already exists.";
        }

        Map<String, Object> hub = new HashMap<>();
        hub.put("name", name);
        hub.put("creatorId", creatorId);
        hub.put("aideId", null); // Aide will be assigned later
        hub.put("isPublic", isPublic);

        ApiFuture<DocumentReference> futureRef = firestore.collection("hubs").add(hub);
        DocumentReference docRef = futureRef.get();
        String documentId = docRef.getId();
        return "Hub created with ID: " + documentId;
    }

    // Assign an aide role to a user in the Hub
    public String assignAide(String hubId, String aideId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("hubs").document(hubId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (!document.exists()) {
            return "Error: No such Hub exists.";
        }

        ApiFuture<WriteResult> writeResult = docRef.update("aideId", aideId);
        writeResult.get();
        return "Aide assigned successfully.";
    }

    // Get Hub by ID
    public Map<String, Object> getHubById(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("hubs").document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        Map<String, Object> response = new HashMap<>();
        if (document.exists()) {
            response.put("id", document.getId());
            response.putAll(document.getData());
        } else {
            response.put("error", "No such Hub exists.");
        }
        return response;
    }

    // Update an existing Hub
    public String updateHub(String id, String name, boolean isPublic) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("hubs").document(id);

        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (!document.exists()) {
            return "Error: No such Hub exists.";
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", name);
        updates.put("isPublic", isPublic);

        ApiFuture<WriteResult> writeResult = docRef.update(updates);
        writeResult.get();
        return "Hub updated successfully.";
    }

    // Delete a Hub
    public String deleteHub(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("hubs").document(id);

        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (!document.exists()) {
            return "Error: No such Hub exists.";
        }

        ApiFuture<WriteResult> writeResult = docRef.delete();
        writeResult.get();
        return "Hub deleted successfully.";
    }
}
