package org.cafeteria.client.handlers;

import org.cafeteria.common.customException.CustomExceptions;
import org.cafeteria.common.model.User;
import java.io.IOException;
import java.util.Scanner;

public abstract class UserManager {
    protected User user;
    protected static Scanner sc;

    public UserManager(User user, Scanner sc) {
        this.user = user;
        UserManager.sc = sc;
    }

    public abstract void showUserActionItems() throws IOException, CustomExceptions.InvalidResponseException, CustomExceptions.BadResponseException;
}