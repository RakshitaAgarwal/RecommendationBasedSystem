package org.cafeteria.server;

import org.cafeteria.client.Client;
import org.cafeteria.server.handlers.*;
import org.cafeteria.server.network.ClientConnection;
import org.cafeteria.server.network.JdbcConnection;

import java.io.IOException;
import java.util.Properties;

import static org.cafeteria.common.constants.Constants.*;

public class Server {
    public static UserHandler userHandler;
    public static MenuHandler menuHandler;
    public static FeedbackHandler feedbackHandler;
    public static DailyRecommendationHandler dailyRecommendationHandler;
    public static NotificationHandler notificationHandler;

    public static void main(String[] args) {
        initDatabaseProperties();
        makeDatabaseConnection();
        initServices();
        initServerProperties();
        makeClientConnection();
        closeResources();
    }

    private static void initServices() {
        userHandler = new UserHandler();
        menuHandler = new MenuHandler();
        feedbackHandler = new FeedbackHandler();
        dailyRecommendationHandler = new DailyRecommendationHandler();
        notificationHandler = new NotificationHandler();
    }


    private static void initDatabaseProperties() {
        Properties connectionProperties = new Properties();
        try {
            connectionProperties.load(Client.class.getClassLoader().getResourceAsStream("database.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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