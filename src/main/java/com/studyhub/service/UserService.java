package com.studyhub.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.studyhub.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {

    @Autowired
    private Firestore firestore;

    private static final String USERS_COLLECTION = "users";

    @CachePut(value = "users", key = "#user.uid")
    public User createUser(User user) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(USERS_COLLECTION).document(user.getUid());
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        ApiFuture<WriteResult> future = docRef.set(user);
        future.get();
        return user;
    }

    @Cacheable(value = "users", key = "#uid")
    public User getUserById(String uid) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(USERS_COLLECTION).document(uid);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot doc = future.get();
        if (doc.exists()) {
            return doc.toObject(User.class);
        }
        return null;
    }

    @CachePut(value = "users", key = "#uid")
    public User updateUser(String uid, User userUpdates) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(USERS_COLLECTION).document(uid);
        userUpdates.setUid(uid);
        userUpdates.setUpdatedAt(new Date());
        ApiFuture<WriteResult> future = docRef.set(userUpdates, SetOptions.merge());
        future.get();
        return getUserById(uid);
    }

    @CacheEvict(value = "users", key = "#uid")
    public void deleteUser(String uid) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(USERS_COLLECTION).document(uid);
        ApiFuture<WriteResult> future = docRef.delete();
        future.get();
    }

    public List<User> searchUsers(String query) throws ExecutionException, InterruptedException {
        CollectionReference usersCol = firestore.collection(USERS_COLLECTION);
        ApiFuture<QuerySnapshot> future1 = usersCol
                .whereGreaterThanOrEqualTo("displayName", query)
                .whereLessThanOrEqualTo("displayName", query + "\uf8ff")
                .get();
        List<QueryDocumentSnapshot> docs1 = future1.get().getDocuments();

        ApiFuture<QuerySnapshot> future2 = usersCol
                .whereGreaterThanOrEqualTo("email", query)
                .whereLessThanOrEqualTo("email", query + "\uf8ff")
                .get();
        List<QueryDocumentSnapshot> docs2 = future2.get().getDocuments();

        List<User> users = new ArrayList<>();
        for (QueryDocumentSnapshot doc : docs1) {
            users.add(doc.toObject(User.class));
        }
        for (QueryDocumentSnapshot doc : docs2) {
            User user = doc.toObject(User.class);
            boolean exists = users.stream().anyMatch(u -> u.getUid().equals(user.getUid()));
            if (!exists) {
                users.add(user);
            }
        }
        return users;
    }
}
