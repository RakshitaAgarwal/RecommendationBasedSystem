package org.cafeteria.server.services.interfaces;

import org.cafeteria.common.customException.CustomExceptions;
import org.cafeteria.common.model.DetailedFeedback;
import org.cafeteria.common.model.DetailedFeedbackRequest;

import java.sql.SQLException;
import java.util.List;

public interface IDetailedFeedbackService {
    boolean addDetailedFeedbackRequest(int menuItemId) throws SQLException, CustomExceptions.DuplicateEntryFoundException;

    boolean addDetailedFeedbacks(List<DetailedFeedback> detailedFeedbacks) throws SQLException;

    List<DetailedFeedbackRequest> getDetailedFeedbackRequests() throws SQLException;
}