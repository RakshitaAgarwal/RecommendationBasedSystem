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
            e.printStackTrace();
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
            out.println(message);
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}