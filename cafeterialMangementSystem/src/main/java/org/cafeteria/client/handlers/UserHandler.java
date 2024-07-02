package org.cafeteria.client.handlers;

import org.cafeteria.common.customException.CustomExceptions.*;
import org.cafeteria.common.model.User;
import java.io.IOException;

public abstract class UserHandler {
    protected User user;

    public UserHandler(User user) {
        this.user = user;
    }

    public abstract void showUserActionItems() throws IOException, InvalidResponseException, BadResponseException;
}