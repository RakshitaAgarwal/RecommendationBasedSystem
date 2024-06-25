DROP TABLE IF EXISTS DailyMenu;

-- Create the DailyMenu table with the new configuration
CREATE TABLE PreparedMenu (
    Id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    menuItemId INT NOT NULL,
    date DATE NOT NULL,
    FOREIGN KEY (menuItemId) REFERENCES Menu(Id)
);