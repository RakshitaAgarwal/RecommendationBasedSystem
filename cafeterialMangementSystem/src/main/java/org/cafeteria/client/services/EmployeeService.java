package org.cafeteria.client.services;

import com.google.gson.JsonSyntaxException;
import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.common.customException.CustomExceptions;
import org.cafeteria.common.model.*;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;

public class EmployeeService extends UserManager {

    public EmployeeService(ServerConnection connection, User user, Scanner sc) {
        super(connection, user, sc);
    }

    @Override
    public void showUserActionItems() throws IOException {
        while (true) {
            System.out.println("1. Show Menu");
            System.out.println("2. See Notifications");
            System.out.println("3. Vote For Next Day Menu");
            System.out.println("4. Provide Feedback");
            System.out.println("5. Exit");
            System.out.println("Enter your Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    displayMenuFromServer();
                    break;
                case 2:
                    seeNotifications();
                    break;
                case 3:
                    voteForNextDayMenu("");
                    break;
                case 4:
                    provideFeedback("Feedback String");
                    break;
                case 5:
                    connection.close();
                    return;
            }
        }
    }

    @Override
    public void displayMenuFromServer() throws IOException {
        String request = createRequest(UserAction.SHOW_MENU, null);
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);
        if( response != null) {
            try {
                ParsedResponse parsedResponse = parseResponse(response);
                ResponseCode responseCode = parsedResponse.getResponseCode();
                if (responseCode == ResponseCode.OK) {
                    List<MenuItem> menu = deserializeList(parsedResponse.getJsonData(), MenuItem.class);
                    displayMenu(menu);
                } else {
                    System.out.println("Unexpected Response Code: " + responseCode);
                }
            } catch (CustomExceptions.InvalidResponseException e) {
                System.out.println("Invalid Response Received from Server");
            } catch (JsonSyntaxException e) {
                System.out.println("Error deserializing JSON data: " + e.getMessage());
            }
        } else {
            throw new IOException("Server Got Disconnected. Please Try again.");
        }
    }

    private void displayMenu(List<MenuItem> menu) {
        System.out.println();
        System.out.println("--------Food Item Menu--------");
        for (MenuItem item : menu) {
            System.out.println(item.getName() + "  " + item.getPrice() + " Rs  Available Status: " + item.isAvailable());
        }
        System.out.println("------------------------------");
    }

    public void seeNotifications() throws IOException {
        String response = connection.sendData("SEE_NOTIFICATIONS");
        System.out.println(response);
    }

    public void voteForNextDayMenu(String vote) throws IOException {
        String response = connection.sendData("VOTE_NEXT_DAY_MENU " + vote);
        System.out.println(response);
    }

    public void provideFeedback(String feedback) throws IOException {
        String response = connection.sendData("EMPLOYEE_FEEDBACK " + feedback);
        System.out.println(response);
    }
}