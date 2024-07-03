package org.cafeteria.common.model;

public class FeedbackAnswers {
    private int id;
    private int detailedFeedbackId;
    private int detailedFeedbackQuestionId;
    private String answer;

    public FeedbackAnswers() {
    }

    public FeedbackAnswers(int id, int detailedFeedbackId, int detailedFeedbackQuestionId, String answer) {
        this.id = id;
        this.detailedFeedbackId = detailedFeedbackId;
        this.detailedFeedbackQuestionId = detailedFeedbackQuestionId;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDetailedFeedbackId() {
        return detailedFeedbackId;
    }

    public void setDetailedFeedbackId(int detailedFeedbackId) {
        this.detailedFeedbackId = detailedFeedbackId;
    }

    public int getDetailedFeedbackQuestionId() {
        return detailedFeedbackQuestionId;
    }

    public void setDetailedFeedbackQuestionId(int detailedFeedbackQuestionId) {
        this.detailedFeedbackQuestionId = detailedFeedbackQuestionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
