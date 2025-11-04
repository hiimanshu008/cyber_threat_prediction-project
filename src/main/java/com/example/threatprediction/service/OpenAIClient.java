package com.example.threatprediction.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIClient {
    private final WebClient webClient;
    private final ConfigService configService;

    @Value("${ai.openai.model:gpt-4-turbo}")
    private String model;

    public OpenAIClient(WebClient webClient, ConfigService configService) {
        this.webClient = webClient;
        this.configService = configService;
    }

    public Map<String, Object> explainThreat(String prompt) {
        String apiKey = configService.getOpenAIKey();
        if (apiKey == null || apiKey.isBlank()) {
            Map<String, Object> r = new HashMap<>();
            r.put("content", "AI key not configured. Unable to generate explanation.");
            r.put("tokens", 0);
            r.put("confidence", 0.0);
            return r;
        }
        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", List.of(Map.of("role", "user", "content", prompt)));
        Map<?,?> resp = webClient.post()
                .uri("https://api.openai.com/v1/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
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
            Object choices = resp.get("choices");
            if (choices instanceof List<?> list && !list.isEmpty()) {
                Object c0 = list.get(0);
                if (c0 instanceof Map<?,?> m) {
                    Object message = m.get("message");
                    if (message instanceof Map<?,?> mm) {
                        Object cont = mm.get("content");
                        if (cont instanceof String s) content = s;
                    }
                }
            }
            Object usage = resp.get("usage");
            if (usage instanceof Map<?,?> u) {
                Object total = u.get("total_tokens");
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
