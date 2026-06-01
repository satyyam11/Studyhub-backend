package com.studyhub.controller;

import com.studyhub.annotation.RequireRole;
import com.studyhub.config.RoleConstants;
import com.studyhub.model.Poll;
import com.studyhub.service.PollService;
import com.studyhub.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/hubs/{hubId}/polls")
public class PollController {

    @Autowired
    private PollService pollService;

    @PostMapping
    public ResponseEntity<Poll> createPoll(@PathVariable String hubId, @RequestBody Poll poll) {
        try {
            String uid = SecurityUtil.getCurrentUserUid();
            if (uid == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            poll.setHubId(hubId);
            poll.setCreatorId(uid);
            Poll created = pollService.createPoll(poll);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Poll>> getPolls(@PathVariable String hubId) {
        try {
            List<Poll> polls = pollService.getPollsForHub(hubId);
            return ResponseEntity.ok(polls);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{pollId}")
    public ResponseEntity<Poll> getPoll(@PathVariable String hubId, @PathVariable String pollId) {
        try {
            Poll poll = pollService.getPollById(pollId);
            if (poll == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(poll);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{pollId}/vote")
    public ResponseEntity<Poll> vote(@PathVariable String hubId, @PathVariable String pollId, @RequestBody Map<String, String> body) {
        try {
            String uid = SecurityUtil.getCurrentUserUid();
            if (uid == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            String option = body.get("option");
            Poll poll = pollService.vote(pollId, uid, option);
            if (poll == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(poll);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{pollId}/close")
    @RequireRole({RoleConstants.ROLE_CREATOR, RoleConstants.ROLE_AIDE})
    public ResponseEntity<Poll> closePoll(@PathVariable String hubId, @PathVariable String pollId) {
        try {
            Poll poll = pollService.closePoll(pollId);
            if (poll == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(poll);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
