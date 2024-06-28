package org.cafeteria.server.repositories;

import org.cafeteria.server.model.SentimentResult;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class SentimentAnalysis {
    private static final Set<String> STOP_WORDS = new HashSet<>();
    private static final Set<String> POSITIVE_WORDS = new HashSet<>();
    private static final Set<String> NEGATIVE_WORDS = new HashSet<>();
    private static final Set<String> NEUTRAL_WORDS = new HashSet<>();
    private static final Set<String> NEGATION_WORDS = new HashSet<>();

    static {
        STOP_WORDS.addAll(Set.of("a", "an", "and", "the", "is", "in", "at", "of", "on", "for", "with", "by", "to", "from"));
        POSITIVE_WORDS.addAll(Set.of("delicious", "tasty", "excellent", "great", "good", "nice", "yummy", "fantastic"));
        NEGATIVE_WORDS.addAll(Set.of("bad", "awful", "terrible", "disgusting", "horrible", "poor", "unpleasant"));
        NEUTRAL_WORDS.addAll(Set.of("okay", "average", "mediocre", "passable", "fair"));
        NEGATION_WORDS.addAll(Set.of("not", "no", "never", "don't", "doesn't", "didn't", "wasn't", "weren't", "isn't", "aren't", "neither", "nor"));
    }

    private static final Pattern WORD_PATTERN = Pattern.compile("\\b\\w+\\b");

    public SentimentResult calculateAverageSentimentAnalysis(List<String> comments) {
        String combinedComment = "";
        for(String comment: comments) {
            combinedComment = combinedComment + comment;
        }
        return analyzeSentiment(combinedComment);
    }

    public SentimentResult analyzeSentiment(String comment) {
        if (comment == null || comment.trim().isEmpty()) {
            return new SentimentResult("Neutral", 0, 0, 0);
        }

        int positiveCount = 0, negativeCount = 0, neutralCount = 0, totalWords = 0;
        boolean negation = false;

        var matcher = WORD_PATTERN.matcher(comment.toLowerCase());
        String prevWord = "";

        while (matcher.find()) {
            String word = matcher.group();

            if (STOP_WORDS.contains(word)) {
                continue;
            }

            totalWords++;

            if (NEGATION_WORDS.contains(word)) {
                negation = true;
                prevWord = word;
                continue;
            }

            if (POSITIVE_WORDS.contains(word)) {
                if (negation) {
                    negativeCount++;
                } else {
                    positiveCount++;
                }
                negation = false;
            } else if (NEGATIVE_WORDS.contains(word)) {
                if (negation) {
                    positiveCount++;
                } else {
                    negativeCount++;
                }
                negation = false;
            } else if (NEUTRAL_WORDS.contains(word)) {
                neutralCount++;
                negation = false;
            } else {
                negation = false;
            }

            prevWord = word;
        }

        int totalSentimentWords = positiveCount + negativeCount + neutralCount;
        if (totalSentimentWords == 0) {
            return new SentimentResult("Neutral", 0, 0, 0);
        }

        double positiveScore = (double) positiveCount / totalSentimentWords*100;
        double negativeScore = (double) negativeCount / totalSentimentWords*100;
        double neutralScore = (double) neutralCount / totalSentimentWords*100;

        String sentiment;
        if (positiveScore > negativeScore && positiveScore > neutralScore) {
            sentiment = "Positive";
        } else if (negativeScore > positiveScore && negativeScore > neutralScore) {
            sentiment = "Negative";
        } else {
            sentiment = "Neutral";
        }

        return new SentimentResult(sentiment, positiveScore, negativeScore, neutralScore);
    }
}