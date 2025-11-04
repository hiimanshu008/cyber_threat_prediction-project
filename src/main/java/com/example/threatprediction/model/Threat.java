package com.example.threatprediction.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "threats")
public class Threat {
    @Id
    private ObjectId id;
    private ObjectId logId;
    private String sourceIP;
    private String destinationIP;
    private String threatType;
    private Integer virusTotalScore; // 0-100
    private Double frequencyScore;   // 0-1
    private Double aiThreatScore;    // 0-1
    private Double finalThreatScore; // 0-1
    private String status;           // active, resolved
    private ObjectId aiExplanationId;
    private Instant createdAt;

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }
    public ObjectId getLogId() { return logId; }
    public void setLogId(ObjectId logId) { this.logId = logId; }
    public String getSourceIP() { return sourceIP; }
    public void setSourceIP(String sourceIP) { this.sourceIP = sourceIP; }
    public String getDestinationIP() { return destinationIP; }
    public void setDestinationIP(String destinationIP) { this.destinationIP = destinationIP; }
    public String getThreatType() { return threatType; }
    public void setThreatType(String threatType) { this.threatType = threatType; }
    public Integer getVirusTotalScore() { return virusTotalScore; }
    public void setVirusTotalScore(Integer virusTotalScore) { this.virusTotalScore = virusTotalScore; }
    public Double getFrequencyScore() { return frequencyScore; }
    public void setFrequencyScore(Double frequencyScore) { this.frequencyScore = frequencyScore; }
    public Double getAiThreatScore() { return aiThreatScore; }
    public void setAiThreatScore(Double aiThreatScore) { this.aiThreatScore = aiThreatScore; }
    public Double getFinalThreatScore() { return finalThreatScore; }
    public void setFinalThreatScore(Double finalThreatScore) { this.finalThreatScore = finalThreatScore; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public ObjectId getAiExplanationId() { return aiExplanationId; }
    public void setAiExplanationId(ObjectId aiExplanationId) { this.aiExplanationId = aiExplanationId; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
