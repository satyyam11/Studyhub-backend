package com.studyhub.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.studyhub.model.Notification;
import com.studyhub.model.StudySession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class StudySessionService {

    @Autowired
    private Firestore firestore;

    @Autowired
    private NotificationService notificationService;

    private static final String SESSIONS_COLLECTION = "studySessions";

    public StudySession createSession(StudySession session) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(SESSIONS_COLLECTION).document();
        session.setId(docRef.getId());
        session.setCreatedAt(new Date());
        session.setUpdatedAt(new Date());
        ApiFuture<WriteResult> future = docRef.set(session);
        future.get();

        // Send notifications to participants
        if (session.getParticipantIds() != null) {
            for (String userId : session.getParticipantIds()) {
                Notification notification = new Notification();
                notification.setUserId(userId);
                notification.setType("STUDY_SESSION");
                notification.setTitle("New Study Session");
                notification.setMessage("You've been invited to a study session: " + session.getTitle());
                notification.setRelatedId(session.getId());
                notificationService.createNotification(notification);
            }
        }

        return session;
    }

    public List<StudySession> getSessionsForHub(String hubId) throws ExecutionException, InterruptedException {
        CollectionReference col = firestore.collection(SESSIONS_COLLECTION);
        ApiFuture<QuerySnapshot> future = col.whereEqualTo("hubId", hubId).orderBy("startTime").get();
        List<QueryDocumentSnapshot> docs = future.get().getDocuments();
        List<StudySession> sessions = new ArrayList<>();
        for (QueryDocumentSnapshot doc : docs) {
            sessions.add(doc.toObject(StudySession.class));
        }
        return sessions;
    }

    public StudySession getSessionById(String sessionId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(SESSIONS_COLLECTION).document(sessionId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot doc = future.get();
        if (doc.exists()) {
            return doc.toObject(StudySession.class);
        }
        return null;
    }

    public StudySession updateSession(String sessionId, StudySession sessionUpdates) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(SESSIONS_COLLECTION).document(sessionId);
        sessionUpdates.setId(sessionId);
        sessionUpdates.setUpdatedAt(new Date());
        ApiFuture<WriteResult> future = docRef.set(sessionUpdates, SetOptions.merge());
        future.get();
        return getSessionById(sessionId);
    }

    public void deleteSession(String sessionId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(SESSIONS_COLLECTION).document(sessionId);
        ApiFuture<WriteResult> future = docRef.delete();
        future.get();
    }
}
