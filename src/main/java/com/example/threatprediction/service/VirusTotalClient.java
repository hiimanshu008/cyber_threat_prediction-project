package com.example.threatprediction.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class VirusTotalClient {
    private final WebClient webClient;
    private final ConfigService configService;

    public VirusTotalClient(WebClient webClient, ConfigService configService) {
        this.webClient = webClient;
        this.configService = configService;
    }

    public int reputationScoreForIP(String ip) {
        String apiKey = configService.getVirusTotalKey();
        if (apiKey == null || apiKey.isBlank()) {
            return 0;
        }
        try {
            String url = "https://www.virustotal.com/api/v3/ip_addresses/" + ip;
            Map<?,?> resp = webClient.get()
                    .uri(url)
                    .header("x-apikey", apiKey)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .onErrorResume(e -> Mono.just(Map.of()))
                    .block();
            if (resp == null) return 0;
            Object data = resp.get("data");
            if (data instanceof Map<?,?> d) {
                Object attributes = d.get("attributes");
                if (attributes instanceof Map<?,?> a) {
                    Object last = a.get("last_analysis_stats");
                    if (last instanceof Map<?,?> stats) {
                        int malicious = toInt(stats.get("malicious"));
                        int suspicious = toInt(stats.get("suspicious"));
                        int harmless = toInt(stats.get("harmless"));
                        int undetected = toInt(stats.get("undetected"));
                        int total = malicious + suspicious + harmless + undetected;
                        if (total == 0) return 0;
                        int score = Math.min(100, (int)Math.round(((malicious * 1.0 + suspicious * 0.5) / total) * 100));
                        return score;
                    }
                }
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    private static int toInt(Object value) {
        if (value instanceof Number n) return n.intValue();
        try {
            return value != null ? Integer.parseInt(String.valueOf(value)) : 0;
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }
}
