CREATE TABLE UserSession (
    id INT AUTO_INCREMENT PRIMARY KEY,
    userId INT NOT NULL,
    loginDateTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    logoutDateTime TIMESTAMP NULL,
    FOREIGN KEY (userId) REFERENCES User(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE UserActivity (
    activityId INT AUTO_INCREMENT PRIMARY KEY,
    sessionId INT NOT NULL,
    activityDescription VARCHAR(255) NOT NULL,
    activityTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sessionId) REFERENCES UserSession(id) ON DELETE CASCADE ON UPDATE CASCADE
);