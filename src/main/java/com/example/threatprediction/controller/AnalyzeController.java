package com.example.threatprediction.controller;

import com.example.threatprediction.model.LogEntry;
import com.example.threatprediction.model.Threat;
import com.example.threatprediction.service.DetectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AnalyzeController {
    private final DetectionService detectionService;

    public AnalyzeController(DetectionService detectionService) {
        this.detectionService = detectionService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<Threat> analyze(@RequestBody LogEntry log) {
        Threat t = detectionService.analyzeAndPersist(log);
        return ResponseEntity.ok(t);
    }
}
