
ALTER TABLE MenuItem
ADD CONSTRAINT FK_MenuItem_MenuItemType FOREIGN KEY (menuItemTypeId) REFERENCES MenuItemType(id)
ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT FK_MenuItem_SweetContentLevel FOREIGN KEY (sweetContentLevelId) REFERENCES ContentLevel(id)
ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT FK_MenuItem_SpiceContentLevel FOREIGN KEY (spiceContentLevelId) REFERENCES ContentLevel(id)
ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT FK_MenuItem_CuisineType FOREIGN KEY (cuisineTypeId) REFERENCES CuisineType(id)
ON DELETE CASCADE ON UPDATE CASCADE;