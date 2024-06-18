package org.cafeteria.client.services;

import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.common.model.User;

import java.io.IOException;
import java.util.Scanner;

public class AdminService extends UserManager {

    public AdminService(ServerConnection connection, User user, Scanner scanner) {
        super(connection, user, scanner);
    }

    @Override
    public void showUserActionItems() throws IOException {
        while (true) {
            System.out.println("1. Show Menu");
            System.out.println("2. Add Menu Item");
            System.out.println("3. Delete Menu Item");
            System.out.println("4. Update Menu Item");
            System.out.println("5. Exit");
            System.out.println("Enter your Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    showFoodItemMenu();
                    break;
                case 2:
                    System.out.print("Enter item to add: ");
                    String addItem = sc.nextLine();
                    addMenuItem(addItem);
                    break;
                case 3:
                    System.out.print("Enter item to delete: ");
                    String deleteItem = sc.nextLine();
                    deleteMenuItem(deleteItem);
                    break;
                case 4:
                    System.out.print("Enter item to update: ");
                    String updateItem = sc.nextLine();
                    System.out.print("Is available (true/false): ");
                    boolean availability = sc.nextBoolean();
                    updateMenuItem(updateItem, availability);
                    break;
                case 5:
                    connection.close();
                    return;
            }
        }
    }

    @Override
    public void showFoodItemMenu() throws IOException {
        String response = connection.sendData("SHOW_MENU");
        System.out.println(response);
    }

    private void addMenuItem(String item) throws IOException {
        String response = connection.sendData("ADD_MENU_ITEM" + item);
        System.out.println(response);
    }

    private void deleteMenuItem(String item) throws IOException {
        String response = connection.sendData("DELETE_MENU_ITEM" + item);
        System.out.println(response);
    }

    private void updateMenuItem(String item, boolean availability) throws IOException {
        String response = connection.sendData("UPDATE_MENU_ITEM" + item + " " + availability);
        System.out.println(response);
    }
}