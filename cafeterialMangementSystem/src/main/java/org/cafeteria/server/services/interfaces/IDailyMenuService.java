package org.cafeteria.server.services.interfaces;

import org.cafeteria.common.model.DailyMenu;

public interface IDailyMenuService extends IValidationService<DailyMenu>, ICrudService<DailyMenu> {
    public void updateDailyMenu();
    public void weeklyMenuCleanUp();
}
