ALTER TABLE dailyRecommendation
DROP FOREIGN KEY dailyrecommendation_ibfk_1;

ALTER TABLE dailyRecommendation
DROP FOREIGN KEY dailyrecommendation_ibfk_2;

ALTER TABLE DailyRecommendation
DROP COLUMN mealTypeId;

ALTER TABLE DailyRecommendation
ADD CONSTRAINT fk_menuItemId
FOREIGN KEY (menuItemId) REFERENCES Menu(Id)
ON DELETE CASCADE;
