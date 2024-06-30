CREATE TABLE DiscardMenuItem (
    Id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    menuItemId INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    price FLOAT NOT NULL,
    mealTypeId INT NOT NULL,
    FOREIGN KEY (menuItemId) REFERENCES Menu(Id),
    FOREIGN KEY (mealTypeId) REFERENCES MealType(Id)
);