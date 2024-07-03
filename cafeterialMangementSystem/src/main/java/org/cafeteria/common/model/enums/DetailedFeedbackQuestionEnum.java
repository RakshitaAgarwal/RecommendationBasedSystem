package org.cafeteria.common.model.enums;

public enum DetailedFeedbackQuestionEnum {
    QUES_1("What didn’t you like about the Food Item?"),
    QUES_2("How would you like the Food Item to taste?"),
    QUES_3("Share any additional comments or suggestions or your mom’s recipe.");

    private final String detailedFeedbackQuestion;

    DetailedFeedbackQuestionEnum(String question) {
        this.detailedFeedbackQuestion = question;
    }

    @Override
    public String toString() {
        return detailedFeedbackQuestion;
    }

    public static DetailedFeedbackQuestionEnum fromString(String question) {
        for (DetailedFeedbackQuestionEnum detailedFeedbackQuestion : DetailedFeedbackQuestionEnum.values()) {
            if (detailedFeedbackQuestion.detailedFeedbackQuestion.equalsIgnoreCase(question)) {
                return detailedFeedbackQuestion;
            }
        }
        throw new IllegalArgumentException("Unknown response code: " + question);
    }
}
