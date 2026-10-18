/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import com.bonolo.poe.prog5121_p1.Message;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MessageTest {

    private Message message1; // Test data: message 1 (send)
    private Message message2; // Test data: message 2 (discard)

    @BeforeEach
    public void setUp() {
        Message.resetState();
        // Fixed IDs starting with "00" so the hash is predictable
        message1 = new Message("0012345678", 0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        message2 = new Message("0098765432", 1, "08575975889",
                "Hi Keegan, did you receive the payment?");
    }

    // ---------- Message length ----------

    @Test
    public void testMessageLength_Success() {
        assertEquals("Message ready to send.", Message.checkMessageLength(message1.getText()));
    }

    @Test
    public void testMessageLength_Failure() {
        String tooLong = "a".repeat(260);
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                Message.checkMessageLength(tooLong));
    }

    // ---------- Recipient number ----------

    @Test
    public void testRecipientCell_Success() {
        assertEquals("Cell phone number successfully captured.", message1.checkRecipientCell());
    }

    @Test
    public void testRecipientCell_Failure() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. "
                + "Please correct the number and try again.", message2.checkRecipientCell());
    }

    // ---------- Message hash ----------

    @Test
    public void testMessageHash_TestCase1() {
        assertEquals("00:0:HITONIGHT", message1.createMessageHash());
    }

    @Test
    public void testMessageHash_LoopThroughAllMessages() {
        Message[] messages = { message1, message2 };
        String[] expected = { "00:0:HITONIGHT", "00:1:HIPAYMENT" };
        for (int i = 0; i < messages.length; i++) {
            assertEquals(expected[i], messages[i].createMessageHash());
        }
    }

    // ---------- Message ID ----------

    @Test
    public void testMessageID_IsCreated() {
        assertTrue(message1.checkMessageID());
        assertEquals("Message ID generated: 0012345678", message1.messageIdCreated());
    }

    @Test
    public void testGeneratedMessageID_IsTenDigits() {
        String id = Message.generateMessageId();
        assertEquals(10, id.length());
        assertTrue(id.matches("\\d{10}"));
    }

    // ---------- SentMessage options ----------

    @Test
    public void testSentMessage_Send() {
        assertEquals("Message successfully sent.", message1.sentMessage(1));
    }

    @Test
    public void testSentMessage_Disregard() {
        assertEquals("Press 0 to delete the message.", message2.sentMessage(2));
    }

    @Test
    public void testSentMessage_Store() {
        assertEquals("Message successfully stored.", message1.sentMessage(3));
    }

    // ---------- Totals and printing ----------

    @Test
    public void testReturnTotalMessages() {
        message1.sentMessage(1);  // sent
        message2.sentMessage(2);  // disregarded - must not count
        assertEquals(1, Message.returnTotalMessages());
    }

    @Test
    public void testPrintMessages_ContainsSentMessage() {
        message1.sentMessage(1);
        String printed = Message.printMessages();
        assertTrue(printed.contains("00:0:HITONIGHT"));
        assertTrue(printed.contains("+27718693002"));
    }
}