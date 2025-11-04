package com.example.threatprediction.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeminiClient {
    private final WebClient webClient;
    private final ConfigService configService;

    @Value("${ai.gemini.model:gemini-1.5-flash}")
    private String model;

    @Value("${ai.gemini.base-url:https://generativelanguage.googleapis.com/v1beta}")
    private String baseUrl;

    public GeminiClient(WebClient webClient, ConfigService configService) {
        this.webClient = webClient;
        this.configService = configService;
    }

    public Map<String, Object> explainThreat(String prompt) {
        String apiKey = configService.getGeminiKey();
        if (apiKey == null || apiKey.isBlank()) {
            Map<String, Object> r = new HashMap<>();
            r.put("content", "AI key not configured. Unable to generate explanation.");
            r.put("tokens", 0);
            r.put("confidence", 0.0);
            return r;
        }
        Map<String, Object> body = new HashMap<>();
        Map<String, Object> userPart = new HashMap<>();
        userPart.put("text", prompt);
        Map<String, Object> userContent = new HashMap<>();
        userContent.put("role", "user");
        userContent.put("parts", List.of(userPart));
        body.put("contents", List.of(userContent));

        String url = baseUrl + "/models/" + model + ":generateContent?key=" + apiKey;
        Map<?,?> resp = webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .onErrorReturn(Map.of())
                .block();

        String content = "";
        int tokens = 0;
        double confidence = 0.9;
        if (resp != null) {
            Object candidates = resp.get("candidates");
            if (candidates instanceof List<?> list && !list.isEmpty()) {
                Object c0 = list.get(0);
                if (c0 instanceof Map<?,?> m) {
                    Object cont = m.get("content");
                    if (cont instanceof Map<?,?> cm) {
                        Object parts = cm.get("parts");
                        if (parts instanceof List<?> pl && !pl.isEmpty()) {
                            Object p0 = pl.get(0);
                            if (p0 instanceof Map<?,?> pm) {
                                Object t = pm.get("text");
                                if (t instanceof String s) content = s;
                            }
                        }
                    }
                }
            }
            Object usage = resp.get("usageMetadata");
            if (usage instanceof Map<?,?> u) {
                Object total = u.get("totalTokenCount");
                if (total instanceof Number n) tokens = n.intValue();
            }
        }
        Map<String, Object> r = new HashMap<>();
        r.put("content", content);
        r.put("tokens", tokens);
        r.put("confidence", confidence);
        return r;
    }
}
