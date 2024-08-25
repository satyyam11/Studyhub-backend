package com.studyhub.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.studyhub.model.ProgressReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class ProgressReportService {

    @Autowired
    private Firestore firestore;

    public String createProgressReport(ProgressReport report) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("progressReports").document();
        report.setId(docRef.getId());
        Map<String, Object> reportMap = new HashMap<>();
        reportMap.put("id", report.getId());
        reportMap.put("userId", report.getUserId());
        reportMap.put("hubId", report.getHubId());
        reportMap.put("reportContent", report.getReportContent());
        reportMap.put("createdAt", report.getCreatedAt().toString());

        ApiFuture<WriteResult> result = docRef.set(reportMap);
        return report.getId();
    }

    public List<ProgressReport> getReportsByUserId(String userId) throws ExecutionException, InterruptedException {
        CollectionReference reportsRef = (CollectionReference) firestore;
        return null;
    }

    public List<ProgressReport> getReportsByHubId(String hubId) {
        return null;
    }

    public ProgressReport getReportById(String reportId) {
        return null;
    }

    public String deleteReport(String reportId) {
        return reportId;
    }
}
