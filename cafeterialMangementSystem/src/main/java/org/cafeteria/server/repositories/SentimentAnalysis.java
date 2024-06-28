package org.cafeteria.server.repositories;

import org.cafeteria.server.model.SentimentResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SentimentAnalysis {
    private static final Set<String> STOP_WORDS = new HashSet<>();
    private static final Set<String> POSITIVE_WORDS = new HashSet<>();
    private static final Set<String> NEGATIVE_WORDS = new HashSet<>();
    private static final Set<String> NEUTRAL_WORDS = new HashSet<>();
    private static final Set<String> NEGATION_WORDS = new HashSet<>();

    static {
        STOP_WORDS.addAll(Set.of("a", "an", "and", "the", "is", "in", "at", "of", "on", "for", "with", "by", "to", "from", "are", "if", "but", "did", "as", "be", "it", "or"));
        POSITIVE_WORDS.addAll(Set.of("delicious", "tasty", "excellent", "great", "good", "nice", "yummy", "fantastic", "happy", "joy", "positive", "fortunate", "correct", "superior"));
        NEGATIVE_WORDS.addAll(Set.of("bad", "awful", "terrible", "disgusting", "horrible", "poor", "unpleasant", "sad", "negative", "unfortunate", "wrong", "inferior"));
        NEUTRAL_WORDS.addAll(Set.of("okay", "average", "mediocre", "passable", "fair"));
        NEGATION_WORDS.addAll(Set.of("not", "no", "never", "don't", "doesn't", "didn't", "wasn't", "weren't", "isn't", "aren't", "neither", "nor"));
    }

    public SentimentResult calculateAverageSentimentAnalysis(List<String> comments) {
        StringBuilder combinedComment = new StringBuilder();
        for (String comment : comments) {
            combinedComment.append(comment);
        }
        return analyzeSentiment(combinedComment.toString());
    }

    public SentimentResult analyzeSentiment(String comment) {
        if (comment == null || comment.trim().isEmpty()) {
            return new SentimentResult("Neutral", 0, 0, 0);
        }

        double positiveCount = 0, negativeCount = 0, neutralCount = 0;
        boolean negation = false;
        List<String> filteredTokens = tokenizeAndFilter(comment);

        for (String token : filteredTokens) {
            if (NEGATION_WORDS.contains(token)) {
                negation = true;
            } else {
                if (POSITIVE_WORDS.contains(token)) {
                    if (negation) {
                        negativeCount++;
                    } else {
                        positiveCount++;
                    }
                } else if (NEGATIVE_WORDS.contains(token)) {
                    if (negation) {
                        positiveCount++;
                    } else {
                        negativeCount++;
                    }
                } else if (NEUTRAL_WORDS.contains(token)) {
                    if (negation) {
                        negativeCount += 0.5;
                    }
                    neutralCount++;
                } else {
                    neutralCount++;
                }
                negation = false;
            }
        }

        double totalSentimentWords = positiveCount + negativeCount + neutralCount;
        if (totalSentimentWords == 0) {
            return new SentimentResult("Neutral", 0, 0, 0);
        }

        double positiveScore = (positiveCount / totalSentimentWords) * 100;
        double negativeScore = (negativeCount / totalSentimentWords) * 100;
        double neutralScore = (neutralCount / totalSentimentWords) * 100;

        String sentiment = determineSentiment(positiveScore, negativeScore, neutralScore);

        return new SentimentResult(sentiment, positiveScore, negativeScore, neutralScore);
    }

    private List<String> tokenizeAndFilter(String comment) {
        String[] tokens = comment.toLowerCase().split("[\\s\\p{Punct}]+");
        List<String> filteredTokens = new ArrayList<>();

        for (String token : tokens) {
            if (!STOP_WORDS.contains(token)) {
                filteredTokens.add(token);
            }
        }
        return filteredTokens;
    }

    private String determineSentiment(double positiveScore, double negativeScore, double neutralScore) {
        if (positiveScore > negativeScore && positiveScore > neutralScore) {
            return "Positive";
        } else if (negativeScore > positiveScore && negativeScore > neutralScore) {
            return "Negative";
        } else {
            return "Neutral";
        }
    }
}