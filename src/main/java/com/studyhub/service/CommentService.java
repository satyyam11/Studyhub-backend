package com.studyhub.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.studyhub.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class CommentService {

    @Autowired
    private Firestore firestore;

    private static final String COMMENTS_COLLECTION = "comments";

    public Comment createComment(Comment comment) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COMMENTS_COLLECTION).document();
        comment.setId(docRef.getId());
        comment.setCreatedAt(new Date());
        comment.setUpdatedAt(new Date());
        ApiFuture<WriteResult> future = docRef.set(comment);
        future.get();
        return comment;
    }

    public List<Comment> getCommentsForNote(String noteId) throws ExecutionException, InterruptedException {
        CollectionReference col = firestore.collection(COMMENTS_COLLECTION);
        ApiFuture<QuerySnapshot> future = col.whereEqualTo("noteId", noteId).orderBy("createdAt").get();
        List<QueryDocumentSnapshot> docs = future.get().getDocuments();
        List<Comment> comments = new ArrayList<>();
        for (QueryDocumentSnapshot doc : docs) {
            comments.add(doc.toObject(Comment.class));
        }
        return comments;
    }

    public Comment getCommentById(String commentId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COMMENTS_COLLECTION).document(commentId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot doc = future.get();
        if (doc.exists()) {
            return doc.toObject(Comment.class);
        }
        return null;
    }

    public Comment updateComment(String commentId, String userId, String content) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COMMENTS_COLLECTION).document(commentId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot doc = future.get();
        if (doc.exists()) {
            Comment comment = doc.toObject(Comment.class);
            if (comment != null && comment.getUserId().equals(userId)) {
                comment.setContent(content);
                comment.setUpdatedAt(new Date());
                ApiFuture<WriteResult> writeFuture = docRef.set(comment);
                writeFuture.get();
                return comment;
            }
        }
        return null;
    }

    public void deleteComment(String commentId, String userId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COMMENTS_COLLECTION).document(commentId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot doc = future.get();
        if (doc.exists()) {
            Comment comment = doc.toObject(Comment.class);
            if (comment != null && comment.getUserId().equals(userId)) {
                ApiFuture<WriteResult> deleteFuture = docRef.delete();
                deleteFuture.get();
            }
        }
    }
}
