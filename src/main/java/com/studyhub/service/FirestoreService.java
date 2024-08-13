package com.studyhub.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FirestoreService {

    private final Firestore firestore;

    @Autowired
    public FirestoreService(Firestore firestore) {
        this.firestore = firestore;
    }

    public ApiFuture<DocumentSnapshot> getDocument(String collectionName, String documentId) {
        DocumentReference docRef = firestore.collection(collectionName).document(documentId);
        return docRef.get();
    }

    public ApiFuture<WriteResult> setDocument(String collectionName, String documentId, Map<String, Object> data) {
        DocumentReference docRef = firestore.collection(collectionName).document(documentId);
        return docRef.set(data);
    }

    public ApiFuture<DocumentReference> createDocument(String collectionName, Map<String, Object> data) {
        CollectionReference collection = firestore.collection(collectionName);
        return collection.add(data);
    }

    public ApiFuture<WriteResult> updateDocument(String collectionName, String documentId, Map<String, Object> data) {
        DocumentReference docRef = firestore.collection(collectionName).document(documentId);
        return docRef.update(data);
    }

    public ApiFuture<WriteResult> deleteDocument(String collectionName, String documentId) {
        DocumentReference docRef = firestore.collection(collectionName).document(documentId);
        return docRef.delete();
    }

    public ApiFuture<QuerySnapshot> customQuery(String collectionName, String field, String value) {
        CollectionReference collection = firestore.collection(collectionName);
        Query query = collection.whereEqualTo(field, value);
        return query.get();
    }
}
