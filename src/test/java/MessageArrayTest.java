/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import com.bonolo.poe.prog5121_p1.Message;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class MessageArrayTest {

    private static final String TEXT1 = "Did you get the cake?";
    private static final String TEXT2 = "Where are you? You are late! I have asked you to be on time.";
    private static final String TEXT3 = "Yohoooo, I am at your gate.";
    private static final String TEXT4 = "It is dinner time!";
    private static final String TEXT5 = "Ok, I am leaving without you.";

    private Message m1, m2, m3, m4, m5;

    @BeforeEach
    public void setUp(@TempDir Path tempDir) {
        Message.resetState();
        Message.setJsonFile(tempDir.resolve("storedMessages.json").toString());
        // Test data from the brief (message 4 is the developer entry)
        m1 = new Message("0012345678", 0, "Kyle Smith", "+27834557896", TEXT1);
        m2 = new Message("0198765432", 1, "Kyle Smith", "+27838884567", TEXT2);
        m3 = new Message("0211112222", 2, "Kyle Smith", "+27834484567", TEXT3);
        m4 = new Message("0838884567", 3, "Kyle Smith", "0838884567", TEXT4);
        m5 = new Message("0344445555", 4, "Kyle Smith", "+27838884567", TEXT5);
    }

    // Message 1 = Sent, 2 = Stored, 3 = Disregard, 4 = Sent
    private void populateMessages1To4() {
        m1.sentMessage(1);
        m2.sentMessage(3);
        m3.disregardMessage();
        m4.sentMessage(1);
    }

    // Message 5 = Stored
    private void populateAllMessages() {
        populateMessages1To4();
        m5.sentMessage(3);
    }

    // ---------- Arrays ----------

    @Test
    public void testSentMessagesArray_CorrectlyPopulated() {
        populateMessages1To4();
        assertEquals(List.of(TEXT1, TEXT4), Message.sentMessageTexts());
    }

    @Test
    public void testDisregardedMessagesArray() {
        populateMessages1To4();
        assertEquals(List.of(TEXT3), Message.disregardedMessageTexts());
    }

    @Test
    public void testStoredMessagesArray() {
        populateAllMessages();
        assertEquals(List.of(TEXT2, TEXT5), Message.storedMessageTexts());
    }

    @Test
    public void testMessageHashArray() {
        populateMessages1To4();
        assertEquals(List.of("00:0:DIDCAKE", "01:1:WHERETIME", "02:2:YOHOOOOGATE", "08:3:ITTIME"),
                Message.getMessageHashes());
    }

    @Test
    public void testMessageIdArray() {
        populateMessages1To4();
        assertEquals(List.of("0012345678", "0198765432", "0211112222", "0838884567"),
                Message.getMessageIds());
    }

    // ---------- Stored messages menu ----------

    @Test
    public void testDisplayLongestMessage() {
        populateMessages1To4();
        assertEquals(TEXT2, Message.longestStoredMessage());
    }

    @Test
    public void testSearchForMessageId() {
        populateMessages1To4();
        assertEquals(TEXT4, Message.findById("0838884567").getText());
    }

    @Test
    public void testSearchForMessageId_NotFound() {
        populateMessages1To4();
        assertNull(Message.findById("9999999999"));
    }

    @Test
    public void testSearchMessagesForRecipient() {
        populateAllMessages();
        assertEquals(List.of(TEXT2, TEXT5), Message.messageTextsForRecipient("+27838884567"));
    }

    @Test
    public void testDeleteMessageUsingHash() {
        populateMessages1To4();
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time\" successfully deleted.",
                Message.deleteByHash("01:1:WHERETIME"));
        assertTrue(Message.storedMessageTexts().isEmpty());
    }

    @Test
    public void testDeleteMessageUsingHash_NotFound() {
        populateMessages1To4();
        assertEquals("No stored message found with that hash.", Message.deleteByHash("99:9:NOPE"));
    }

    @Test
    public void testDisplaySenderAndRecipientOfStoredMessages() {
        populateAllMessages();
        assertEquals("Sender: Kyle Smith | Recipient: +27838884567\n"
                + "Sender: Kyle Smith | Recipient: +27838884567",
                Message.storedSendersAndRecipients());
    }

    // ---------- Reports ----------

    @Test
    public void testDisplayReport_SentMessages() {
        populateMessages1To4();
        String report = Message.sentMessagesReport();
        assertTrue(report.contains("Message Hash: 00:0:DIDCAKE"));
        assertTrue(report.contains("Recipient: +27834557896"));
        assertTrue(report.contains("Message: " + TEXT1));
        assertTrue(report.contains("Message Hash: 08:3:ITTIME"));
        assertTrue(report.contains("Message: " + TEXT4));
        assertFalse(report.contains(TEXT2));
    }

    @Test
    public void testStoredMessagesReport_FullDetails() {
        populateAllMessages();
        String report = Message.storedMessagesReport();
        assertTrue(report.contains("Message ID: 0198765432"));
        assertTrue(report.contains("Message Hash: 01:1:WHERETIME"));
        assertTrue(report.contains("Sender: Kyle Smith"));
        assertTrue(report.contains("Recipient: +27838884567"));
        assertTrue(report.contains("Message: " + TEXT5));
    }

    // ---------- JSON file ----------

    @Test
    public void testStoredMessagesReadBackFromJson() {
        populateAllMessages();
        Message.resetState();               // empty the arrays, keep the file
        Message.loadStoredMessages();       // read the JSON file into the stored array
        assertEquals(List.of(TEXT2, TEXT5), Message.storedMessageTexts());
        assertTrue(Message.getMessageHashes().contains("01:1:WHERETIME"));
        assertTrue(Message.storedSendersAndRecipients().contains("Kyle Smith"));
    }

    @Test
    public void testJsonRoundTrip_SpecialCharacters() {
        Message special = new Message("0555555555", 0, "Kyle Smith", "+27834557896",
                "He said \"hi\" and left \\ quickly");
        special.sentMessage(3);
        Message.resetState();
        Message.loadStoredMessages();
        assertEquals(List.of("He said \"hi\" and left \\ quickly"), Message.storedMessageTexts());
    }

    @Test
    public void testDeleteIsSavedToJson() {
        populateAllMessages();
        Message.deleteByHash("01:1:WHERETIME");
        Message.resetState();
        Message.loadStoredMessages();
        assertEquals(List.of(TEXT5), Message.storedMessageTexts());
    }
}