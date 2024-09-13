CREATE TABLE Feedback (
    id VARCHAR(50) PRIMARY KEY,
    userId INT NOT NULL,
    menuItemId INT NOT NULL,
    rating FLOAT NOT NULL,
    comment VARCHAR(255) NOT NULL,
    dateTime TIMESTAMP NOT NULL,
    FOREIGN KEY (menuItemId) REFERENCES Menu(Id),
    FOREIGN KEY (userId) REFERENCES User(Id)
);