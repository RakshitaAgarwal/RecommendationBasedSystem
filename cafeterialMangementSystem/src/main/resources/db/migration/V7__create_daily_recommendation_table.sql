-- Drop the DailyRecommendation table if it exists (optional)
DROP TABLE IF EXISTS DailyRecommendation;

-- Create the DailyRecommendation table
CREATE TABLE DailyRecommendation (
    Id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    menuItemId INT NOT NULL,
    mealTypeId INT NOT NULL,
    votes INT NOT NULL,
    dateTime DATE NOT NULL,
    FOREIGN KEY (menuItemId) REFERENCES Menu(Id),
    FOREIGN KEY (mealTypeId) REFERENCES MealType(Id)
);