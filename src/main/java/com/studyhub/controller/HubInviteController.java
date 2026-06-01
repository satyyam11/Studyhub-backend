package com.studyhub.controller;

import com.studyhub.annotation.RequireRole;
import com.studyhub.config.RoleConstants;
import com.studyhub.model.HubInvite;
import com.studyhub.service.HubInviteService;
import com.studyhub.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/hubs/{hubId}/invites")
public class HubInviteController {

    @Autowired
    private HubInviteService hubInviteService;

    @PostMapping
    @RequireRole({RoleConstants.ROLE_CREATOR, RoleConstants.ROLE_AIDE})
    public ResponseEntity<HubInvite> createInvite(@PathVariable String hubId, @RequestParam String inviteeId) {
        try {
            String uid = SecurityUtil.getCurrentUserUid();
            if (uid == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            HubInvite invite = hubInviteService.createInvite(hubId, uid, inviteeId);
            return ResponseEntity.status(HttpStatus.CREATED).body(invite);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/my")
    public ResponseEntity<List<HubInvite>> getMyInvites() {
        try {
            String uid = SecurityUtil.getCurrentUserUid();
            if (uid == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            List<HubInvite> invites = hubInviteService.getInvitesForUser(uid);
            return ResponseEntity.ok(invites);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{inviteId}/accept")
    public ResponseEntity<HubInvite> acceptInvite(@PathVariable String inviteId) {
        try {
            String uid = SecurityUtil.getCurrentUserUid();
            if (uid == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            HubInvite invite = hubInviteService.acceptInvite(inviteId, uid);
            if (invite == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(invite);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{inviteId}/decline")
    public ResponseEntity<HubInvite> declineInvite(@PathVariable String inviteId) {
        try {
            String uid = SecurityUtil.getCurrentUserUid();
            if (uid == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            HubInvite invite = hubInviteService.declineInvite(inviteId, uid);
            if (invite == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(invite);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
