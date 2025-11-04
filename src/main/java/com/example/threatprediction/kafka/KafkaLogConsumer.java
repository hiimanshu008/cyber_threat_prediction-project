package com.example.threatprediction.kafka;

import com.example.threatprediction.model.LogEntry;
import com.example.threatprediction.service.DetectionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaLogConsumer {
    private final DetectionService detectionService;
    private final ObjectMapper mapper = new ObjectMapper();

    public KafkaLogConsumer(DetectionService detectionService) {
        this.detectionService = detectionService;
    }

    @KafkaListener(topics = "${app.kafka.topic:network-logs}")
    public void consume(String message) {
        try {
            LogEntry log = mapper.readValue(message, LogEntry.class);
            detectionService.analyzeAndPersist(log);
        } catch (Exception ignored) {
        }
    }
}
