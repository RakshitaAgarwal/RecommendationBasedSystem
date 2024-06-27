CREATE TABLE MenuItemUserVote (
    id INT AUTO_INCREMENT PRIMARY KEY,
    menuItemId INT NOT NULL,
    userId INT NOT NULL,
    FOREIGN KEY (menuItemId) REFERENCES Menu(id),
    FOREIGN KEY (userId) REFERENCES User(id)
);