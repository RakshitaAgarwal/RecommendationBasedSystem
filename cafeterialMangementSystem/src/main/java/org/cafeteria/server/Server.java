package org.cafeteria.server;

import org.cafeteria.client.Client;
import org.cafeteria.server.controller.*;
import org.cafeteria.server.network.ClientConnection;
import org.cafeteria.server.network.JdbcConnection;

import java.io.IOException;
import java.util.Properties;

public class Server {
    public static UserController userController;
    public static MenuController menuController;
    public static FeedbackController feedbackController;
    public static RolledOutMenuItemController rolledOutMenuItemController;
    public static NotificationController notificationController;
    public static PreparedMenuItemController preparedMenuItemController;
    public static DiscardMenuItemController discardMenuItemController;
    public static DetailedFeedbackController detailedFeedbackController;
    public static VotingController votingController;
    public static RecommendationController recommendationController;
    private static String databaseURL;
    private static String databaseUser;
    private static String databasePassword;
    private static int serverPort;

    public static void main(String[] args) {
        initDatabaseProperties();
        makeDatabaseConnection();
        initControllers();
        initServerProperties();
        makeClientConnection();
    }


    private static void initDatabaseProperties() {
        Properties connectionProperties = new Properties();
        try {
            connectionProperties.load(Client.class.getClassLoader().getResourceAsStream("database.properties"));
        } catch (IOException e) {
            System.out.println("Some Error occurred while loading DB Properties.");
        } finally {
            databaseURL = connectionProperties.getProperty("database.url");
            databaseUser = connectionProperties.getProperty("database.username");
            databasePassword = connectionProperties.getProperty("database.password");
            connectionProperties.clear();
        }
    }

    private static void makeDatabaseConnection() {
        JdbcConnection jdbcConnection = JdbcConnection.getInstance();
        jdbcConnection.connectToDatabase(databaseURL, databaseUser, databasePassword);
    }

    private static void initControllers() {
        userController = new UserController();
        menuController = new MenuController();
        feedbackController = new FeedbackController();
        rolledOutMenuItemController = new RolledOutMenuItemController();
        notificationController = new NotificationController();
        preparedMenuItemController = new PreparedMenuItemController();
        votingController = new VotingController();
        recommendationController = new RecommendationController();
        discardMenuItemController = new DiscardMenuItemController();
        detailedFeedbackController = new DetailedFeedbackController();
    }

    private static void initServerProperties() {
        Properties connectionProperties = new Properties();
        try {
            connectionProperties.load(Client.class.getClassLoader().getResourceAsStream("server.properties"));
        } catch (IOException e) {
            System.out.println("Some Error occurred while loading Server Properties.");
        } finally {
            serverPort = Integer.parseInt(connectionProperties.getProperty("server.port"));
            connectionProperties.clear();
        }
    }

    private static void makeClientConnection() {
        ClientConnection server = ClientConnection.getInstance();
        server.connectToClient(serverPort);
    }
}