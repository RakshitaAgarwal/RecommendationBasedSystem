package org.cafeteria.client.consoleManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class UserConsoleManager {
    protected static Scanner sc = new Scanner(System.in);
    private static boolean shouldConsumeNextLine = false;

    public static int takeUserIntInput(String message) {
        int choice = 0;
        try {
            System.out.println(message);
            choice = sc.nextInt();
            shouldConsumeNextLine = true;
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            sc.next();
        }
        return choice;
    }

    public static String takeUserStringInput(String message) {
        System.out.println(message);
        if(shouldConsumeNextLine)
            sc.nextLine();
        shouldConsumeNextLine = false;
        return sc.nextLine();
    }

    public static float takeUserFloatInput(String message) {
        float input = 0F;
        try {
            System.out.println(message);
            input = sc.nextFloat();
            shouldConsumeNextLine = true;
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number in float");
            sc.next();
        }
        return input;
    }

    public static boolean takeUserBooleanInput(String message) {
        boolean input = false;
        boolean isValid = false;
        while (!isValid) {
            try {
                System.out.println(message + " (true/false):");
                input = sc.nextBoolean();
                isValid = true;
                shouldConsumeNextLine = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter 'true' or 'false'.");
                sc.next();
            }
        }
        return input;
    }

    public static Date takeUserDateInput(String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        Date inputDate = null;

        while (inputDate == null) {
            String date = takeUserStringInput(message);
            try {
                Date currentDate = new Date();
                Date minDate = dateFormat.parse("1990-01-01");
                inputDate = dateFormat.parse(date);
                if (inputDate.after(currentDate)) {
                    System.out.println("The date should not be in the future.");
                    inputDate = null;
                } else if (inputDate.before(minDate)) {
                    System.out.println("The date should not be before 1990.");
                    inputDate = null;
                }
            } catch (ParseException e) {
                System.out.println("Invalid Date Format. Please enter the date in yyyy-MM-dd format.");
            }
        }
        return inputDate;
    }

    public static void displayMessage(String message) {
        System.out.println("-----------------------------------------");
        System.out.println(message);
        System.out.println("-----------------------------------------");
    }
}