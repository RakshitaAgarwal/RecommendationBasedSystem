package org.cafeteria.server.model;

public class SentimentResult {
    private String sentiment;
    private double positiveSentimentScore;
    private double negativeSentimentScore;
    private double neutralSentimentScore;

    public SentimentResult(String sentiment, double positiveSentimentScore, double negativeSentimentScore, double neutralSentimentScore) {
        this.sentiment = sentiment;
        this.positiveSentimentScore = positiveSentimentScore;
        this.negativeSentimentScore = negativeSentimentScore;
        this.neutralSentimentScore = neutralSentimentScore;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public double getPositiveSentimentScore() {
        return positiveSentimentScore;
    }

    public void setPositiveSentimentScore(double positiveSentimentScore) {
        this.positiveSentimentScore = positiveSentimentScore;
    }

    public double getNegativeSentimentScore() {
        return negativeSentimentScore;
    }

    public void setNegativeSentimentScore(double negativeSentimentScore) {
        this.negativeSentimentScore = negativeSentimentScore;
    }

    public double getNeutralSentimentScore() {
        return neutralSentimentScore;
    }

    public void setNeutralSentimentScore(double neutralSentimentScore) {
        this.neutralSentimentScore = neutralSentimentScore;
    }
}