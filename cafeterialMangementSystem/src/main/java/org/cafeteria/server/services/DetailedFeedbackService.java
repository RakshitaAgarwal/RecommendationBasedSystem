package org.cafeteria.server.services;

import org.cafeteria.common.customException.CustomExceptions.DuplicateEntryFoundException;
import org.cafeteria.common.model.DetailedFeedbackRequest;
import org.cafeteria.common.model.DetailedFeedback;
import org.cafeteria.server.repositories.DetailedFeedbackRequestRepository;
import org.cafeteria.server.repositories.DetailedFeedbackRepository;
import org.cafeteria.server.repositories.interfaces.IDetailedFeedbackRequestRepository;
import org.cafeteria.server.repositories.interfaces.IDetailedFeedbackRepository;
import org.cafeteria.server.services.interfaces.IDetailedFeedbackService;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class DetailedFeedbackService implements IDetailedFeedbackService {
    private static IDetailedFeedbackRequestRepository _detailedFeedbackRequestRepository;
    private static IDetailedFeedbackRepository _detailedFeedbackRepository;
    public DetailedFeedbackService() {
        _detailedFeedbackRequestRepository = new DetailedFeedbackRequestRepository();
        _detailedFeedbackRepository = new DetailedFeedbackRepository();
    }

    @Override
    public boolean addDetailedFeedbackRequest(int menuItemId) throws SQLException, DuplicateEntryFoundException {
        if(_detailedFeedbackRequestRepository.getByMenuItemId(menuItemId) == null ) {
            DetailedFeedbackRequest feedbackRequest = new DetailedFeedbackRequest(menuItemId, new Date());
            return _detailedFeedbackRequestRepository.add(feedbackRequest);
        } else throw new DuplicateEntryFoundException("Detailed Feedback Request for " + menuItemId + " already exists.");
    }

    @Override
    public boolean addDetailedFeedbacks(List<DetailedFeedback> detailedFeedbacks) throws SQLException {
        return _detailedFeedbackRepository.addBatch(detailedFeedbacks);
    }

    @Override
    public List<DetailedFeedbackRequest> getDetailedFeedbackRequests() throws SQLException {
        return _detailedFeedbackRequestRepository.getAll();
    }


}