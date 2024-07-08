package org.cafeteria.server.model;

import java.util.Date;

public record UserActivity(
        int id,
        int sessionId,
        String activity,
        Date datetime
) {
}
