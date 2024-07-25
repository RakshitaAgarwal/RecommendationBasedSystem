CREATE TABLE FeedbackQuestion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question VARCHAR(255) NOT NULL
);

INSERT INTO FeedbackQuestion (question) VALUES
('What didn’t you like about the Food Item?'),
('How would you like the Food Item to taste?'),
('Share any additional comments or suggestions or your mom’s recipe.');