package org.cafeteria.client;

import org.cafeteria.client.consoleManager.UserConsoleManager;
import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.client.handlers.AdminHandler;
import org.cafeteria.client.repositories.AuthenticationRepository;
import org.cafeteria.client.handlers.ChefHandler;
import org.cafeteria.client.handlers.EmployeeHandler;
import org.cafeteria.common.customException.CustomExceptions.*;
import org.cafeteria.common.model.User;
import org.cafeteria.common.model.enums.UserRoleEnum;

import static org.cafeteria.common.constants.Constants.SERVER_ADDRESS;
import static org.cafeteria.common.constants.Constants.SERVER_PORT;
import static org.cafeteria.common.util.Utils.getEnumFromOrdinal;

import java.io.IOException;
import java.util.Properties;

public class Client extends UserConsoleManager {
    private static ServerConnection connection;
    private static final String SERVER_PROPERTIES_FILE = "server.properties";

    public static void main(String[] args) {
        try {
            if (setUpApplication()) {
                displayMessage("Welcome to Cafeteria Management System");

                int choice;
                do {
                    displayMessage("Please Login to proceed.");
                    User userToLogin = fetchUserCredentialsForLogin();
                    try {
                        User user = new AuthenticationRepository(connection).login(userToLogin);
                        showUserActionItems(user);
                        choice = 0;
                    } catch (LoginFailedException e) {
                        displayMessage("Login Failed");
                        choice = takeUserIntInput("Do you want to try again? If yes please enter 1.");
                    }
                } while (choice == 1);
            }

        } catch (IOException e) {
            displayMessage("Server Got Disconnected. " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    private static void showUserActionItems(User user) throws IOException {
        UserRoleEnum userRole = getEnumFromOrdinal(UserRoleEnum.class, user.getUserRoleId());
        switch (userRole) {
            case ADMIN -> {
                AdminHandler adminHandler = new AdminHandler(connection, user);
                adminHandler.showUserActionItems();
            }
            case CHEF -> {
                ChefHandler chef = new ChefHandler(connection, user);
                chef.showUserActionItems();
            }
            case EMP -> {
                EmployeeHandler employee = new EmployeeHandler(connection, user);
                employee.showUserActionItems();
            }
            default -> {
                displayMessage("Some Error Occurred");
                connection.close();
            }
        }
    }

    private static boolean setUpApplication() throws IOException {
        initProperties();
        return createConnection();
    }

    private static void initProperties() throws IOException {
        Properties connectionProperties = new Properties();
        connectionProperties.load(Client.class.getClassLoader().getResourceAsStream(SERVER_PROPERTIES_FILE));
        SERVER_PORT = Integer.parseInt(connectionProperties.getProperty("server.port"));
        SERVER_ADDRESS = connectionProperties.getProperty("server.address");
    }

    private static boolean createConnection() {
        try {
            connection = ServerConnection.getInstance(SERVER_ADDRESS, SERVER_PORT);
            displayMessage("Server Got Connected");
            return true;
        } catch (IOException e) {
            displayMessage("Server is not connected. " + e.getMessage());
        }
        return false;
    }

    private static User fetchUserCredentialsForLogin() {
        int userId = takeUserIntInput("Enter your Employee ID:");
        String username = takeUserStringInput("Enter your Username: ");
        return new User(userId, username);
    }

    private static void closeResources() {
        sc.close();
    }
}