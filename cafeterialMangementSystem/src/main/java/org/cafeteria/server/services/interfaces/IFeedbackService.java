package org.cafeteria.server.services.interfaces;

import org.cafeteria.common.model.Feedback;
import java.sql.SQLException;
import java.util.List;

public interface IFeedbackService extends ICrudService<Feedback> {
    List<Feedback> getFeedbackByMenuItemId(int menuItemId) throws SQLException;
}