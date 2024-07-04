package org.cafeteria.server.repositories.interfaces;

import org.cafeteria.common.model.DetailedFeedbackRequest;
import org.cafeteria.server.services.interfaces.ICrudService;

import java.sql.SQLException;

public interface IDetailedFeedbackRequestRepository extends ICrudService<DetailedFeedbackRequest> {
    DetailedFeedbackRequest getByMenuItemId(int menuItemId) throws SQLException;
}
