package org.cafeteria.common.model;

import java.util.Date;

public class DetailedFeedback {
    private int id;
    private int userId;
    private int menuItemId;
    private int feedbackQuestionId;
    private String answer;
    private Date dateTime;

    public DetailedFeedback() {
    }

    public DetailedFeedback(int userId, int menuItemId, int feedbackQuestionId, String answer, Date dateTime) {
        this.userId = userId;
        this.menuItemId = menuItemId;
        this.feedbackQuestionId = feedbackQuestionId;
        this.answer = answer;
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getFeedbackQuestionId() {
        return feedbackQuestionId;
    }

    public void setFeedbackQuestionId(int feedbackQuestionId) {
        this.feedbackQuestionId = feedbackQuestionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
}
