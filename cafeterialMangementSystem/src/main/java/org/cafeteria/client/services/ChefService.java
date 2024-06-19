package org.cafeteria.client.services;

import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.common.model.User;

import java.io.IOException;
import java.util.Scanner;

public class ChefService extends UserManager {

    public ChefService(ServerConnection connection, User user, Scanner sc) {
        super(connection, user, sc);
    }

    @Override
    public void showUserActionItems() throws IOException {
        while (true) {
            System.out.println("1. Show Menu");
            System.out.println("2. See Monthly Report");
            System.out.println("3. Provide Next Day Menu Options");
            System.out.println("4. Exit");
            System.out.println("Enter your Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    displayMenuFromServer();
                    break;
                case 2:
                    seeMonthlyReport();
                    break;
                case 3:
                    provideNextDayMenuOptions(""," ", "");
                    break;
                case 4:
                    connection.close();
                    return;
            }
        }
    }

    @Override
    public void displayMenuFromServer() throws IOException {
        String response = connection.sendData("SHOW_MENU");
        System.out.println(response);
    }

    public void seeMonthlyReport() throws IOException {
        String response = connection.sendData("SEE_MONTHLY_REPORT");
        System.out.println(response);
    }

    public void provideNextDayMenuOptions(String breakfast, String lunch, String dinner) throws IOException {
        String response = connection.sendData("NEXT_DAY_MENU " + breakfast + " " + lunch + " " + dinner);
        System.out.println(response);
    }
}