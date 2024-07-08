package org.cafeteria.server.network;

import org.cafeteria.common.customException.CustomExceptions.DuplicateEntryFoundException;
import org.cafeteria.common.customException.CustomExceptions.InvalidRequestException;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.server.RequestHandler;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.parseRequest;

public class ClientHandler implements Runnable {
    private final BufferedReader in;
    private final PrintWriter out;
    private final RequestHandler requestHandler;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        requestHandler = new RequestHandler();
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println();
                System.out.println("message that is received from client: " + message);
                ParsedRequest request = parseRequest(message);
                System.out.println("parsed request that is received from client: " + request.getUserAction() + " " + request.getJsonData());
                String response = requestHandler.handleRequest(request);
                System.out.println("response that is sent to client: " + response);
                out.println(response);
            }
            System.out.println("Client Got disconnected");
            requestHandler.endUserSession();
        } catch (DuplicateEntryFoundException | InvalidRequestException e) {
            System.out.println(e.getMessage());
        } catch (SocketException e) {
            System.out.println("Client Got disconnected");
            requestHandler.endUserSession();
        } catch (IOException e) {
            System.out.println("Some Error occurred while reading input from client");
        }
    }
}