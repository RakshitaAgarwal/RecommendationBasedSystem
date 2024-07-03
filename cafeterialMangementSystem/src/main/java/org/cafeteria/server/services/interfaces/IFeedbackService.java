package org.cafeteria.server.services.interfaces;

import org.cafeteria.common.model.Feedback;

import java.sql.SQLException;
import java.util.List;

public interface IFeedbackService extends IValidationService<Feedback>, ICrudService<Feedback> {
    public void getFeedbackReport();
    public void generateFeedbackReport();

    public List<Feedback> getFeedbackByMenuItemId(int menuItemId) throws SQLException;
}
