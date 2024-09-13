DROP TABLE IF EXISTS Notification;

CREATE TABLE Notification (
    Id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    userId INT NOT NULL,
    notificationTypeId Int NOT NULL,
    notificationMessage VARCHAr(255) NOT NULL,
    dateTime DATE NOT NULL,
    isNotificationRead BOOLEAN NOT NULL,
    FOREIGN KEY (notificationTypeId) REFERENCES NotificationType(Id),
    FOREIGN KEY (userId) REFERENCES User(Id)
);