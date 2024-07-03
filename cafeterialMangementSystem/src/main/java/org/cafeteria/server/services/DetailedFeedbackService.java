package org.cafeteria.server.services;

import org.cafeteria.common.customException.CustomExceptions.DuplicateEntryFoundException;
import org.cafeteria.common.model.DetailedFeedbackRequest;
import org.cafeteria.server.repositories.DetailedFeedbackRequestRepository;
import org.cafeteria.server.repositories.interfaces.IDetailedFeedbackRequestRepository;
import org.cafeteria.server.services.interfaces.IDetailedFeedbackService;

import java.sql.SQLException;
import java.util.Date;

public class DetailedFeedbackService implements IDetailedFeedbackService {
    private static IDetailedFeedbackRequestRepository _detailedFeedbackRequestRepository;
    public DetailedFeedbackService() {
        _detailedFeedbackRequestRepository = new DetailedFeedbackRequestRepository();
    }

    @Override
    public boolean addDetailedFeedbackRequest(int menuItemId) throws SQLException, DuplicateEntryFoundException {
        DetailedFeedbackRequest feedbackRequest = new DetailedFeedbackRequest(menuItemId, new Date());
        return _detailedFeedbackRequestRepository.add(feedbackRequest);
    }
}
