package org.cafeteria.client.services;

import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.common.model.User;

import java.io.IOException;
import java.util.Scanner;

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