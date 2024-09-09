package com.studyhub.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class FriendService {

    private final Firestore db = FirestoreClient.getFirestore();

    // Add a friend
    public void addFriend(String id, String userid) {
        DocumentReference userRef = db.collection("users").document(id);

        // Update the 'friends' field in the user's document
        ApiFuture<WriteResult> future = userRef.update("friends", FieldValue.arrayUnion(userid));

        try {
            future.get(); // Wait for the write to complete
            System.out.println("Added friend: User ID = " + id + ", Friend ID = " + userid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Remove a friend
    public void removeFriend(String userId, String friendId) {
        DocumentReference userRef = db.collection("users").document(userId);

        // Update the 'friends' field in the user's document
        ApiFuture<WriteResult> future = userRef.update("friends", FieldValue.arrayRemove(friendId));

        try {
            future.get(); // Wait for the write to complete
            System.out.println("Removed friend: User ID = " + userId + ", Friend ID = " + friendId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Update a friend's details (if necessary, e.g., status changes)
    public void updateFriend(String id, String userid, Map<String, Object> updates) {
        // This is a placeholder for any additional friend details you want to update
        // For example, you might have a separate 'friends' subcollection where you store detailed friend info
        DocumentReference userRef = db.collection("users").document(id);

        // No specific updates for 'friends' field in this example
        // Update logic can be added here if needed
    }

    // List all friends of a user
    public List<String> listFriends(String userId) {
        DocumentReference userRef = db.collection("users").document(userId);

        try {
            // Fetch the document and retrieve the 'friends' field
            Map<String, Object> userDoc = userRef.get().get().getData();
            if (userDoc != null && userDoc.containsKey("friends")) {
                return (List<String>) userDoc.get("friends");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
