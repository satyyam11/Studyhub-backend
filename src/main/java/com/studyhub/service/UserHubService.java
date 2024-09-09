package com.studyhub.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class UserHubService {

    @Autowired
    private Firestore firestore;

    private static final String USER_HUBS_COLLECTION = "user_hubs";

    // Get the list of Hubs a user has joined
    public List<String> getJoinedHubs(String userId) throws ExecutionException, InterruptedException {
        DocumentReference userHubRef = firestore.collection(USER_HUBS_COLLECTION).document(userId);
        ApiFuture<com.google.cloud.firestore.DocumentSnapshot> future = userHubRef.get();
        com.google.cloud.firestore.DocumentSnapshot document = future.get();

        List<String> hubIds = new ArrayList<>();
        if (document.exists()) {
            List<String> hubs = (List<String>) document.get("hubs");
            if (hubs != null) {
                hubIds.addAll(hubs);
            }
        }
        return hubIds;
    }

    // Add a Hub to a user's joined list
    public void addHubToUser(String userId, String hubId) throws ExecutionException, InterruptedException {
        DocumentReference userHubRef = firestore.collection(USER_HUBS_COLLECTION).document(userId);
        ApiFuture<com.google.cloud.firestore.DocumentSnapshot> future = userHubRef.get();
        com.google.cloud.firestore.DocumentSnapshot document = future.get();

        List<String> hubs;
        if (document.exists()) {
            hubs = (List<String>) document.get("hubs");
            if (hubs == null) {
                hubs = new ArrayList<>();
            }
        } else {
            hubs = new ArrayList<>();
        }

        if (!hubs.contains(hubId)) {
            hubs.add(hubId);
            userHubRef.set(Map.of("hubs", hubs));
        }
    }
}
