package com.studyhub.controller;

import com.studyhub.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @PostMapping("/add")
    public String addFriend(@RequestParam String id, @RequestParam String userid) {
        friendService.addFriend(id, userid);
        friendService.addFriend(userid, id);
        return "Friend added";
    }

    @DeleteMapping("/remove")
    public String removeFriend(@RequestParam String id, @RequestParam String userid) {
        friendService.removeFriend(id, userid);
        return "Friend removed";
    }

    @PutMapping("/update")
    public String updateFriend(@RequestParam String id, @RequestParam String userid, @RequestBody Map<String, Object> updates) {
        friendService.updateFriend(id, userid, updates);
        return "Friend updated";
    }

    @GetMapping("/list/{userId}")
    public List<String> listFriends(@PathVariable String userId) {
        return friendService.listFriends(userId);
    }
}
