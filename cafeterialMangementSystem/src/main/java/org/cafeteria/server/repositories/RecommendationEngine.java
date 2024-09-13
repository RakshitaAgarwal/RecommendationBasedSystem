package org.cafeteria.server.repositories;

import org.cafeteria.common.model.Feedback;
import org.cafeteria.common.model.MenuItem;
import org.cafeteria.common.model.MenuItemRecommendation;
import org.cafeteria.server.model.SentimentResult;
import org.cafeteria.server.services.interfaces.IFeedbackService;
import org.cafeteria.server.services.interfaces.IMenuService;

import java.sql.SQLException;
import java.util.*;

public class RecommendationEngine {

    private final IFeedbackService _feedbackService;
    private final IMenuService _menuService;

    public RecommendationEngine(IFeedbackService feedbackService, IMenuService menuService) {
        _feedbackService = feedbackService;
        _menuService = menuService;
    }

    public List<MenuItemRecommendation> getTopRecommendedItems(List<Integer> filteredMenuItemIds, int topX) throws SQLException {
        List<MenuItemRecommendation> recommendedItems = getRecommendationsOfMenuItems(filteredMenuItemIds);
        recommendedItems.sort(Comparator.comparingDouble(MenuItemRecommendation::getRecommendationScore).reversed());
        List<MenuItemRecommendation> topElements = getTopItems(recommendedItems, topX);
        return topElements;
    }

    public List<MenuItemRecommendation> getRecommendationsOfMenuItems(List<Integer> menuItemIds) throws SQLException {
        List<MenuItemRecommendation> menuItemRecommendations = new ArrayList<>();
        for (int menuItemId : menuItemIds) {
            MenuItem menuItem = _menuService.getById(menuItemId);
            MenuItemRecommendation menuItemRecommendation = evaluateMenuItemRecommendation(menuItemId, menuItem.getName());
            menuItemRecommendations.add(menuItemRecommendation);
        }
        return menuItemRecommendations;
    }

    public MenuItemRecommendation evaluateMenuItemRecommendation(int menuItemId, String menuItemName) throws SQLException {
        List<Feedback> menuItemFeedbacks = _feedbackService.getFeedbackByMenuItemId(menuItemId);
        SentimentResult sentimentResult = calculateAverageSentimentOfMenuItem(menuItemFeedbacks, menuItemName);

        double averageRating = calculateAverageRatingOfMenuItem(menuItemFeedbacks);
        double averageSentimentScore = sentimentResult.positiveSentimentScore() + (sentimentResult.neutralSentimentScore() * 0.5) - sentimentResult.negativeSentimentScore();
        double recommendationScore = calculateRecommendationScore(averageRating, averageSentimentScore);
        String sentimentOfMenuItem = sentimentResult.keyPhrase() != null ? sentimentResult.keyPhrase() : sentimentResult.sentiment();

        return new MenuItemRecommendation(menuItemId, recommendationScore, sentimentOfMenuItem);
    }

    private double calculateAverageRatingOfMenuItem(List<Feedback> menuItemFeedbacks) {
        return menuItemFeedbacks.stream()
                .mapToDouble(Feedback::getRating)
                .average()
                .orElse(0.0);
    }

    private SentimentResult calculateAverageSentimentOfMenuItem(List<Feedback> menuItemFeedbacks, String menuItemName) {
        SentimentAnalysis sentimentAnalysis = new SentimentAnalysis();
        List<String> commentsList = new ArrayList<>();

        for (Feedback feedback : menuItemFeedbacks) {
            String comment = feedback.getComment();
            if (comment != null && !comment.isEmpty()) {
                commentsList.add(comment);
            }
        }
        return sentimentAnalysis.calculateAverageSentimentAnalysis(commentsList, menuItemName);
    }

    private double calculateRecommendationScore(double avgRating, double avgSentimentScore) {
        return ((20 * avgRating) + (avgSentimentScore))/2;
    }

    private List<MenuItemRecommendation> getTopItems(List<MenuItemRecommendation> menuItemRecommendations, int noOfTopRecommendationRequired) {
        if (noOfTopRecommendationRequired > menuItemRecommendations.size()) {
            noOfTopRecommendationRequired = menuItemRecommendations.size();
        }
        return new ArrayList<>(menuItemRecommendations.subList(0, noOfTopRecommendationRequired));
    }
}