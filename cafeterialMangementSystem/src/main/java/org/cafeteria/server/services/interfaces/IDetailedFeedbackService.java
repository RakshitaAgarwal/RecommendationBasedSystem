package org.cafeteria.server.services.interfaces;

import org.cafeteria.common.customException.CustomExceptions;

import java.sql.SQLException;

public interface IDetailedFeedbackService {
    boolean addDetailedFeedbackRequest(int menuItemId) throws SQLException, CustomExceptions.DuplicateEntryFoundException;
}
