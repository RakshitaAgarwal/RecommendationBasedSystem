package org.cafeteria.server.network;

import org.cafeteria.common.customException.CustomExceptions;
import org.cafeteria.common.model.ParsedRequest;
import static org.cafeteria.common.communicationProtocol.CustomProtocol.parseRequest;
import static org.cafeteria.server.Server.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;

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
            System.out.println("Some Error occurred while reading input from client");
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
            case LOGIN -> response = userController.handleUserLogin(request);

            case ADD_MENU_ITEM -> response = menuController.addMenuItem(request);

            case DELETE_MENU_ITEM -> response = menuController.deleteMenuItem(request);

            case UPDATE_MENU_ITEM -> response = menuController.updateMenuItem(request);

            case SHOW_MENU -> response = menuController.ShowMenuItems();

            case GET_MENU_ITEM_BY_NAME -> response = menuController.getMenuItemByName(request);

            case SEE_MONTHLY_REPORT -> response = feedbackController.getFeedbackReport(request);

            case PROVIDE_FEEDBACK -> response = feedbackController.addFeedback(request);

            case PROVIDE_NEXT_DAY_MENU_OPTIONS -> response = dailyRecommendationController.getDailyRecommendation(request);

            case VOTE_NEXT_DAY_MENU -> response = dailyRecommendationController.voteForNextDayMenu(request);

            case SEE_NOTIFICATIONS -> response = notificationController.getUserNotification(request);

        }
        return response;
    }
}