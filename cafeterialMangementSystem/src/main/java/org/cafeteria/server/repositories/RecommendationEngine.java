package org.cafeteria.server.repositories;

import org.cafeteria.common.model.Feedback;
import org.cafeteria.common.model.MenuItemRecommendation;
import org.cafeteria.server.model.SentimentResult;
import org.cafeteria.server.services.interfaces.IFeedbackService;

import java.sql.SQLException;
import java.util.*;

public class RecommendationEngine {

    private final IFeedbackService _feedbackService;

    public RecommendationEngine(IFeedbackService feedbackService) {
        _feedbackService = feedbackService;
    }

    public List<MenuItemRecommendation> getTopRecommendedItems(List<Integer> filteredMenuItemIds, int topX) throws SQLException {
        List<MenuItemRecommendation> recommendedItems = getRecommendationsOfMenuItems(filteredMenuItemIds);
        for (MenuItemRecommendation item : recommendedItems) {
            System.out.println(item.getMenuItemId() + " " + item.getRecommendationScore());
        }
        Collections.sort(recommendedItems, Comparator.comparingDouble(MenuItemRecommendation::getRecommendationScore).reversed());
        System.out.println("\n after sorting");
        for (MenuItemRecommendation item : recommendedItems) {
            System.out.println(item.getMenuItemId() + " " + item.getRecommendationScore());
        }

        List<MenuItemRecommendation> topElements = getTopItems(recommendedItems, topX);

        for (MenuItemRecommendation menuItemRecommendation : topElements) {
            System.out.println(menuItemRecommendation);
        }
        return topElements;
    }

    public List<MenuItemRecommendation> getRecommendationsOfMenuItems(List<Integer> menuItemIds) throws SQLException {
        List<MenuItemRecommendation> menuItemSentiments = new ArrayList<>();
        for (int menuItemId : menuItemIds) {
            MenuItemRecommendation menuItemRecommendation = evaluateMenuItemRecommendation(menuItemId);
            menuItemSentiments.add(menuItemRecommendation);
        }
        return menuItemSentiments;
    }

    public MenuItemRecommendation evaluateMenuItemRecommendation(int menuItemId) throws SQLException {
        List<Feedback> menuItemFeedbacks = _feedbackService.getFeedbackByMenuItem(menuItemId);
        SentimentResult sentimentResult = calculateAverageSentimentOfMenuItem(menuItemFeedbacks);

        double averageRating = calculateAverageRatingOfMenuItem(menuItemFeedbacks);
        double averageSentiment = sentimentResult.getPositiveSentimentScore() + (sentimentResult.getNeutralSentimentScore() * 0.5) - sentimentResult.getNegativeSentimentScore();
        double recommendationScore = calculateRecommendationScore(averageRating, averageSentiment);
        String sentimentOfMenuItem = sentimentResult.getSentiment() + " " + sentimentResult.getKeyPhrase();

        return new MenuItemRecommendation(menuItemId, recommendationScore, sentimentOfMenuItem);
    }

    private double calculateAverageRatingOfMenuItem(List<Feedback> menuItemFeedbacks) {
        return menuItemFeedbacks.stream()
                .mapToDouble(Feedback::getRating)
                .average()
                .orElse(0.0);
    }

    private SentimentResult calculateAverageSentimentOfMenuItem(List<Feedback> menuItemFeedbacks) {
        SentimentAnalysis sentimentAnalysis = new SentimentAnalysis();
        List<String> commentsList = new ArrayList<>();

        for (Feedback feedback : menuItemFeedbacks) {
            String comment = feedback.getComment();
            if (comment != null && !comment.isEmpty()) {
                commentsList.add(comment);
            }
        }
        return sentimentAnalysis.calculateAverageSentimentAnalysis(commentsList);
    }

    private double calculateRecommendationScore(double avgRating, double avgSentiment) {
        double ratingWeight = 0.3;
        double sentimentWeight = 0.7;
        double normalizedRating = avgRating / 5.0;
        return (ratingWeight * normalizedRating) + (sentimentWeight * avgSentiment);
    }

    private List<MenuItemRecommendation> getTopItems(List<MenuItemRecommendation> menuItemRecommendations, int noOfTopRecommendationRequired) {
        if (noOfTopRecommendationRequired > menuItemRecommendations.size()) {
            noOfTopRecommendationRequired = menuItemRecommendations.size();
        }
        return new ArrayList<>(menuItemRecommendations.subList(0, noOfTopRecommendationRequired));
    }
}