/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bonolo.poe.prog5121_p1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Message {

    private static final int MAX_MESSAGE_LENGTH = 250;
    private static final String JSON_FILE = "storedMessages.json";

    // Messages handled while the program is running
    private static final List<Message> sentMessages = new ArrayList<>();
    private static final List<Message> storedMessages = new ArrayList<>();
    private static int totalMessagesSent = 0;

    private final String messageId;
    private final int messageNumber;
    private final String recipient;
    private final String text;
    private final String messageHash;

    public Message(String messageId, int messageNumber, String recipient, String text) {
        this.messageId = messageId;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.text = text;
        this.messageHash = createMessageHash();
    }

    // Randomly generated ten-digit message ID
    public static String generateMessageId() {
        Random random = new Random();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(random.nextInt(10));
        }
        return id.toString();
    }

    // Message ID must not be more than ten characters
    public boolean checkMessageID() {
        return messageId != null && !messageId.isEmpty() && messageId.length() <= 10;
    }

    // Message must not exceed 250 characters
    public static String checkMessageLength(String text) {
        int length = (text == null) ? 0 : text.length();
        if (length <= MAX_MESSAGE_LENGTH) {
            return "Message ready to send.";
        }
        return "Message exceeds 250 characters by " + (length - MAX_MESSAGE_LENGTH)
                + "; please reduce the size.";
    }

    // Cell number needs an international code (+) and no more than ten digits after the code
    public static String validateRecipientCell(String cell) {
        if (cell != null && cell.matches("^\\+\\d{1,3}\\d{1,10}$")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. "
                + "Please correct the number and try again.";
    }

    public String checkRecipientCell() {
        return validateRecipientCell(recipient);
    }

    // First two digits of ID : message number : FIRSTWORDLASTWORD (all caps)
    public String createMessageHash() {
        String prefix = messageId.length() >= 2 ? messageId.substring(0, 2) : messageId;
        String[] words = text.trim().split("\\s+");
        String first = words[0].replaceAll("[^A-Za-z0-9]", "");
        String last = words[words.length - 1].replaceAll("[^A-Za-z0-9]", "");
        return (prefix + ":" + messageNumber + ":" + first + last).toUpperCase();
    }

    public String messageIdCreated() {
        return "Message ID generated: " + messageId;
    }

    // 1 = send, 2 = disregard, 3 = store
    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                sentMessages.add(this);
                totalMessagesSent++;
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid choice.";
        }
    }

    // All messages sent while the program is running
    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages have been sent yet.";
        }
        StringBuilder all = new StringBuilder();
        for (Message m : sentMessages) {
            all.append(m.getDetails()).append("\n\n");
        }
        return all.toString().trim();
    }

    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    // Writes every stored message to a JSON file
    public void storeMessage() {
        storedMessages.add(this);
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            Message m = storedMessages.get(i);
            json.append("  {\n")
                .append("    \"messageId\": \"").append(escapeJson(m.messageId)).append("\",\n")
                .append("    \"messageHash\": \"").append(escapeJson(m.messageHash)).append("\",\n")
                .append("    \"recipient\": \"").append(escapeJson(m.recipient)).append("\",\n")
                .append("    \"message\": \"").append(escapeJson(m.text)).append("\"\n")
                .append("  }").append(i < storedMessages.size() - 1 ? "," : "").append("\n");
        }
        json.append("]");
        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            writer.write(json.toString());
        } catch (IOException e) {
            System.out.println("Could not write JSON file: " + e.getMessage());
        }
    }

    private static String escapeJson(String value) {
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }

    // Full details in the order: Message ID, Message Hash, Recipient, Message
    public String getDetails() {
        return "Message ID: " + messageId
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + text;
    }

    public String getMessageId() { return messageId; }
    public String getMessageHash() { return messageHash; }
    public String getRecipient() { return recipient; }
    public String getText() { return text; }
    public int getMessageNumber() { return messageNumber; }

    // Clears the running totals (used so each unit test starts fresh)
    public static void resetState() {
        sentMessages.clear();
        storedMessages.clear();
        totalMessagesSent = 0;
    }
}