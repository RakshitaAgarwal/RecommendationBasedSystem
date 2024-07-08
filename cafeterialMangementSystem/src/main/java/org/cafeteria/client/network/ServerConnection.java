package org.cafeteria.client.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerConnection {
    private static ServerConnection instance;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private ServerConnection(String SERVER_ADDRESS, int SERVER_PORT) {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Some Error Occurred while creating the server connection.");
        }
    }

    public static ServerConnection getInstance(String serverAddress, int serverPort) {
        if (instance == null) {
            instance = new ServerConnection(serverAddress, serverPort);
        }
        return instance;
    }

    public String sendData(String message) {
        try {
            if (socket != null && socket.isConnected() && !socket.isClosed()) {
                out.println(message);
                return in.readLine();
            } else {
                System.out.println("Socket is not connected or already closed.");
                return null;
            }
        } catch (IOException e) {
            System.out.println("Server got disconnected. Please try again.");
            return null;
        }
    }

    public void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("Connection to Server closed.");
            }
        } catch (IOException e) {
            System.out.println("Error occurred while closing the server connection.");
        }
    }
}