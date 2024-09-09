package com.studyhub.controller;

import com.studyhub.model.Hut;
import com.studyhub.service.HutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/hubs/{hubId}/huts")
public class HutController {

    @Autowired
    private HutService hutService;

    @PostMapping
    public ResponseEntity<String> createHut(@PathVariable String hubId,
                                            @RequestParam String name,
                                            @RequestParam String type) {
        try {
            String hutId = hutService.createHut(hubId, name, type);
            return ResponseEntity.status(HttpStatus.CREATED).body(hutId);
        } catch (ExecutionException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating hut", e);
        }
    }

    @GetMapping
    public ResponseEntity<List<Hut>> getHuts(@PathVariable String hubId) {
        try {
            List<Hut> huts = hutService.getHuts(hubId);
            if (huts.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(huts);
        } catch (ExecutionException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving huts", e);
        }
    }

    @GetMapping("/{hutId}")
    public ResponseEntity<Hut> getHutById(@PathVariable String hubId, @PathVariable String hutId) {
        try {
            Hut hut = hutService.getHutById(hubId, hutId);
            if (hut == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(hut);
        } catch (ExecutionException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving hut", e);
        }
    }

    @DeleteMapping("/{hutId}")
    public ResponseEntity<Void> deleteHut(@PathVariable String hubId, @PathVariable String hutId) {
        try {
            hutService.deleteHut(hubId, hutId);
            return ResponseEntity.noContent().build();
        } catch (ExecutionException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting hut", e);
        }
    }
}
