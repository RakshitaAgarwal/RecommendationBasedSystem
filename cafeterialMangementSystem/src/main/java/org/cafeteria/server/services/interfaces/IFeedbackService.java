package org.cafeteria.server.services.interfaces;

import org.cafeteria.common.model.Feedback;

public interface IFeedbackService extends IValidationService<Feedback>, ICrudService<Feedback> {
    public void getFeedbackReport();
    public void generateFeedbackReport();
}
