package com.example.threatprediction.service;

import org.springframework.stereotype.Service;

@Service
public class ThreatScoringService {
    public double computeFinalScore(double frequencyScore, int virusTotalScore, double aiThreatScore) {
        double vt = virusTotalScore / 100.0;
        double wFreq = 0.35;
        double wVt = 0.35;
        double wAi = 0.30;
        double score = wFreq * clamp01(frequencyScore) + wVt * clamp01(vt) + wAi * clamp01(aiThreatScore);
        return Math.max(0.0, Math.min(1.0, score));
    }

    private double clamp01(double v) {
        return Math.max(0.0, Math.min(1.0, v));
    }
}
