package com.studyhub.model;

import java.time.LocalDateTime;

public class ProgressReport {

    private String id;
    private String userId;
    private String hubId;
    private String reportContent;
    private LocalDateTime createdAt;

    // Constructors
    public ProgressReport() {
    }

    public ProgressReport(String userId, String hubId, String reportContent) {
        this.userId = userId;
        this.hubId = hubId;
        this.reportContent = reportContent;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHubId() {
        return hubId;
    }

    public void setHubId(String hubId) {
        this.hubId = hubId;
    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
