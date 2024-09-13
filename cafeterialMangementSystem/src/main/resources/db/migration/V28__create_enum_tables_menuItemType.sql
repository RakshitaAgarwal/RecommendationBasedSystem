CREATE TABLE ContentLevel (
    Id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    contentLevel VARCHAR(255) NOT NULL
);

INSERT INTO ContentLevel (contentLevel)
VALUES ('LOW'), ('MEDIUM'), ('HIGH');

CREATE TABLE CuisineType (
    Id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    cuisineType VARCHAR(255) NOT NULL
);

INSERT INTO CuisineType (CuisineType)
VALUES ('NORTH INDIAN'), ('SOUTH INDIAN'), ('AMERICAN'), ('ITALIAN'), ('OTHER');

CREATE TABLE MenuItemtype (
    Id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    menuItemType VARCHAR(255) NOT NULL
);

INSERT INTO MenuItemType (menuItemtype)
VALUES ('VEG'), ('NON VEG'), ('EGG BASED');