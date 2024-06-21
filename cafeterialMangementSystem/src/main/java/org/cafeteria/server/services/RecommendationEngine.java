package org.cafeteria.server.services;

import org.cafeteria.common.model.Feedback;
import org.cafeteria.common.model.MenuItem;
import org.cafeteria.server.model.MenuItemScore;
import org.cafeteria.server.services.interfaces.IFeedbackService;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class RecommendationEngine {

    private final IFeedbackService _feedbackService;
    private List<MenuItemScore> recommendedItems = new ArrayList<>();

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

        List<MenuItemScore> topElements = getTopElements(recommendedItems, 3);

        for (MenuItemScore menuItemScore : topElements) {
            System.out.println(menuItemScore);
        }
        return topElements;
    }

    public static List<MenuItemScore> getTopElements(List<MenuItemScore> list, int x) {
        // Handle cases where x is larger than the list size
        if (x > list.size()) {
            x = list.size();
        }

        // Return the sublist of the first x elements
        return new ArrayList<>(list.subList(0, x));
    }

    private void updateRecommendedItems(List<MenuItem> menuItems) throws SQLException {
        recommendedItems.clear();
        List<MenuItem> filteredMenuItems = filterMenuItemByLastPrepared(menuItems);

        for (MenuItem menuItem : filteredMenuItems) {
            List<Feedback> menuItemFeedbacks = _feedbackService.getFeedbackByMenuItem(menuItem.getId());
            double averageRating = calculateAverageRatingOfMenuItem(menuItemFeedbacks);
            double averageSentiment = calculateAverageSentimentOfMenuItem(menuItemFeedbacks);
            double recommendationScore = calculateRecommendationScore(averageRating, averageSentiment);

            MenuItemScore menuItemScore = new MenuItemScore(menuItem.getId(), recommendationScore);
            recommendedItems.add(menuItemScore);
        }
    }

    private List<MenuItem> filterMenuItemByLastPrepared(List<MenuItem> menuItems) {
        Date dateOfOneWeekAgo = new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000L);

        List<MenuItem> filteredMenuItems = menuItems.stream()
                .filter(item -> item.getLastTimePrepared() == null || item.getLastTimePrepared().before(dateOfOneWeekAgo))
                .collect(Collectors.toList());

        return filteredMenuItems;
    }

    private double calculateAverageRatingOfMenuItem(List<Feedback> menuItemFeedbacks) {
        double averageRating = menuItemFeedbacks.stream()
                .mapToDouble(Feedback::getRating)
                .average()
                .orElse(0.0);
        return averageRating;
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
//        DocumentSentiment documentSentiment = sentimentAnalysis.calculateAverageSentimentAnalysis(commentsList);
//        return documentSentiment.getConfidenceScores().getPositive();
        return Math.random();
    }

    private double calculateRecommendationScore(double avgRating, double avgSentiment) {
        double ratingWeight = 0.3;
        double sentimentWeight = 0.7;
        double normalizedRating = avgRating / 5.0;
        double normalizedSentiment = avgSentiment;
        double recommendationScore = (ratingWeight * normalizedRating) + (sentimentWeight * normalizedSentiment);
        return recommendationScore;
    }
}