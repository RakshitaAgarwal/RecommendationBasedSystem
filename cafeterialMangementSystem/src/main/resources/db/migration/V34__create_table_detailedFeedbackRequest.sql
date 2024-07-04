CREATE TABLE DetailedFeedbackRequest (
    id INT AUTO_INCREMENT PRIMARY KEY,
    menuItemId INT NOT NULL UNIQUE,
    dateTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (menuItemId) REFERENCES MenuItem(id) ON DELETE CASCADE ON UPDATE CASCADE
);