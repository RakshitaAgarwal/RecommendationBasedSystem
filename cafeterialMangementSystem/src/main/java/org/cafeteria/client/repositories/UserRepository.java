package org.cafeteria.client.repositories;

import org.cafeteria.client.network.ServerConnection;
import java.io.IOException;

public abstract class UserRepository {

    protected static ServerConnection connection;
    UserRepository(ServerConnection connection) {
        UserRepository.connection = connection;
    }

    public void closeConnection() throws IOException {
        connection.close();
    }
}