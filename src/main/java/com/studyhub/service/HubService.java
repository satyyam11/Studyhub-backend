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

        List<Map<String, Object>> userHubs = new ArrayList<>();

        // First, get hubs where user is creator
        Query creatorQuery = firestore.collection("hubs").whereEqualTo("creatorId", userId);
        ApiFuture<QuerySnapshot> creatorFuture = creatorQuery.get();
        List<QueryDocumentSnapshot> creatorDocs = creatorFuture.get().getDocuments();

        for (QueryDocumentSnapshot doc : creatorDocs) {
            Map<String, Object> hubData = doc.getData();
            hubData.put("hubId", doc.getId());
            userHubs.add(hubData);
        }

        // Next, get hubs from user_hubs collection (where user is a member)
        Query userHubsQuery = firestore.collection("user_hubs").whereEqualTo("userId", userId);
        ApiFuture<QuerySnapshot> userHubsFuture = userHubsQuery.get();
        List<QueryDocumentSnapshot> userHubDocs = userHubsFuture.get().getDocuments();

        for (QueryDocumentSnapshot doc : userHubDocs) {
            String hubId = (String) doc.get("hubId");
            if (hubId != null && !hubId.isEmpty()) {
                DocumentReference hubRef = firestore.collection("hubs").document(hubId);
                ApiFuture<DocumentSnapshot> hubFuture = hubRef.get();
                DocumentSnapshot hubDoc = hubFuture.get();
                if (hubDoc.exists()) {
                    Map<String, Object> hubData = hubDoc.getData();
                    hubData.put("hubId", hubDoc.getId());
                    // Avoid duplicates in case user is both creator and member
                    boolean alreadyAdded = userHubs.stream().anyMatch(h -> h.get("hubId").equals(hubId));
                    if (!alreadyAdded) {
                        userHubs.add(hubData);
                    }
                }
            }
        }

        return userHubs;
    }

}
