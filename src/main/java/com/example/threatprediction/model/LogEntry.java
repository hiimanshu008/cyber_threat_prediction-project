package com.example.threatprediction.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "logs")
public class LogEntry {
    @Id
    private ObjectId id;
    private Instant timestamp;
    private String sourceIP;
    private String destinationIP;
    private Integer port;
    private String protocol;
    private String message;
    private String severity; // info, warning, critical
    private String status;   // raw, processed
    private Boolean threatDetected;
    private Double threatScore;
    private ObjectId threatId;
    private Instant createdAt;
    private Instant updatedAt;

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public String getSourceIP() { return sourceIP; }
    public void setSourceIP(String sourceIP) { this.sourceIP = sourceIP; }
    public String getDestinationIP() { return destinationIP; }
    public void setDestinationIP(String destinationIP) { this.destinationIP = destinationIP; }
    public Integer getPort() { return port; }
    public void setPort(Integer port) { this.port = port; }
    public String getProtocol() { return protocol; }
    public void setProtocol(String protocol) { this.protocol = protocol; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Boolean getThreatDetected() { return threatDetected; }
    public void setThreatDetected(Boolean threatDetected) { this.threatDetected = threatDetected; }
    public Double getThreatScore() { return threatScore; }
    public void setThreatScore(Double threatScore) { this.threatScore = threatScore; }
    public ObjectId getThreatId() { return threatId; }
    public void setThreatId(ObjectId threatId) { this.threatId = threatId; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
