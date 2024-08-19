package com.studyhub.controller;

import com.studyhub.model.Hut;
import com.studyhub.service.HutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/hubs/{hubId}/huts")
public class HutController {

    @Autowired
    private HutService hutService;

    @PostMapping
    public String createHut(@PathVariable String hubId,
                            @RequestParam String name,
                            @RequestParam String type) {
        try {
            return hutService.createHut(hubId, name, type);
        } catch (ExecutionException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating hut", e);
        }
    }

    @GetMapping
    public List<Hut> getHuts(@PathVariable String hubId) {
        try {
            return hutService.getHuts(hubId);
        } catch (ExecutionException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving huts", e);
        }
    }

    @GetMapping("/{hutId}")
    public Hut getHutById(@PathVariable String hubId, @PathVariable String hutId) {
        try {
            return hutService.getHutById(hubId, hutId);
        } catch (ExecutionException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving hut", e);
        }
    }

    @DeleteMapping("/{hutId}")
    public void deleteHut(@PathVariable String hubId, @PathVariable String hutId) {
        try {
            hutService.deleteHut(hubId, hutId);
        } catch (ExecutionException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting hut", e);
        }
    }
}
