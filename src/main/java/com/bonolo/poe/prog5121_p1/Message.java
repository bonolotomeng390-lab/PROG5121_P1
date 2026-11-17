/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bonolo.poe.prog5121_p1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Message {

    private static final int MAX_MESSAGE_LENGTH = 250;
    private static String jsonFile = "storedMessages.json";

    // The five arrays from the brief - filled while the program runs (nothing is hard-coded)
    private static final List<Message> sentMessages = new ArrayList<>();
    private static final List<Message> disregardedMessages = new ArrayList<>();
    private static final List<Message> storedMessages = new ArrayList<>();
    private static final List<String> messageHashes = new ArrayList<>();
    private static final List<String> messageIds = new ArrayList<>();
    private static int totalMessagesSent = 0;

    private final String messageId;
    private final int messageNumber;
    private final String sender;
    private final String recipient;
    private final String text;
    private final String messageHash;

    public Message(String messageId, int messageNumber, String sender, String recipient, String text) {
        this.messageId = messageId;
        this.messageNumber = messageNumber;
        this.sender = sender;
        this.recipient = recipient;
        this.text = text;
        this.messageHash = createMessageHash();
    }

    // Part 2 constructor - sender is not known
    public Message(String messageId, int messageNumber, String recipient, String text) {
        this(messageId, messageNumber, "Unknown", recipient, text);
    }

    // ---------------- Part 2 rules ----------------

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

    // 1 = send, 2 = disregard (asks the user to press 0), 3 = store
    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                sentMessages.add(this);
                totalMessagesSent++;
                recordIdentifiers();
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

    // Called once the user has pressed 0 to delete: the message goes into the disregarded array
    public String disregardMessage() {
        disregardedMessages.add(this);
        recordIdentifiers();
        return "Message deleted.";
    }

    // Adds this message to the stored array and saves every stored message to the JSON file
    public void storeMessage() {
        storedMessages.add(this);
        recordIdentifiers();
        writeStoredMessagesJson();
    }

    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    // Keeps the Message Hash and Message ID arrays up to date
    private void recordIdentifiers() {
        messageHashes.add(messageHash);
        messageIds.add(messageId);
    }

    // ---------------- Part 3: reading the arrays ----------------

    public static List<String> sentMessageTexts() { return texts(sentMessages); }
    public static List<String> disregardedMessageTexts() { return texts(disregardedMessages); }
    public static List<String> storedMessageTexts() { return texts(storedMessages); }
    public static List<String> getMessageHashes() { return new ArrayList<>(messageHashes); }
    public static List<String> getMessageIds() { return new ArrayList<>(messageIds); }

    private static List<String> texts(List<Message> list) {
        List<String> result = new ArrayList<>();
        for (Message m : list) {
            result.add(m.text);
        }
        return result;
    }

    // a) Sender and recipient of every stored message
    public static String storedSendersAndRecipients() {
        if (storedMessages.isEmpty()) {
            return "No stored messages.";
        }
        StringBuilder all = new StringBuilder();
        for (Message m : storedMessages) {
            all.append("Sender: ").append(m.sender)
               .append(" | Recipient: ").append(m.recipient).append("\n");
        }
        return all.toString().trim();
    }

    // b) The longest stored message
    public static String longestStoredMessage() {
        if (storedMessages.isEmpty()) {
            return "No stored messages.";
        }
        Message longest = storedMessages.get(0);
        for (Message m : storedMessages) {
            if (m.text.length() > longest.text.length()) {
                longest = m;
            }
        }
        return longest.text;
    }

    // c) Find a message by its ID (returns null when there is no match)
    public static Message findById(String id) {
        for (List<Message> list : List.of(sentMessages, disregardedMessages, storedMessages)) {
            for (Message m : list) {
                if (m.messageId.equals(id)) {
                    return m;
                }
            }
        }
        return null;
    }

    // d) Every message sent or stored for one recipient
    public static List<String> messageTextsForRecipient(String recipient) {
        List<String> result = new ArrayList<>();
        for (List<Message> list : List.of(sentMessages, storedMessages)) {
            for (Message m : list) {
                if (m.recipient.equals(recipient)) {
                    result.add(m.text);
                }
            }
        }
        return result;
    }

    // e) Delete a stored message using its hash
    public static String deleteByHash(String hash) {
        for (int i = 0; i < storedMessages.size(); i++) {
            Message m = storedMessages.get(i);
            if (m.messageHash.equalsIgnoreCase(hash)) {
                storedMessages.remove(i);
                messageHashes.remove(m.messageHash);
                messageIds.remove(m.messageId);
                writeStoredMessagesJson();
                // The brief shows the deleted text without its final full stop
                String shown = m.text.endsWith(".") ? m.text.substring(0, m.text.length() - 1) : m.text;
                return "Message: \"" + shown + "\" successfully deleted.";
            }
        }
        return "No stored message found with that hash.";
    }

    // f) Full details of every stored message
    public static String storedMessagesReport() {
        if (storedMessages.isEmpty()) {
            return "No stored messages.";
        }
        StringBuilder report = new StringBuilder();
        for (Message m : storedMessages) {
            report.append("Message ID: ").append(m.messageId)
                  .append("\nMessage Hash: ").append(m.messageHash)
                  .append("\nSender: ").append(m.sender)
                  .append("\nRecipient: ").append(m.recipient)
                  .append("\nMessage: ").append(m.text).append("\n\n");
        }
        return report.toString().trim();
    }

    // Report of the sent messages: hash, recipient and message
    public static String sentMessagesReport() {
        if (sentMessages.isEmpty()) {
            return "No messages have been sent yet.";
        }
        StringBuilder report = new StringBuilder();
        for (Message m : sentMessages) {
            report.append("Message Hash: ").append(m.messageHash)
                  .append("\nRecipient: ").append(m.recipient)
                  .append("\nMessage: ").append(m.text).append("\n\n");
        }
        return report.toString().trim();
    }

    // All messages sent while the program is running (full details)
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

    // ---------------- JSON storage ----------------

    public static void setJsonFile(String path) {
        jsonFile = path;
    }

    // Writes every stored message to the JSON file
    private static void writeStoredMessagesJson() {
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            Message m = storedMessages.get(i);
            json.append("  {\n")
                .append("    \"messageId\": \"").append(escapeJson(m.messageId)).append("\",\n")
                .append("    \"messageNumber\": \"").append(m.messageNumber).append("\",\n")
                .append("    \"messageHash\": \"").append(escapeJson(m.messageHash)).append("\",\n")
                .append("    \"sender\": \"").append(escapeJson(m.sender)).append("\",\n")
                .append("    \"recipient\": \"").append(escapeJson(m.recipient)).append("\",\n")
                .append("    \"message\": \"").append(escapeJson(m.text)).append("\"\n")
                .append("  }").append(i < storedMessages.size() - 1 ? "," : "").append("\n");
        }
        json.append("]");
        try {
            Files.writeString(Paths.get(jsonFile), json.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Could not write JSON file: " + e.getMessage());
        }
    }

    // Reads the JSON file back into the stored messages array
    public static void loadStoredMessages() {
        storedMessages.clear();
        Path path = Paths.get(jsonFile);
        if (!Files.exists(path)) {
            return;
        }
        try {
            String json = Files.readString(path, StandardCharsets.UTF_8);
            for (Map<String, String> object : parseJsonObjects(json)) {
                int number = 0;
                try {
                    number = Integer.parseInt(object.getOrDefault("messageNumber", "0"));
                } catch (NumberFormatException e) {
                    number = 0;
                }
                Message m = new Message(
                        object.getOrDefault("messageId", ""),
                        number,
                        object.getOrDefault("sender", "Unknown"),
                        object.getOrDefault("recipient", ""),
                        object.getOrDefault("message", ""));
                storedMessages.add(m);
                if (!messageIds.contains(m.messageId)) {
                    messageIds.add(m.messageId);
                    messageHashes.add(m.messageHash);
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Could not read " + jsonFile + ": " + e.getMessage());
        }
    }

    private static String escapeJson(String value) {
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }

    // Small reader for the file format written above: an array of objects with text values
    private static List<Map<String, String>> parseJsonObjects(String json) {
        List<Map<String, String>> result = new ArrayList<>();
        JsonReader reader = new JsonReader(json);
        if (reader.atEnd()) {
            return result;
        }
        reader.expect('[');
        if (reader.peek() == ']') {
            return result;
        }
        while (true) {
            reader.expect('{');
            Map<String, String> object = new LinkedHashMap<>();
            if (reader.peek() == '}') {
                reader.expect('}');
            } else {
                while (true) {
                    String key = reader.readString();
                    reader.expect(':');
                    object.put(key, reader.readString());
                    char next = reader.peek();
                    reader.expect(next);
                    if (next == '}') {
                        break;
                    }
                    if (next != ',') {
                        throw new IllegalArgumentException("Expected ',' or '}' in the JSON file");
                    }
                }
            }
            result.add(object);
            char next = reader.peek();
            reader.expect(next);
            if (next == ']') {
                break;
            }
            if (next != ',') {
                throw new IllegalArgumentException("Expected ',' or ']' in the JSON file");
            }
        }
        return result;
    }

    private static class JsonReader {
        private final String s;
        private int i = 0;

        JsonReader(String s) {
            this.s = s;
        }

        private void skipWhitespace() {
            while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
                i++;
            }
        }

        boolean atEnd() {
            skipWhitespace();
            return i >= s.length();
        }

        char peek() {
            skipWhitespace();
            if (i >= s.length()) {
                throw new IllegalArgumentException("Unexpected end of the JSON file");
            }
            return s.charAt(i);
        }

        void expect(char c) {
            if (peek() != c) {
                throw new IllegalArgumentException("Expected '" + c + "' at position " + i);
            }
            i++;
        }

        String readString() {
            expect('"');
            StringBuilder sb = new StringBuilder();
            while (true) {
                if (i >= s.length()) {
                    throw new IllegalArgumentException("Unterminated text in the JSON file");
                }
                char c = s.charAt(i++);
                if (c == '"') {
                    return sb.toString();
                }
                if (c != '\\') {
                    sb.append(c);
                    continue;
                }
                if (i >= s.length()) {
                    throw new IllegalArgumentException("Bad escape in the JSON file");
                }
                char e = s.charAt(i++);
                switch (e) {
                    case 'n': sb.append('\n'); break;
                    case 'r': sb.append('\r'); break;
                    case 't': sb.append('\t'); break;
                    case 'b': sb.append('\b'); break;
                    case 'f': sb.append('\f'); break;
                    case 'u':
                        if (i + 4 > s.length()) {
                            throw new IllegalArgumentException("Bad unicode escape in the JSON file");
                        }
                        sb.append((char) Integer.parseInt(s.substring(i, i + 4), 16));
                        i += 4;
                        break;
                    default:
                        sb.append(e); // covers \" \\ and \/
                }
            }
        }
    }

    // ---------------- Display and helpers ----------------

    // Full details in the order: Message ID, Message Hash, Recipient, Message
    public String getDetails() {
        return "Message ID: " + messageId
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + text;
    }

    public String getMessageId() { return messageId; }
    public String getMessageHash() { return messageHash; }
    public String getSender() { return sender; }
    public String getRecipient() { return recipient; }
    public String getText() { return text; }
    public int getMessageNumber() { return messageNumber; }

    // Clears the arrays and totals (used so each unit test starts fresh)
    public static void resetState() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIds.clear();
        totalMessagesSent = 0;
    }
}