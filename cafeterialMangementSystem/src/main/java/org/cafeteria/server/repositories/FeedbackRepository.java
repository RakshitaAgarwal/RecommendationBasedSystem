package org.cafeteria.server.repositories;

import org.cafeteria.common.model.Feedback;
import org.cafeteria.server.repositories.interfaces.IFeedbackRepository;

import java.util.List;

public class FeedbackRepository implements IFeedbackRepository {
    @Override
    public boolean add(Feedback item) {

        return false;
    }

    @Override
    public void delete(Feedback item) {

    }

    @Override
    public boolean update(Feedback item) {

        return false;
    }

    @Override
    public List<Feedback> GetAll() {
        return null;
    }

    @Override
    public Feedback getById(int id) {
        return null;
    }
}