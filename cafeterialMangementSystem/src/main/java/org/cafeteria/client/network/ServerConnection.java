package org.cafeteria.client.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerConnection {
    private static ServerConnection instance;
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    private ServerConnection(String SERVER_ADDRESS, int SERVER_PORT) throws IOException {
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public static ServerConnection getInstance(String serverAddress, int serverPort) throws IOException {
        if (instance == null) {
            instance = new ServerConnection(serverAddress, serverPort);
        }
        return instance;
    }

    public String sendData(String message) throws IOException {
        if (socket != null && socket.isConnected() && !socket.isClosed()) {
            out.println(message);
            return in.readLine();
        } else {
            System.out.println("Socket is not connected or already closed.");
            return null;
        }
    }

    public void close() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}