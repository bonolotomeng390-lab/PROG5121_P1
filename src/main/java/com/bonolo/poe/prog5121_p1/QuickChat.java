/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bonolo.poe.prog5121_p1;

import java.util.List;
import java.util.Scanner;

public class QuickChat {

    // Entry point for the messaging feature - only call this after a successful login
    public static void start(Scanner scanner, String sender) {
        System.out.println("\nWelcome to QuickChat.");

        // Read any messages stored on a previous run into the stored messages array
        Message.loadStoredMessages();

        System.out.print("How many messages do you wish to enter? ");
        int maxMessages = readPositiveInt(scanner);
        int messagesEntered = 0;

        boolean running = true;
        while (running) {
            System.out.println("\n1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Choose an option: ");
            int choice = readInt(scanner);

            switch (choice) {
                case 1:
                    messagesEntered = sendMessages(scanner, sender, maxMessages, messagesEntered);
                    break;
                case 2:
                    System.out.println("\n" + Message.sentMessagesReport());
                    break;
                case 3:
                    running = false;
                    break;
                case 4:
                    storedMessagesMenu(scanner);
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1, 2, 3 or 4.");
            }
        }

        System.out.println("\nTotal messages sent: " + Message.returnTotalMessages());
        System.out.println("Goodbye!");
    }

    // Lets the user enter messages until the number they chose at the start is reached
    private static int sendMessages(Scanner scanner, String sender, int maxMessages, int messagesEntered) {
        if (messagesEntered >= maxMessages) {
            System.out.println("You have already entered all " + maxMessages + " message(s).");
            return messagesEntered;
        }

        while (messagesEntered < maxMessages) {
            System.out.println("\n--- Message " + (messagesEntered + 1) + " of " + maxMessages + " ---");

            String recipient = readRecipient(scanner);
            String text = readMessageText(scanner);

            Message message = new Message(Message.generateMessageId(), messagesEntered, sender, recipient, text);
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
                        System.out.println(message.disregardMessage());
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

    // The fourth main menu option: works with the stored messages array
    private static void storedMessagesMenu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Stored Messages ---");
            System.out.println("1) Display the sender and recipient of all stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search for a message ID");
            System.out.println("4) Search for all messages for a particular recipient");
            System.out.println("5) Delete a message using the message hash");
            System.out.println("6) Display a report of all stored messages");
            System.out.println("7) Back to main menu");
            System.out.print("Choose an option: ");

            switch (readInt(scanner)) {
                case 1:
                    System.out.println("\n" + Message.storedSendersAndRecipients());
                    break;
                case 2:
                    System.out.println("\nLongest stored message: " + Message.longestStoredMessage());
                    break;
                case 3:
                    System.out.print("Enter the message ID: ");
                    Message found = Message.findById(scanner.nextLine().trim());
                    if (found == null) {
                        System.out.println("No message found with that ID.");
                    } else {
                        System.out.println("Recipient: " + found.getRecipient());
                        System.out.println("Message: " + found.getText());
                    }
                    break;
                case 4:
                    System.out.print("Enter the recipient's number: ");
                    List<String> texts = Message.messageTextsForRecipient(scanner.nextLine().trim());
                    if (texts.isEmpty()) {
                        System.out.println("No messages found for that recipient.");
                    } else {
                        for (String t : texts) {
                            System.out.println("- " + t);
                        }
                    }
                    break;
                case 5:
                    System.out.print("Enter the message hash: ");
                    System.out.println(Message.deleteByHash(scanner.nextLine().trim()));
                    break;
                case 6:
                    System.out.println("\n" + Message.storedMessagesReport());
                    break;
                case 7:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1 to 7.");
            }
        }
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