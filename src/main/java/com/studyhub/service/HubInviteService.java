package com.studyhub.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.studyhub.config.RoleConstants;
import com.studyhub.model.HubInvite;
import com.studyhub.model.Notification;
import com.studyhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class HubInviteService {

    @Autowired
    private Firestore firestore;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserHubService userHubService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    private static final String INVITES_COLLECTION = "hubInvites";

    public HubInvite createInvite(String hubId, String inviterId, String inviteeId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(INVITES_COLLECTION).document();
        HubInvite invite = new HubInvite();
        invite.setId(docRef.getId());
        invite.setHubId(hubId);
        invite.setInviterId(inviterId);
        invite.setInviteeId(inviteeId);
        invite.setStatus("PENDING");
        invite.setCreatedAt(new Date());
        invite.setUpdatedAt(new Date());
        ApiFuture<WriteResult> future = docRef.set(invite);
        future.get();

        // Send notification
        Notification notification = new Notification();
        notification.setUserId(inviteeId);
        notification.setType("HUB_INVITE");
        notification.setTitle("New Hub Invite");
        notification.setMessage("You've been invited to join a hub!");
        notification.setRelatedId(hubId);
        notificationService.createNotification(notification);

        return invite;
    }

    public List<HubInvite> getInvitesForUser(String userId) throws ExecutionException, InterruptedException {
        CollectionReference col = firestore.collection(INVITES_COLLECTION);
        ApiFuture<QuerySnapshot> future = col.whereEqualTo("inviteeId", userId).whereEqualTo("status", "PENDING").get();
        List<QueryDocumentSnapshot> docs = future.get().getDocuments();
        List<HubInvite> invites = new ArrayList<>();
        for (QueryDocumentSnapshot doc : docs) {
            invites.add(doc.toObject(HubInvite.class));
        }
        return invites;
    }

    public HubInvite acceptInvite(String inviteId, String userId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(INVITES_COLLECTION).document(inviteId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot doc = future.get();
        if (doc.exists()) {
            HubInvite invite = doc.toObject(HubInvite.class);
            if (invite != null && invite.getInviteeId().equals(userId) && invite.getStatus().equals("PENDING")) {
                invite.setStatus("ACCEPTED");
                invite.setUpdatedAt(new Date());
                ApiFuture<WriteResult> writeFuture = docRef.set(invite);
                writeFuture.get();

                // Add user to hub
                userHubService.addHubToUser(userId, invite.getHubId());
                roleService.addMember(invite.getHubId(), userId);

                return invite;
            }
        }
        return null;
    }

    public HubInvite declineInvite(String inviteId, String userId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(INVITES_COLLECTION).document(inviteId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot doc = future.get();
        if (doc.exists()) {
            HubInvite invite = doc.toObject(HubInvite.class);
            if (invite != null && invite.getInviteeId().equals(userId) && invite.getStatus().equals("PENDING")) {
                invite.setStatus("DECLINED");
                invite.setUpdatedAt(new Date());
                ApiFuture<WriteResult> writeFuture = docRef.set(invite);
                writeFuture.get();
                return invite;
            }
        }
        return null;
    }
}
