package org.cafeteria.server.services;

import org.cafeteria.common.model.Feedback;
import org.cafeteria.server.services.interfaces.IFeedbackService;

import java.util.List;

public class FeedbackService implements IFeedbackService {
    @Override
    public boolean validate(Feedback item) {
        return false;
    }

    @Override
    public boolean add(Feedback object) {

        return false;
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
    public void getById(Feedback object) {

    }

    @Override
    public void getFeedbackReport() {

    }

    @Override
    public void generateFeedbackReport() {

    }
}
