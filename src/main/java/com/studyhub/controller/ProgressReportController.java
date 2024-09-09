package com.studyhub.controller;

import com.studyhub.model.ProgressReport;
import com.studyhub.service.ProgressReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/progress-reports")
public class ProgressReportController {

    @Autowired
    private ProgressReportService progressReportService;

    @PostMapping
    public ResponseEntity<String> createProgressReport(@RequestBody ProgressReport report) {
        try {
            String reportId = progressReportService.createProgressReport(report);
            return ResponseEntity.status(HttpStatus.CREATED).body(reportId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating progress report: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProgressReport>> getReportsByUserId(@PathVariable String userId) {
        try {
            List<ProgressReport> reports = progressReportService.getReportsByUserId(userId);
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/hub/{hubId}")
    public ResponseEntity<List<ProgressReport>> getReportsByHubId(@PathVariable String hubId) {
        try {
            List<ProgressReport> reports = progressReportService.getReportsByHubId(hubId);
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<ProgressReport> getReportById(@PathVariable String reportId) {
        try {
            ProgressReport report = progressReportService.getReportById(reportId);
            if (report == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<String> deleteReport(@PathVariable String reportId) {
        try {
            String result = progressReportService.deleteReport(reportId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting progress report: " + e.getMessage());
        }
    }
}
