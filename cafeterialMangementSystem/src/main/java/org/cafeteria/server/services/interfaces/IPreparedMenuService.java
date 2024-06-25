package org.cafeteria.server.services.interfaces;

import org.cafeteria.common.model.PreparedMenu;

public interface IPreparedMenuService extends IValidationService<PreparedMenu>, ICrudService<PreparedMenu> {
    public void updateDailyMenu();
    public void weeklyMenuCleanUp();
}
