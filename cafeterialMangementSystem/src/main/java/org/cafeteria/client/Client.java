package org.cafeteria.client;

import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.client.services.AdminService;
import org.cafeteria.client.services.AuthenticationService;
import org.cafeteria.client.services.ChefService;
import org.cafeteria.client.services.EmployeeService;
import org.cafeteria.common.customException.CustomExceptions;
import org.cafeteria.common.model.User;

import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

import static org.cafeteria.common.constants.Constants.SERVER_ADDRESS;
import static org.cafeteria.common.constants.Constants.SERVER_PORT;

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
                System.out.println("Please Login to proceed.");
                User userToLogin = fetchUserCredentialsForLogin();
                try {
                    User user = new AuthenticationService(connection).login(userToLogin);
                    showUserActionItems(user);
                } catch (CustomExceptions.LoginFailedException e) {
                    System.out.println("Login Failed");
                } finally {
                    System.out.println("Do you want to try again? If yes please enter 1.");
                    choice = sc.nextInt();
                }
            } while (choice == 1);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources();
        }
    }

    private static void showUserActionItems(User user) {
        try {
            switch (user.getUserRole()) {
                case ADMIN -> {
                    AdminService adminService = new AdminService(connection, user, sc);
                    adminService.showUserActionItems();
                }
                case CHEF -> {
                    ChefService chef = new ChefService(connection, user, sc);
                    chef.showUserActionItems();
                }
                case EMP -> {
                    EmployeeService employee = new EmployeeService(connection, user, sc);
                    employee.showUserActionItems();
                }
                default -> {
                    System.out.println("Some Error Occurred");
                    connection.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    private static void createConnection() throws IOException {
        connection = ServerConnection.getInstance(SERVER_ADDRESS, SERVER_PORT);
    }

    private static User fetchUserCredentialsForLogin() {
        System.out.println("Please Enter your Employee ID:");
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