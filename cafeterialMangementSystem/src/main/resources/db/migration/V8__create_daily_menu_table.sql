-- Drop the DailyMenu table if it exists (optional)
DROP TABLE IF EXISTS DailyMenu;

-- Create the DailyMenu table
CREATE TABLE DailyMenu (
    Id Int AUTO_INCREMENT PRIMARY KEY NOT NULL,
    menuItemId Int NOT NULL,
    mealTypeId Int NOT NULL,
    dateTime DATE NOT NULL,
    FOREIGN KEY (menuItemId) REFERENCES Menu(Id),
    FOREIGN KEY (mealTypeId) REFERENCES MealType(Id)
);