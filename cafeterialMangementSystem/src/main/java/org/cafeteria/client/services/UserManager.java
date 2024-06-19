package org.cafeteria.client.services;

import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.common.model.User;

import java.io.IOException;
import java.util.Scanner;

public abstract class UserManager {
    protected static ServerConnection connection;
    protected User user;
    protected static Scanner sc;

    public UserManager(ServerConnection connection, User user, Scanner sc) {
        UserManager.connection = connection;
        this.user = user;
        UserManager.sc = sc;
    }
    public abstract void displayMenuFromServer() throws IOException;

    public abstract void showUserActionItems() throws IOException;
}