package com.example.threatprediction.controller;

import com.example.threatprediction.model.AIExplanation;
import com.example.threatprediction.repository.AIExplanationRepository;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {
    private final AIExplanationRepository aiRepo;

    public SummaryController(AIExplanationRepository aiRepo) {
        this.aiRepo = aiRepo;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AIExplanation> getByThreatId(@PathVariable("id") String id) {
        return aiRepo.findTopByThreatIdOrderByCreatedAtDesc(new ObjectId(id))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
