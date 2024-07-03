CREATE TABLE FeedbackQuestion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question VARCHAR(255) NOT NULL
);

CREATE TABLE FeedbackAnswer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    detailedFeedbackId INT NOT NULL,
    feedbackQuestionId INT NOT NULL,
    answer VARCHAR(255) NOT NULL,
    FOREIGN KEY (detailedFeedbackId) REFERENCES DetailedFeedback(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (feedbackQuestionId) REFERENCES FeedbackQuestion(id) ON DELETE CASCADE ON UPDATE CASCADE
);

