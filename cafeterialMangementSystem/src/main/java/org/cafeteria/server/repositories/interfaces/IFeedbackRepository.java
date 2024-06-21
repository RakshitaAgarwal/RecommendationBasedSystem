package org.cafeteria.server.repositories.interfaces;


import org.cafeteria.common.model.Feedback;

import java.sql.SQLException;
import java.util.List;

public interface IFeedbackRepository extends ICrudRepository<Feedback> {
    public List<Feedback> getFeedbacksByMenuItem(int menuItemId) throws SQLException;
}
