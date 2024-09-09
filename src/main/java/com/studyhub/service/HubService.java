package com.studyhub.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class HubService {

    @Autowired
    private Firestore firestore;

    @Autowired
    private RoleService roleService;

    // Create a new Hub with creator and aide roles
    public String createHub(String name, String creatorId, boolean isPublic) throws ExecutionException, InterruptedException {
        // Check if a Hub with the same name already exists
        Query query = firestore.collection("hubs").whereEqualTo("name", name);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        if (!documents.isEmpty()) {
            return "Error: A Hub with this name already exists.";
        }

        // Create new Hub
        Map<String, Object> hub = new HashMap<>();
        hub.put("name", name);
        hub.put("creatorId", creatorId);
        hub.put("aideId", null);  // Aide will be assigned later
        hub.put("isPublic", isPublic);

        // Add the Hub to the database
        ApiFuture<DocumentReference> futureRef = firestore.collection("hubs").add(hub);
        DocumentReference docRef = futureRef.get();
        String hubId = docRef.getId();

        // Assign creator role to the hub creator
        roleService.assignCreatorRole(hubId, creatorId);

        return "Hub created with ID: " + hubId;
    }

    // Assign an aide role to a user in the Hub
    public String assignAide(String hubId, String aideId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("hubs").document(hubId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (!document.exists()) {
            return "Error: No such Hub exists.";
        }

        // Update the Aide ID
        ApiFuture<WriteResult> writeResult = docRef.update("aideId", aideId);
        writeResult.get();
        return "Aide assigned successfully.";
    }

    // Get a specific Hub by ID
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

        // Prepare the updates
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

    // Get all Hubs that a user has joined
    public List<Map<String, Object>> getHubsForUser(String userId) throws ExecutionException, InterruptedException {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID must not be null or empty");
        }

        // Log the userId
        System.out.println("Fetching hubs for userId: " + userId);

        // Query the user_hubs collection where userId matches
        Query query = firestore.collection("hubs").whereEqualTo("creatorId", userId);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        // Log the number of documents found
        System.out.println("Number of documents found: " + documents.size());

        // Prepare list of hubs
        List<Map<String, Object>> userHubs = new ArrayList<>();

        // If user has not joined any hubs
        if (documents.isEmpty()) {
            return userHubs;
        }

        // Fetch each hub the user has joined by hubId
        for (QueryDocumentSnapshot document : documents) {
            String hubId = document.getId();

            // Log the hubId
            System.out.println("Fetching hub with hubId: " + hubId);

            // Check if hubId is not null or empty
            if ( hubId.isEmpty()) {
                continue; // Skip this document
            }

            // Retrieve the hub details
            DocumentReference hubRef = firestore.collection("hubs").document(hubId);
            ApiFuture<DocumentSnapshot> hubFuture = hubRef.get();
            DocumentSnapshot hubDocument = hubFuture.get();

            if (hubDocument.exists()) {
                Map<String, Object> hubData = hubDocument.getData();
                hubData.put("hubId", hubDocument.getId()); // Add the hubId to the response
                userHubs.add(hubData);  // Add hub data to the list
            }
        }
        return userHubs;
    }

}
