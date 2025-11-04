package com.example.threatprediction.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "system_config")
public class SystemConfig {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    private String configName;
    private String apiKey;
    private Instant lastUpdated;
    private Boolean active;

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }
    public String getConfigName() { return configName; }
    public void setConfigName(String configName) { this.configName = configName; }
    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public Instant getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Instant lastUpdated) { this.lastUpdated = lastUpdated; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
