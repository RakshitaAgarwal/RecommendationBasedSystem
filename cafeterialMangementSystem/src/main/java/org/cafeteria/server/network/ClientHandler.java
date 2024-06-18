package org.cafeteria.server.network;

import com.sun.istack.NotNull;
import org.cafeteria.common.customException.CustomExceptions;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.common.model.ResponseCode;
import org.cafeteria.common.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;
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
                switch (request.getUserAction()) {
                    case LOGIN -> {
                        handleUserLogin(request);
                    }
                    case ADD_MENU_ITEM -> {
//                        menuService.add();
                    }
                    case DELETE_MENU_ITEM -> {
//                        menuService.delete();
                    }
                    case UPDATE_MENU_ITEM -> {
//                        menuService.update();
                    }
                    case SHOW_MENU -> {
//                        menuService.getAll();
                    }
                    case SEE_MONTHLY_REPORT -> {
                        feedbackService.getFeedbackReport();
                    }
                    case PROVIDE_NEXT_DAY_MENU_OPTIONS -> {
                        dailyRecommendationService.getDailyRecommendation();
                    }
                    case PROVIDE_FEEDBACK -> {
//                        feedbackService.add();
                    }
                    case SEE_NOTIFICATIONS -> {
                        notificationService.getUserNotification();
                    }
                    case VOTE_NEXT_DAY_MENU -> {
                        dailyRecommendationService.voteForNextDayMenu();
                    }
                }
            }
        } catch (SocketException e) {
            System.out.println("Client Got disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CustomExceptions.InvalidRequestException | SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleUserLogin(@NotNull ParsedRequest request) throws SQLException {
        User user = deserializeData(request.getJsonData(), User.class);
        System.out.println(user.getId() + " " + user.getName());
        User loggedInUser = userService.loginUser(user);
        String response;
        if (loggedInUser != null) {
            response = createResponse(ResponseCode.OK, serializeData(loggedInUser));
        } else {
            response = createResponse(ResponseCode.UNAUTHORIZED, null);
        }
        System.out.println("response that is sent to client: " + response);
        out.println(response);
    }
}