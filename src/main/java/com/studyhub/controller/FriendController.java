package com.studyhub.controller;

import com.studyhub.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @PostMapping("/add")
    public ResponseEntity<String> addFriend(@RequestParam String id, @RequestParam String userid) {
        if(id == null || userid == null || id.isEmpty() || userid.isEmpty()) {
            return new ResponseEntity<>("Invalid parameters", HttpStatus.BAD_REQUEST);
        }
        friendService.addFriend(id, userid);
        return new ResponseEntity<>("Friend added", HttpStatus.CREATED);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFriend(@RequestParam String id, @RequestParam String userid) {
        if(id == null || userid == null || id.isEmpty() || userid.isEmpty()) {
            return new ResponseEntity<>("Invalid parameters", HttpStatus.BAD_REQUEST);
        }
        friendService.removeFriend(id, userid);
        return new ResponseEntity<>("Friend removed", HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateFriend(@RequestParam String id, @RequestParam String userid, @RequestBody Map<String, Object> updates) {
        if(id == null || userid == null || id.isEmpty() || userid.isEmpty() || updates == null) {
            return new ResponseEntity<>("Invalid parameters", HttpStatus.BAD_REQUEST);
        }
        friendService.updateFriend(id, userid, updates);
        return new ResponseEntity<>("Friend updated", HttpStatus.OK);
    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<List<String>> listFriends(@PathVariable String userId) {
        if(userId == null || userId.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<String> friends = friendService.listFriends(userId);
        return new ResponseEntity<>(friends, HttpStatus.OK);
    }
}