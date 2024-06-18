INSERT INTO UserRole (userRole)
VALUES ('ADMIN'), ('CHEF'), ('EMP');

INSERT INTO MealType (mealType)
VALUES ('LUNCH'), ('BREAKFAST'), ('DINNER');

INSERT INTO NotificationType (notificationType, expirationTime, priority)
VALUES ('ADD MENU ITEM', null, 0), ('DELETE MENU ITEM', null, 0), ('UPDATE MENU ITEM', null, 0), ('NEXT DAY OPTIONS', 24, 1), ('FINAL NEXT DAY MENU', 24, 1);