package com.studyhub.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.studyhub.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class NotificationService {

    @Autowired
    private Firestore firestore;

    private static final String NOTIFICATIONS_COLLECTION = "notifications";

    public Notification createNotification(Notification notification) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(NOTIFICATIONS_COLLECTION).document();
        notification.setId(docRef.getId());
        notification.setCreatedAt(new Date());
        notification.setRead(false);
        ApiFuture<WriteResult> future = docRef.set(notification);
        future.get();
        return notification;
    }

    public List<Notification> getNotificationsForUser(String userId) throws ExecutionException, InterruptedException {
        CollectionReference col = firestore.collection(NOTIFICATIONS_COLLECTION);
        ApiFuture<QuerySnapshot> future = col.whereEqualTo("userId", userId).orderBy("createdAt", Query.Direction.DESCENDING).get();
        List<QueryDocumentSnapshot> docs = future.get().getDocuments();
        List<Notification> notifications = new ArrayList<>();
        for (QueryDocumentSnapshot doc : docs) {
            notifications.add(doc.toObject(Notification.class));
        }
        return notifications;
    }

    public Notification markAsRead(String notificationId, String userId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(NOTIFICATIONS_COLLECTION).document(notificationId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot doc = future.get();
        if (doc.exists()) {
            Notification notification = doc.toObject(Notification.class);
            if (notification != null && notification.getUserId().equals(userId)) {
                notification.setRead(true);
                ApiFuture<WriteResult> writeFuture = docRef.set(notification);
                writeFuture.get();
                return notification;
            }
        }
        return null;
    }

    public void deleteNotification(String notificationId, String userId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(NOTIFICATIONS_COLLECTION).document(notificationId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot doc = future.get();
        if (doc.exists()) {
            Notification notification = doc.toObject(Notification.class);
            if (notification != null && notification.getUserId().equals(userId)) {
                ApiFuture<WriteResult> deleteFuture = docRef.delete();
                deleteFuture.get();
            }
        }
    }
}
