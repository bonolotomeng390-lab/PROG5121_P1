/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bonolo.poe.prog5121_p1;

import java.util.Scanner;

public class QuickChat {

    // Entry point for the messaging feature - only call this after a successful login
    public static void start(Scanner scanner) {
        System.out.println("\nWelcome to QuickChat.");

        System.out.print("How many messages do you wish to enter? ");
        int maxMessages = readPositiveInt(scanner);
        int messagesEntered = 0;

        boolean running = true;
        while (running) {
            System.out.println("\n1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");
            int choice = readInt(scanner);

            switch (choice) {
                case 1:
                    messagesEntered = sendMessages(scanner, maxMessages, messagesEntered);
                    break;
                case 2:
                    System.out.println("Coming Soon.");
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1, 2 or 3.");
            }
        }

        System.out.println("\nTotal messages sent: " + Message.returnTotalMessages());
        System.out.println("Goodbye!");
    }

    // Lets the user enter messages until the number they chose at the start is reached
    private static int sendMessages(Scanner scanner, int maxMessages, int messagesEntered) {
        if (messagesEntered >= maxMessages) {
            System.out.println("You have already entered all " + maxMessages + " message(s).");
            return messagesEntered;
        }

        while (messagesEntered < maxMessages) {
            System.out.println("\n--- Message " + (messagesEntered + 1) + " of " + maxMessages + " ---");

            String recipient = readRecipient(scanner);
            String text = readMessageText(scanner);

            Message message = new Message(Message.generateMessageId(), messagesEntered, recipient, text);
            System.out.println(message.messageIdCreated());

            boolean resolved = false;
            while (!resolved) {
                System.out.println("\n1) Send Message");
                System.out.println("2) Disregard Message");
                System.out.println("3) Store Message to send later");
                System.out.print("Choose an option: ");
                int option = readInt(scanner);

                if (option == 1) {
                    System.out.println(message.sentMessage(1));
                    System.out.println("\n" + message.getDetails());
                    resolved = true;
                } else if (option == 2) {
                    System.out.println(message.sentMessage(2));
                    if (readInt(scanner) == 0) {
                        System.out.println("Message deleted.");
                        resolved = true;
                    } else {
                        System.out.println("Message was not deleted. Please choose again.");
                    }
                } else if (option == 3) {
                    System.out.println(message.sentMessage(3));
                    resolved = true;
                } else {
                    System.out.println("Invalid option. Please choose 1, 2 or 3.");
                }
            }
            messagesEntered++;
        }

        System.out.println("\nAll messages entered. Total messages sent: " + Message.returnTotalMessages());
        return messagesEntered;
    }

    private static String readRecipient(Scanner scanner) {
        while (true) {
            System.out.print("Enter recipient cell number (e.g. +27718693002): ");
            String cell = scanner.nextLine().trim();
            String result = Message.validateRecipientCell(cell);
            System.out.println(result);
            if (result.equals("Cell phone number successfully captured.")) {
                return cell;
            }
        }
    }

    private static String readMessageText(Scanner scanner) {
        while (true) {
            System.out.print("Enter your message (max 250 characters): ");
            String text = scanner.nextLine();
            if (text.trim().isEmpty()) {
                System.out.println("The message cannot be empty.");
            } else if (Message.checkMessageLength(text).equals("Message ready to send.")) {
                return text;
            } else {
                System.out.println("Please enter a message of less than 250 characters.");
            }
        }
    }

    private static int readInt(Scanner scanner) {
        while (true) {
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    private static int readPositiveInt(Scanner scanner) {
        while (true) {
            int value = readInt(scanner);
            if (value > 0) {
                return value;
            }
            System.out.print("Please enter a number greater than 0: ");
        }
    }
}