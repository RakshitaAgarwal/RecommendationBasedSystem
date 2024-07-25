ALTER TABLE dailyRecommendation RENAME TO rolledOutMenuItem;

ALTER TABLE rolledOutMenuItem DROP COLUMN votes;