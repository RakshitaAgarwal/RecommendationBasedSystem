package org.cafeteria.server.repositories;

import org.cafeteria.server.model.SentimentResult;

import java.util.*;

public class SentimentAnalysis {
    private static final Set<String> STOP_WORDS = new HashSet<>();
    private static final Set<String> POSITIVE_WORDS = new HashSet<>();
    private static final Set<String> NEGATIVE_WORDS = new HashSet<>();
    private static final Set<String> NEUTRAL_WORDS = new HashSet<>();
    private static final Set<String> NEGATION_WORDS = new HashSet<>();

    static {
        STOP_WORDS.addAll(Set.of(
                "by", "are", "if", "but", "did", "a", "an", "and", "as", "at", "be", "for", "from", "had", "has", "have",
                "he", "her", "hers", "him", "his", "I", "in", "into", "is", "it", "its", "me", "my", "of", "on", "or",
                "our", "she", "that", "the", "their", "them", "there", "they", "this", "to", "us", "was", "we", "were",
                "what", "when", "where", "which", "who", "with", "you", "your", "about", "during", "before", "after",
                "above", "below", "up", "down", "out", "over", "under", "again", "further", "then", "once", "here",
                "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such", "only",
                "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just", "don", "should", "now",
                "could", "i", "while", "although", "though", "however", "also", "no", "not", "every", "either", "neither",
                "another", "much", "many", "less", "fewer", "several", "quite", "rather", "first", "last", "next",
                "between", "among", "since", "until", "without", "through", "because", "per", "due", "like"));

        POSITIVE_WORDS.addAll(Set.of(
                "delicious", "tasty", "excellent", "great", "good", "nice", "yummy", "fantastic", "happy", "joy",
                "positive", "fortunate", "correct", "superior", "Excellent", "Delicious", "Great", "Fantastic",
                "Perfect", "Loved", "Enjoyed", "Wonderful", "Amazing"));

        NEGATIVE_WORDS.addAll(Set.of(
                "bad", "awful", "terrible", "disgusting", "horrible", "poor", "unpleasant", "sad", "negative",
                "unfortunate", "wrong", "inferior", "Disappointed", "Bland", "Overcooked", "Tough", "Dry", "Greasy",
                "Salty", "Watery", "Old"));

        NEUTRAL_WORDS.addAll(Set.of(
                "okay", "average", "mediocre", "passable", "fair", "Decent", "Fine", "Average", "Satisfactory",
                "Needed", "But"));

        NEGATION_WORDS.addAll(Set.of(
                "not", "no", "never", "don't", "doesn't", "didn't", "wasn't", "weren't", "isn't", "aren't", "neither",
                "nor"));
    }

    public SentimentResult calculateAverageSentimentAnalysis(List<String> comments, String menuItemName) {
        updateStopWordsList(menuItemName);
        StringBuilder combinedComment = new StringBuilder();
        for (String comment : comments) {
            combinedComment.append(comment);
        }
        return analyzeSentiment(combinedComment.toString());
    }

    private void updateStopWordsList(String menuItemName) {
        String lowerCaseMenuItemName = menuItemName.toLowerCase();
        STOP_WORDS.add(lowerCaseMenuItemName);
        String[] words = lowerCaseMenuItemName.split("\\s+");
        STOP_WORDS.addAll(Arrays.asList(words));
    }

    public SentimentResult analyzeSentiment(String comment) {
        if (comment == null || comment.trim().isEmpty()) {
            return new SentimentResult("Neutral", 0, 0, 0, "");
        }

        List<String> filteredComment = tokenizeAndFilter(comment);

        double positiveCount = 0, negativeCount = 0, neutralCount = 0;
        boolean negation = false;

        for (String token : filteredComment) {
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
            return new SentimentResult("Neutral", 0, 0, 0, "");
        }

        double positiveScore = (positiveCount / totalSentimentWords) * 100;
        double negativeScore = (negativeCount / totalSentimentWords) * 100;
        double neutralScore = (neutralCount / totalSentimentWords) * 100;

        String sentiment = determineSentiment(positiveScore, negativeScore, neutralScore);

        Map<String, Integer> phraseFrequency = extractPhrasesWithFrequency(filteredComment);

        return new SentimentResult(sentiment, positiveScore, negativeScore, neutralScore, extractKeyPhrase(phraseFrequency, 3));
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
            return "Good";
        } else if (neutralScore > positiveScore && neutralScore > negativeScore) {
            return "OK";
        } else {
            return "Bad";
        }
    }

    private Map<String, Integer> extractPhrasesWithFrequency(List<String> filteredComment) {
        Map<String, Integer> phraseFrequency = new HashMap<>();
        for (String token : filteredComment) {
            token = token.toLowerCase();
            phraseFrequency.put(token, phraseFrequency.getOrDefault(token, 0) + 1);
        }
        return phraseFrequency;
    }

    private String extractKeyPhrase(Map<String, Integer> phraseFrequency, int topN) {
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(phraseFrequency.entrySet());
        entries.sort((a, b) -> b.getValue() - a.getValue());
        StringBuilder keyPhrase = new StringBuilder();
        for (int i = 0; i < topN && i < entries.size(); i++) {
            if (i > 0) {
                keyPhrase.append(", ");
            }
            keyPhrase.append(entries.get(i).getKey());
        }
        return keyPhrase.toString();
    }
}