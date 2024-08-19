package com.studyhub.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
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

    /**
     * Creates a new Hub if a Hub with the same name does not already exist.
     *
     * @param name       The name of the Hub.
     * @param creatorId  The ID of the user creating the Hub.
     * @param isPublic   Whether the Hub is public or private.
     * @return           A message indicating the result of the creation.
     * @throws ExecutionException   If the Firestore operation fails.
     * @throws InterruptedException If the Firestore operation is interrupted.
     */
    public String createHub(String name, String creatorId, boolean isPublic) throws ExecutionException, InterruptedException {
        // Check if a Hub with the same name already exists
        Query query = firestore.collection("hubs").whereEqualTo("name", name);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        if (!documents.isEmpty()) {
            return "Error: A Hub with this name already exists.";
        }

        // If no existing Hub with the same name, proceed to create a new one
        Map<String, Object> hub = new HashMap<>();
        hub.put("name", name);
        hub.put("creatorId", creatorId);
        hub.put("isPublic", isPublic);

        ApiFuture<DocumentReference> futureRef = firestore.collection("hubs").add(hub);
        DocumentReference docRef = futureRef.get();
        String documentId = docRef.getId();
        return "Hub created with ID: " + documentId;
    }

    /**
     * Retrieves a Hub by its ID.
     *
     * @param id The ID of the Hub.
     * @return   A map containing the Hub data or an error message.
     * @throws ExecutionException   If the Firestore operation fails.
     * @throws InterruptedException If the Firestore operation is interrupted.
     */
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

    /**
     * Updates an existing Hub.
     *
     * @param id         The ID of the Hub to update.
     * @param name       The new name of the Hub.
     * @param isPublic   Whether the Hub is public or private.
     * @return           A message indicating the result of the update.
     * @throws ExecutionException   If the Firestore operation fails.
     * @throws InterruptedException If the Firestore operation is interrupted.
     */
    public String updateHub(String id, String name, boolean isPublic) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("hubs").document(id);

        // Check if the Hub exists
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (!document.exists()) {
            return "Error: No such Hub exists.";
        }

        // Update the Hub
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", name);
        updates.put("isPublic", isPublic);

        ApiFuture<WriteResult> writeResult = docRef.update(updates);
        writeResult.get();
        return "Hub updated successfully.";
    }

    /**
     * Deletes a Hub by its ID.
     *
     * @param id The ID of the Hub to delete.
     * @return   A message indicating the result of the deletion.
     * @throws ExecutionException   If the Firestore operation fails.
     * @throws InterruptedException If the Firestore operation is interrupted.
     */
    public String deleteHub(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("hubs").document(id);

        // Check if the Hub exists
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (!document.exists()) {
            return "Error: No such Hub exists.";
        }

        // Delete the Hub
        ApiFuture<WriteResult> writeResult = docRef.delete();
        writeResult.get();
        return "Hub deleted successfully.";
    }
}
