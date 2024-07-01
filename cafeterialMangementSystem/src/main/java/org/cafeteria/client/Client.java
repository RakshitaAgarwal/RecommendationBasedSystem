package org.cafeteria.client;

import org.cafeteria.client.global.GlobalData;
import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.client.handlers.AdminHandler;
import org.cafeteria.client.handlers.AuthenticationHandler;
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
import java.util.Scanner;

public class Client {
    private static final Scanner sc = new Scanner(System.in);
    private static ServerConnection connection;
    private static final String SERVER_PROPERTIES_FILE = "server.properties";

    public static void main(String[] args) {
        try {
            setUpApplication();
            System.out.println("Welcome to Cafeteria Management System");

            int choice;
            do {
                choice = 0;
                System.out.println("Please Login to proceed.");
                User userToLogin = fetchUserCredentialsForLogin();
                try {
                    User user = new AuthenticationHandler(connection).login(userToLogin);
                    updateGlobalData(user);
                    showUserActionItems(user);
                } catch (LoginFailedException e) {
                    System.out.println("Login Failed");
                    System.out.println("Do you want to try again? If yes please enter 1.");
                    choice = sc.nextInt();
                }
            } while (choice == 1);

        } catch (IOException e) {
            System.out.println("Server Got Disconnected");
        } finally {
            closeResources();
        }
    }

    private static void updateGlobalData(User user) {
        GlobalData.loggedInUser = user;
    }

    private static void showUserActionItems(User user) throws IOException {
        UserRoleEnum userRole = getEnumFromOrdinal(UserRoleEnum.class, user.getUserRoleId());
        switch (userRole) {
            case ADMIN -> {
                AdminHandler adminHandler = new AdminHandler(connection, user, sc);
                adminHandler.showUserActionItems();
            }
            case CHEF -> {
                ChefHandler chef = new ChefHandler(connection, user, sc);
                chef.showUserActionItems();
            }
            case EMP -> {
                EmployeeHandler employee = new EmployeeHandler(connection, user, sc);
                employee.showUserActionItems();
            }
            default -> {
                System.out.println("Some Error Occurred");
                connection.close();
            }
        }
    }

    private static void setUpApplication() throws IOException {
        initProperties();
        createConnection();
    }

    private static void initProperties() throws IOException {
        Properties connectionProperties = new Properties();
        connectionProperties.load(Client.class.getClassLoader().getResourceAsStream(SERVER_PROPERTIES_FILE));
        SERVER_PORT = Integer.parseInt(connectionProperties.getProperty("server.port"));
        SERVER_ADDRESS = connectionProperties.getProperty("server.address");
    }

    private static void createConnection() {
        connection = ServerConnection.getInstance(SERVER_ADDRESS, SERVER_PORT);
        System.out.println("Server Got Connected");
    }

    private static User fetchUserCredentialsForLogin() {
        System.out.println("Enter your Employee ID:");
        int userId = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter your Username: ");
        String username = sc.nextLine();
        return new User(userId, username);
    }

    private static void closeResources() {
        sc.close();
    }
}