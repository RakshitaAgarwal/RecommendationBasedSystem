-- Enable the event scheduler
SET GLOBAL event_scheduler = ON;

DROP PROCEDURE IF EXISTS DiscardLowRatingMenuItems;
-- Create the stored procedure
DELIMITER $$

CREATE PROCEDURE DiscardLowRatingMenuItems()
BEGIN
    INSERT INTO DiscardMenuItem (menuItemId, avgRating)
    SELECT menuItem.id, feedbackByMenuItemId.avgRating
    FROM Menu menuItem
    JOIN (
        SELECT menuItemId, AVG(rating) AS avgRating
        FROM Feedback
        GROUP BY menuItemId
        HAVING avgRating < 2
    ) feedbackByMenuItemId ON menuItem.id = feedbackByMenuItemId.menuItemId;
END $$

DELIMITER ;

-- Create the event to run the procedure monthly
CREATE EVENT IF NOT EXISTS DiscardLowRatingMenuItemsEvent
ON SCHEDULE
    EVERY 1 MONTH
    STARTS '2024-07-01 00:00:00'
DO
    CALL DiscardLowRatingMenuItems();