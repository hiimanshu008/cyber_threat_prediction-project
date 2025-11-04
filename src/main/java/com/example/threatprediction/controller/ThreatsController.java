package com.example.threatprediction.controller;

import com.example.threatprediction.model.Threat;
import com.example.threatprediction.repository.ThreatRepository;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/threats")
public class ThreatsController {
    private final ThreatRepository threatRepo;

    public ThreatsController(ThreatRepository threatRepo) {
        this.threatRepo = threatRepo;
    }

    @GetMapping
    public List<Threat> all() {
        return threatRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Threat> byId(@PathVariable("id") String id) {
        return threatRepo.findById(new ObjectId(id))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
