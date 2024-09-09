package com.studyhub.controller;

import com.studyhub.model.Note;
import com.studyhub.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @PostMapping
    public ResponseEntity<String> createNote(@RequestBody Note note) {
        try {
            String noteId = noteService.createNote(note);
            return ResponseEntity.status(HttpStatus.CREATED).body(noteId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating note: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userid}")
    public ResponseEntity<List<Note>> getNotesByuserid(@PathVariable String userid) {
        try {
            List<Note> notes = noteService.getNotesByuserid(userid);
            return ResponseEntity.ok(notes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<Note> getNoteById(@PathVariable String noteId) {
        try {
            Note note = noteService.getNoteById(noteId);
            if (note == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(note);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<String> updateNote(@PathVariable String noteId, @RequestBody Note note) {
        try {
            String result = noteService.updateNote(noteId, note.getTitle(), note.getContent());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating note: " + e.getMessage());
        }
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<String> deleteNote(@PathVariable String noteId) {
        try {
            String result = noteService.deleteNote(noteId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting note: " + e.getMessage());
        }
    }
}
