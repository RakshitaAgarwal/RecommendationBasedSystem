ALTER TABLE MealType
ADD CONSTRAINT unique_menuItem_name UNIQUE (mealType);

ALTER TABLE UserRole
ADD CONSTRAINT unique_menuItem_name UNIQUE (userRole);

ALTER TABLE NotificationType
ADD CONSTRAINT unique_menuItem_name UNIQUE (notificationType);