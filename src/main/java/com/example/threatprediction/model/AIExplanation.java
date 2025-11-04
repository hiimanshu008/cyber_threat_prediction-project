package com.example.threatprediction.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "ai_explanations")
public class AIExplanation {
    @Id
    private ObjectId id;
    private ObjectId threatId;
    private String aiModel;
    private String prompt;
    private String response;
    private Integer tokensUsed;
    private Double confidence;
    private Instant createdAt;

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }
    public ObjectId getThreatId() { return threatId; }
    public void setThreatId(ObjectId threatId) { this.threatId = threatId; }
    public String getAiModel() { return aiModel; }
    public void setAiModel(String aiModel) { this.aiModel = aiModel; }
    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }
    public Integer getTokensUsed() { return tokensUsed; }
    public void setTokensUsed(Integer tokensUsed) { this.tokensUsed = tokensUsed; }
    public Double getConfidence() { return confidence; }
    public void setConfidence(Double confidence) { this.confidence = confidence; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
