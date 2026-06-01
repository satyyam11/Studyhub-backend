package com.studyhub.controller;

import com.studyhub.model.StudySession;
import com.studyhub.service.StudySessionService;
import com.studyhub.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/hubs/{hubId}/sessions")
public class StudySessionController {

    @Autowired
    private StudySessionService studySessionService;

    @PostMapping
    public ResponseEntity<StudySession> createSession(@PathVariable String hubId, @RequestBody StudySession session) {
        try {
            String uid = SecurityUtil.getCurrentUserUid();
            if (uid == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            session.setHubId(hubId);
            session.setCreatorId(uid);
            StudySession created = studySessionService.createSession(session);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<StudySession>> getSessions(@PathVariable String hubId) {
        try {
            List<StudySession> sessions = studySessionService.getSessionsForHub(hubId);
            return ResponseEntity.ok(sessions);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<StudySession> getSession(@PathVariable String hubId, @PathVariable String sessionId) {
        try {
            StudySession session = studySessionService.getSessionById(sessionId);
            if (session == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(session);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{sessionId}")
    public ResponseEntity<StudySession> updateSession(@PathVariable String hubId, @PathVariable String sessionId, @RequestBody StudySession sessionUpdates) {
        try {
            StudySession updated = studySessionService.updateSession(sessionId, sessionUpdates);
            if (updated == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(updated);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteSession(@PathVariable String hubId, @PathVariable String sessionId) {
        try {
            studySessionService.deleteSession(sessionId);
            return ResponseEntity.noContent().build();
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
