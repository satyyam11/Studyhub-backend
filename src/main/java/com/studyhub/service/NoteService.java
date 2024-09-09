package com.studyhub.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.studyhub.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class NoteService {

    @Autowired
    private Firestore firestore;

    public String createNote(Note note) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("notes").document();
        note.setId(docRef.getId());
        Map<String, Object> noteMap = new HashMap<>();
        noteMap.put("id", note.getId());
        noteMap.put("title", note.getTitle());
        noteMap.put("content", note.getContent());
        noteMap.put("userid", note.getuserid());
        noteMap.put("createdAt", note.getCreatedAt().toString());
        noteMap.put("updatedAt", note.getUpdatedAt().toString());

        ApiFuture<WriteResult> result = docRef.set(noteMap);
        return note.getId();
    }

    public List<Note> getNotesByuserid(String userid) throws ExecutionException, InterruptedException {
        CollectionReference notesRef = firestore.collection("notes");
        Query query = notesRef.whereEqualTo("userid", userid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        return documents.stream().map(doc -> doc.toObject(Note.class)).collect(Collectors.toList());
    }

    public Note getNoteById(String noteId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("notes").document(noteId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            return document.toObject(Note.class);
        } else {
            return null;
        }
    }

    public String updateNote(String noteId, String title, String content) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("notes").document(noteId);

        Map<String, Object> updates = new HashMap<>();
        updates.put("title", title);
        updates.put("content", content);
        updates.put("updatedAt", LocalDateTime.now().toString());

        ApiFuture<WriteResult> writeResult = docRef.update(updates);
        return "Note updated successfully.";
    }

    public String deleteNote(String noteId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("notes").document(noteId);
        ApiFuture<WriteResult> writeResult = docRef.delete();
        return "Note deleted successfully.";
    }
}
