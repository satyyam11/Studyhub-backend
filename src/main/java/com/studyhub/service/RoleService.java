package com.studyhub.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.studyhub.config.RoleConstants;
import com.studyhub.model.RoleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class RoleService {

    @Autowired
    private Firestore firestore;

    private static final String HUB_ROLES_COLLECTION = "hubRoles";

    // Assign a role to a user in a hub
    public void assignRole(String hubId, String userId, String role) throws ExecutionException, InterruptedException {
        validateRole(role);
        DocumentReference roleRef = firestore.collection(HUB_ROLES_COLLECTION).document(hubId).collection("roles").document(userId);
        RoleModel roleModel = new RoleModel(role);
        ApiFuture<WriteResult> writeResult = roleRef.set(roleModel);
        writeResult.get(); // Wait for completion
    }

    // Assign the creator role when creating a hub
    public void assignCreatorRole(String hubId, String creatorId) throws ExecutionException, InterruptedException {
        assignRole(hubId, creatorId, RoleConstants.ROLE_CREATOR);
    }

    // Add a user as a member by default
    public void addMember(String hubId, String userId) throws ExecutionException, InterruptedException {
        assignRole(hubId, userId, RoleConstants.ROLE_MEMBER);
    }

    // Promote a member to aide
    public void promoteToAide(String hubId, String userId) throws ExecutionException, InterruptedException {
        assignRole(hubId, userId, RoleConstants.ROLE_AIDE);
    }

    // Reassign a role to a user
    public void reassignRole(String hubId, String userId, String newRole) throws ExecutionException, InterruptedException {
        validateRole(newRole);
        assignRole(hubId, userId, newRole);
    }

    // Delete a role from a user
    public void deleteRole(String hubId, String userId) throws ExecutionException, InterruptedException {
        DocumentReference roleRef = firestore.collection(HUB_ROLES_COLLECTION).document(hubId).collection("roles").document(userId);
        ApiFuture<WriteResult> writeResult = roleRef.delete();
        writeResult.get(); // Wait for completion
    }

    // Get the role of a user in a hub
    public String getRole(String hubId, String userId) throws ExecutionException, InterruptedException {
        DocumentReference roleRef = firestore.collection(HUB_ROLES_COLLECTION).document(hubId).collection("roles").document(userId);
        ApiFuture<com.google.cloud.firestore.DocumentSnapshot> future = roleRef.get();
        com.google.cloud.firestore.DocumentSnapshot document = future.get();
        if (document.exists()) {
            RoleModel roleModel = document.toObject(RoleModel.class);
            return roleModel != null ? roleModel.getRole() : null;
        } else {
            return null;
        }
    }

    private void validateRole(String role) {
        if (!RoleConstants.ROLE_CREATOR.equals(role) &&
                !RoleConstants.ROLE_AIDE.equals(role) &&
                !RoleConstants.ROLE_MEMBER.equals(role)) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }
}
