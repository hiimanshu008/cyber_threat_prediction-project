package com.example.threatprediction.service;

import com.example.threatprediction.model.AIExplanation;
import com.example.threatprediction.model.LogEntry;
import com.example.threatprediction.model.Threat;
import com.example.threatprediction.repository.AIExplanationRepository;
import com.example.threatprediction.repository.LogEntryRepository;
import com.example.threatprediction.repository.ThreatRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Service
public class DetectionService {
    private final LogEntryRepository logRepo;
    private final ThreatRepository threatRepo;
    private final AIExplanationRepository aiRepo;
    private final VirusTotalClient virusTotalClient;
    private final GeminiClient geminiClient;
    private final ThreatScoringService scoringService;

    public DetectionService(LogEntryRepository logRepo,
                            ThreatRepository threatRepo,
                            AIExplanationRepository aiRepo,
                            VirusTotalClient virusTotalClient,
                            GeminiClient geminiClient,
                            ThreatScoringService scoringService) {
        this.logRepo = logRepo;
        this.threatRepo = threatRepo;
        this.aiRepo = aiRepo;
        this.virusTotalClient = virusTotalClient;
        this.geminiClient = geminiClient;
        this.scoringService = scoringService;
    }

    public Threat analyzeAndPersist(LogEntry log) {
        if (log.getTimestamp() == null) log.setTimestamp(Instant.now());
        if (log.getCreatedAt() == null) log.setCreatedAt(Instant.now());
        log.setUpdatedAt(Instant.now());
        if (log.getStatus() == null) log.setStatus("processed");
        log = logRepo.save(log);

        Instant end = Instant.now();
        Instant start = end.minus(10, ChronoUnit.MINUTES);
        int frequency = logRepo.findBySourceIPAndTimestampBetween(log.getSourceIP(), start, end).size();
        double frequencyScore = Math.min(1.0, frequency / 20.0);

        int vtScore = 0;
        if (log.getSourceIP() != null) {
            vtScore = virusTotalClient.reputationScoreForIP(log.getSourceIP());
        }

        String prompt = buildPrompt(log, frequency, vtScore);
        Map<String, Object> ai = geminiClient.explainThreat(prompt);
        double aiThreatScore = ai.get("confidence") instanceof Number n ? n.doubleValue() : 0.0;
        double finalScore = scoringService.computeFinalScore(frequencyScore, vtScore, aiThreatScore);

        Threat t = new Threat();
        t.setLogId(log.getId());
        t.setSourceIP(log.getSourceIP());
        t.setDestinationIP(log.getDestinationIP());
        t.setThreatType(deriveThreatType(log));
        t.setVirusTotalScore(vtScore);
        t.setFrequencyScore(frequencyScore);
        t.setAiThreatScore(aiThreatScore);
        t.setFinalThreatScore(finalScore);
        t.setStatus("active");
        t.setCreatedAt(Instant.now());
        t = threatRepo.save(t);

        AIExplanation ex = new AIExplanation();
        ex.setThreatId(t.getId());
        ex.setAiModel("gemini-1.5-flash");
        ex.setPrompt(prompt);
        ex.setResponse(String.valueOf(ai.getOrDefault("content", "")));
        ex.setTokensUsed(((Number) ai.getOrDefault("tokens", 0)).intValue());
        ex.setConfidence(((Number) ai.getOrDefault("confidence", 0.0)).doubleValue());
        ex.setCreatedAt(Instant.now());
        ex = aiRepo.save(ex);

        log.setThreatDetected(true);
        log.setThreatScore(finalScore);
        log.setThreatId(t.getId());
        log.setUpdatedAt(Instant.now());
        logRepo.save(log);

        t.setAiExplanationId(ex.getId());
        threatRepo.save(t);

        return t;
    }

    private String deriveThreatType(LogEntry log) {
        String msg = log.getMessage() != null ? log.getMessage().toLowerCase() : "";
        if (msg.contains("failed login") || msg.contains("authentication")) return "Brute Force Attack";
        if (msg.contains("port scan") || msg.contains("scan")) return "Port Scan";
        if (msg.contains("malware")) return "Malware Communication";
        return "Suspicious Activity";
    }

    private String buildPrompt(LogEntry log, int frequency, int vtScore) {
        return "Explain the security risk for the following network log in one concise paragraph and suggest 2 remediation steps. " +
                "Log:{sourceIP=" + log.getSourceIP() + ", destinationIP=" + log.getDestinationIP() + ", port=" + log.getPort() +
                ", protocol=" + log.getProtocol() + ", message='" + log.getMessage() + "'} " +
                "Observations:{recentEventsFromSource=" + frequency + ", virusTotalScore=" + vtScore + "}.";
    }
}
