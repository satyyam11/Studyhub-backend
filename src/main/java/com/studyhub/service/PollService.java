package com.studyhub.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.studyhub.model.Poll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class PollService {

    @Autowired
    private Firestore firestore;

    private static final String POLLS_COLLECTION = "polls";

    public Poll createPoll(Poll poll) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(POLLS_COLLECTION).document();
        poll.setId(docRef.getId());
        poll.setCreatedAt(new Date());
        poll.setActive(true);
        ApiFuture<WriteResult> future = docRef.set(poll);
        future.get();
        return poll;
    }

    public List<Poll> getPollsForHub(String hubId) throws ExecutionException, InterruptedException {
        CollectionReference col = firestore.collection(POLLS_COLLECTION);
        ApiFuture<QuerySnapshot> future = col.whereEqualTo("hubId", hubId).get();
        List<QueryDocumentSnapshot> docs = future.get().getDocuments();
        List<Poll> polls = new ArrayList<>();
        for (QueryDocumentSnapshot doc : docs) {
            polls.add(doc.toObject(Poll.class));
        }
        return polls;
    }

    public Poll getPollById(String pollId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(POLLS_COLLECTION).document(pollId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot doc = future.get();
        if (doc.exists()) {
            return doc.toObject(Poll.class);
        }
        return null;
    }

    public Poll vote(String pollId, String userId, String option) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(POLLS_COLLECTION).document(pollId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot doc = future.get();
        if (doc.exists()) {
            Poll poll = doc.toObject(Poll.class);
            if (poll != null && poll.isActive()) {
                Map<String, String> votes = poll.getVotes();
                votes.put(userId, option);
                poll.setVotes(votes);
                ApiFuture<WriteResult> writeFuture = docRef.set(poll);
                writeFuture.get();
                return poll;
            }
        }
        return null;
    }

    public Poll closePoll(String pollId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(POLLS_COLLECTION).document(pollId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot doc = future.get();
        if (doc.exists()) {
            Poll poll = doc.toObject(Poll.class);
            if (poll != null) {
                poll.setActive(false);
                ApiFuture<WriteResult> writeFuture = docRef.set(poll);
                writeFuture.get();
                return poll;
            }
        }
        return null;
    }
}
