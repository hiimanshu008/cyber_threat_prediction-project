package com.example.threatprediction.service;

import com.example.threatprediction.model.SystemConfig;
import com.example.threatprediction.repository.SystemConfigRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConfigService {
    private final SystemConfigRepository systemConfigRepository;

    public ConfigService(SystemConfigRepository systemConfigRepository) {
        this.systemConfigRepository = systemConfigRepository;
    }

    @Value("${security.virustotal.apiKey:}")
    private String vtKeyProp;

    @Value("${ai.openai.apiKey:}")
    private String openaiKeyProp;

    @Value("${ai.gemini.apiKey:}")
    private String geminiKeyProp;

    public String getVirusTotalKey() {
        if (vtKeyProp != null && !vtKeyProp.isBlank()) return vtKeyProp;
        Optional<SystemConfig> c = systemConfigRepository.findByConfigNameAndActiveTrue("virusTotalAPI");
        return c.map(SystemConfig::getApiKey).orElse("");
    }

    public String getOpenAIKey() {
        if (openaiKeyProp != null && !openaiKeyProp.isBlank()) return openaiKeyProp;
        Optional<SystemConfig> c = systemConfigRepository.findByConfigNameAndActiveTrue("openAIApi");
        return c.map(SystemConfig::getApiKey).orElse("");
    }

    public String getGeminiKey() {
        if (geminiKeyProp != null && !geminiKeyProp.isBlank()) return geminiKeyProp;
        return "";
    }
}
