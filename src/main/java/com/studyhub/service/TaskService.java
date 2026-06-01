package com.studyhub.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.studyhub.model.Notification;
import com.studyhub.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class TaskService {

    @Autowired
    private Firestore firestore;

    @Autowired
    private NotificationService notificationService;

    private static final String TASKS_COLLECTION = "tasks";

    public Task createTask(Task task) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(TASKS_COLLECTION).document();
        task.setId(docRef.getId());
        task.setCreatedAt(new Date());
        task.setUpdatedAt(new Date());
        if (task.getStatus() == null) {
            task.setStatus("TODO");
        }
        ApiFuture<WriteResult> future = docRef.set(task);
        future.get();

        // Send notifications to assigned users
        if (task.getAssignedUserIds() != null) {
            for (String userId : task.getAssignedUserIds()) {
                Notification notification = new Notification();
                notification.setUserId(userId);
                notification.setType("TASK_ASSIGNED");
                notification.setTitle("New Task Assigned");
                notification.setMessage("You've been assigned a new task: " + task.getTitle());
                notification.setRelatedId(task.getId());
                notificationService.createNotification(notification);
            }
        }

        return task;
    }

    public List<Task> getTasksForHub(String hubId) throws ExecutionException, InterruptedException {
        CollectionReference col = firestore.collection(TASKS_COLLECTION);
        ApiFuture<QuerySnapshot> future = col.whereEqualTo("hubId", hubId).get();
        List<QueryDocumentSnapshot> docs = future.get().getDocuments();
        List<Task> tasks = new ArrayList<>();
        for (QueryDocumentSnapshot doc : docs) {
            tasks.add(doc.toObject(Task.class));
        }
        return tasks;
    }

    public Task getTaskById(String taskId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(TASKS_COLLECTION).document(taskId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot doc = future.get();
        if (doc.exists()) {
            return doc.toObject(Task.class);
        }
        return null;
    }

    public Task updateTask(String taskId, Task taskUpdates) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(TASKS_COLLECTION).document(taskId);
        taskUpdates.setId(taskId);
        taskUpdates.setUpdatedAt(new Date());
        ApiFuture<WriteResult> future = docRef.set(taskUpdates, SetOptions.merge());
        future.get();
        return getTaskById(taskId);
    }

    public void deleteTask(String taskId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(TASKS_COLLECTION).document(taskId);
        ApiFuture<WriteResult> future = docRef.delete();
        future.get();
    }
}
