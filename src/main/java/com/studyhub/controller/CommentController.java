package com.studyhub.controller;

import com.studyhub.model.Comment;
import com.studyhub.service.CommentService;
import com.studyhub.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/notes/{noteId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<Comment> createComment(@PathVariable String noteId, @RequestBody Comment comment) {
        try {
            String uid = SecurityUtil.getCurrentUserUid();
            if (uid == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            comment.setNoteId(noteId);
            comment.setUserId(uid);
            Comment created = commentService.createComment(comment);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Comment>> getComments(@PathVariable String noteId) {
        try {
            List<Comment> comments = commentService.getCommentsForNote(noteId);
            return ResponseEntity.ok(comments);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable String noteId, @PathVariable String commentId, @RequestBody Comment commentUpdates) {
        try {
            String uid = SecurityUtil.getCurrentUserUid();
            if (uid == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            Comment updated = commentService.updateComment(commentId, uid, commentUpdates.getContent());
            if (updated == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(updated);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable String noteId, @PathVariable String commentId) {
        try {
            String uid = SecurityUtil.getCurrentUserUid();
            if (uid == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            commentService.deleteComment(commentId, uid);
            return ResponseEntity.noContent().build();
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
