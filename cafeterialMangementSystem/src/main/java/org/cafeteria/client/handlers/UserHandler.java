package org.cafeteria.client.handlers;

import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.client.repositories.AuthenticationRepository;
import org.cafeteria.common.customException.CustomExceptions.*;
import org.cafeteria.common.model.User;

import java.io.IOException;

import static org.cafeteria.client.consoleManager.UserConsoleManager.displayMessage;

public abstract class UserHandler {
    protected User user;
    private static AuthenticationRepository authenticationRepository;

    public UserHandler(User user, ServerConnection connection) {
        this.user = user;
        authenticationRepository = new AuthenticationRepository(connection);
    }

    public abstract boolean showUserActionItems() throws IOException, InvalidResponseException, BadResponseException;

    protected boolean handleLogout() throws IOException {
        try {
            String response = authenticationRepository.logout(user);
            displayMessage(response);
            return true;
        } catch (BadResponseException e) {
            displayMessage(e.getMessage());
        }
        return false;
    }
}