package org.cafeteria.server.repositories.interfaces;

import org.cafeteria.common.model.DetailedFeedback;

import java.sql.SQLException;
import java.util.List;

public interface IDetailedFeedbackRepository {
    boolean addBatch(List<DetailedFeedback> answers) throws SQLException;
}
