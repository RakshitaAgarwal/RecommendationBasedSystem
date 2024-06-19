package org.cafeteria.server.network;

import org.cafeteria.common.customException.CustomExceptions;
import org.cafeteria.common.model.ParsedRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.parseRequest;
import static org.cafeteria.server.Server.*;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final BufferedReader in;
    private final PrintWriter out;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
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
                String response = handleRequest(request);
                System.out.println("response that is sent to client: " + response);
                out.println(response);
            }
        } catch (SQLException | CustomExceptions.InvalidRequestException ex) {
            throw new RuntimeException(ex);
        } catch (SocketException e) {
            System.out.println("Client Got disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String handleRequest(ParsedRequest request) throws SQLException {
        String response = null;
        switch (request.getUserAction()) {
            case LOGIN -> response = userHandler.handleUserLogin(request);

            case ADD_MENU_ITEM -> response = menuHandler.addMenuItem(request);

            case DELETE_MENU_ITEM -> response = menuHandler.deleteMenuItem(request);

            case UPDATE_MENU_ITEM -> response = menuHandler.updateMenuItem(request);

            case SHOW_MENU -> response = menuHandler.ShowMenuItems(request);

            case GET_MENU_ITEM_BY_NAME -> response = menuHandler.getMenuItemByName(request);

            case SEE_MONTHLY_REPORT -> response = feedbackHandler.getFeedbackReport(request);

            case PROVIDE_FEEDBACK -> response = feedbackHandler.addFeedback(request);

            case PROVIDE_NEXT_DAY_MENU_OPTIONS -> response = dailyRecommendationHandler.getDailyRecommendation(request);

            case VOTE_NEXT_DAY_MENU -> response = dailyRecommendationHandler.voteForNextDayMenu(request);

            case SEE_NOTIFICATIONS -> response = notificationHandler.getUserNotification(request);

        }
        return response;
    }
}