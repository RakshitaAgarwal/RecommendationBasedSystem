package org.cafeteria.server.model;

public record SentimentResult(
        String sentiment,
        double positiveSentimentScore,
        double negativeSentimentScore,
        double neutralSentimentScore,
        String keyPhrase
) { }