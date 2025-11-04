package com.example.threatprediction.controller;

import com.example.threatprediction.model.LogEntry;
import com.example.threatprediction.model.Threat;
import com.example.threatprediction.repository.LogEntryRepository;
import com.example.threatprediction.service.DetectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogsController {
    private final LogEntryRepository logRepo;
    private final DetectionService detectionService;

    public LogsController(LogEntryRepository logRepo, DetectionService detectionService) {
        this.logRepo = logRepo;
        this.detectionService = detectionService;
    }

    @GetMapping
    public List<LogEntry> all() {
        return logRepo.findAll();
    }

    @PostMapping
    public ResponseEntity<Threat> ingestAndAnalyze(@RequestBody LogEntry log) {
        Threat t = detectionService.analyzeAndPersist(log);
        return ResponseEntity.ok(t);
    }
}
