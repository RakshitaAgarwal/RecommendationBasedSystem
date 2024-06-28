package org.cafeteria.server;

import org.cafeteria.client.Client;
import org.cafeteria.server.controller.*;
import org.cafeteria.server.network.ClientConnection;
import org.cafeteria.server.network.JdbcConnection;

import java.io.IOException;
import java.util.Properties;

import static org.cafeteria.common.constants.Constants.*;

public class Server {
    public static UserController userController;
    public static MenuController menuController;
    public static FeedbackController feedbackController;
    public static RolledOutMenuItemController rolledOutMenuItemController;
    public static NotificationController notificationController;
    public static PreparedMenuItemController preparedMenuItemController;

    public static VotingController votingController;
    public static RecommendationController recommendationController;

    public static void main(String[] args) {
        initDatabaseProperties();
        makeDatabaseConnection();
        initServices();
        initServerProperties();
        makeClientConnection();
        closeResources();
    }

    private static void initServices() {
        userController = new UserController();
        menuController = new MenuController();
        feedbackController = new FeedbackController();
        rolledOutMenuItemController = new RolledOutMenuItemController();
        notificationController = new NotificationController();
        preparedMenuItemController = new PreparedMenuItemController();
        votingController = new VotingController();
        recommendationController = new RecommendationController();
    }


    private static void initDatabaseProperties() {
        Properties connectionProperties = new Properties();
        try {
            connectionProperties.load(Client.class.getClassLoader().getResourceAsStream("database.properties"));
        } catch (IOException e) {
            System.out.println("Some Error occurred while loading DB Properties.");
        } finally {
            DATABASE_URL = connectionProperties.getProperty("database.url");
            DATABASE_USER = connectionProperties.getProperty("database.username");
            DATABASE_PASSWORD = connectionProperties.getProperty("database.password");
            connectionProperties.clear();
        }
    }

    private static void initServerProperties() {
        Properties connectionProperties = new Properties();
        try {
            connectionProperties.load(Client.class.getClassLoader().getResourceAsStream("server.properties"));
        } catch (IOException e) {
            System.out.println("Some Error occurred while loading Server Properties.");
        } finally {
            SERVER_PORT = Integer.parseInt(connectionProperties.getProperty("server.port"));
            SERVER_ADDRESS = connectionProperties.getProperty("server.address");
            connectionProperties.clear();
        }
    }

    private static void makeDatabaseConnection() {
        JdbcConnection jdbcConnection = new JdbcConnection();
        jdbcConnection.connectToDatabase();
    }

    private static void makeClientConnection() {
        ClientConnection server = new ClientConnection();
        server.connectToClient(SERVER_PORT);
    }

    private static void closeResources() {
    }
}