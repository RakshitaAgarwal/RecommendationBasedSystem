ALTER TABLE Menu
ADD CONSTRAINT unique_Menu_menuItemName UNIQUE (name);

ALTER TABLE DiscardMenuItem
ADD CONSTRAINT unique_DiscardMenuItem_menuItemId UNIQUE (menuItemId);