package org.cafeteria.server.services;

import org.cafeteria.common.model.Feedback;
import org.cafeteria.common.model.MenuItem;
import org.cafeteria.common.model.MenuItemScore;
import org.cafeteria.server.services.interfaces.IFeedbackService;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class RecommendationEngine {

    private final IFeedbackService _feedbackService;
    private final List<MenuItemScore> recommendedItems = new ArrayList<>();

    public RecommendationEngine(IFeedbackService feedbackService) {
        _feedbackService = feedbackService;
    }
    public List<MenuItemScore> getTopRecommendedItems(List<MenuItem> menuItems, int topX) throws SQLException {
        updateRecommendedItems(menuItems);
        for(MenuItemScore item:recommendedItems){
            System.out.println(item.getMenuItemId() + " " + item.getRecommendationScore());
        }
        Collections.sort(recommendedItems, Comparator.comparingDouble(MenuItemScore::getRecommendationScore).reversed());
        System.out.println("\n after sorting");
        for(MenuItemScore item:recommendedItems){
            System.out.println(item.getMenuItemId() + " " + item.getRecommendationScore());
        }

        List<MenuItemScore> topElements = getTopElements(recommendedItems, topX);

        for (MenuItemScore menuItemScore : topElements) {
            System.out.println(menuItemScore);
        }
        return topElements;
    }

    private static List<MenuItemScore> getTopElements(List<MenuItemScore> list, int noOfTopRecommendationRequired) {
        if (noOfTopRecommendationRequired > list.size()) {
            noOfTopRecommendationRequired = list.size();
        }
        return new ArrayList<>(list.subList(0, noOfTopRecommendationRequired));
    }

    private void updateRecommendedItems(List<MenuItem> menuItems) throws SQLException {
        recommendedItems.clear();
        List<MenuItem> filteredMenuItems = filterMenuItemByLastPrepared(menuItems);

        for (MenuItem menuItem : filteredMenuItems) {
            List<Feedback> menuItemFeedbacks = _feedbackService.getFeedbackByMenuItem(menuItem.getId());
            double averageRating = calculateAverageRatingOfMenuItem(menuItemFeedbacks);
            double averageSentiment = calculateAverageSentimentOfMenuItem(menuItemFeedbacks);
            double recommendationScore = calculateRecommendationScore(averageRating, averageSentiment);

            String sentimentOfMenuItem = evaluateSentimentOfMenuItem();

            MenuItemScore menuItemScore = new MenuItemScore(menuItem.getId(), menuItem.getName(), recommendationScore, sentimentOfMenuItem);
            recommendedItems.add(menuItemScore);
        }
    }

    private String evaluateSentimentOfMenuItem() {
        return "tasty";
    }

    private List<MenuItem> filterMenuItemByLastPrepared(List<MenuItem> menuItems) {
        Date dateOfOneWeekAgo = new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000L);

        return menuItems.stream()
                .filter(item -> item.getLastTimePrepared() == null || item.getLastTimePrepared().before(dateOfOneWeekAgo))
                .collect(Collectors.toList());
    }

    private double calculateAverageRatingOfMenuItem(List<Feedback> menuItemFeedbacks) {
        return menuItemFeedbacks.stream()
                .mapToDouble(Feedback::getRating)
                .average()
                .orElse(0.0);
    }

    private double calculateAverageSentimentOfMenuItem(List<Feedback> menuItemFeedbacks) {
        SentimentAnalysis sentimentAnalysis = new SentimentAnalysis();
        List<String> commentsList = new ArrayList<>();

        for (Feedback feedback : menuItemFeedbacks) {
            String comment = feedback.getComment();
            if (comment != null && !comment.isEmpty()) {
                commentsList.add(comment);
            }
        }
        return sentimentAnalysis.calculateAverageSentimentAnalysis(commentsList).getPositiveSentimentScore();
    }

    private double calculateRecommendationScore(double avgRating, double avgSentiment) {
        double ratingWeight = 0.3;
        double sentimentWeight = 0.7;
        double normalizedRating = avgRating / 5.0;
        double recommendationScore = (ratingWeight * normalizedRating) + (sentimentWeight * avgSentiment);
        return recommendationScore;
    }
}