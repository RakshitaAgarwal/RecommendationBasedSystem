package org.cafeteria.server.services;

import org.cafeteria.common.model.Feedback;
import org.cafeteria.server.repositories.FeedbackRepository;
import org.cafeteria.server.repositories.interfaces.IFeedbackRepository;
import org.cafeteria.server.services.interfaces.IFeedbackService;

import java.sql.SQLException;
import java.util.List;

public class FeedbackService implements IFeedbackService {

    private static IFeedbackRepository _feedbackRepository;

    public FeedbackService() {
        _feedbackRepository = new FeedbackRepository();
    }
    @Override
    public boolean validate(Feedback item) {
        return false;
    }

    @Override
    public boolean add(Feedback object) throws SQLException {
        return _feedbackRepository.add(object);
    }

    @Override
    public boolean update(Feedback object) {

        return false;
    }

    @Override
    public boolean delete(Feedback object) {

        return false;
    }

    @Override
    public List<Feedback> getAll() {

        return null;
    }

    @Override
    public Feedback getById(int id) {
        return null;
    }

    @Override
    public void getFeedbackReport() {

    }

    @Override
    public void generateFeedbackReport() {

    }

    @Override
    public List<Feedback> getFeedbackByMenuItem(int menuItemId) throws SQLException {
        return _feedbackRepository.getFeedbacksByMenuItem(menuItemId);
    }
}
