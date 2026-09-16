import com.bonolo.poe.prog5121_p1.Login;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoginTest {

    private Login login;

    @BeforeEach
    public void setUp() {
        // A valid Login object reused for tests that need one
        login = new Login("Kyle", "Smith", "kyl_1", "Ch3ck!234", "+27838968976");
    }

    // ---------- a) checkUserName ----------

    @Test
    public void testCheckUserName_ValidUsername() {
        Login validLogin = new Login("Kyle", "Smith", "kyl_1", "Ch3ck!234", "+27838968976");
        assertTrue(validLogin.checkUserName());
    }

    @Test
    public void testCheckUserName_NoUnderscore() {
        Login invalidLogin = new Login("Kyle", "Smith", "kyle1", "Ch3ck!234", "+27838968976");
        assertFalse(invalidLogin.checkUserName());
    }

    @Test
    public void testCheckUserName_TooLong() {
        Login invalidLogin = new Login("Kyle", "Smith", "kyl_12345", "Ch3ck!234", "+27838968976");
        assertFalse(invalidLogin.checkUserName());
    }

    // ---------- b) checkPasswordComplexity ----------

    @Test
    public void testCheckPasswordComplexity_Valid() {
        Login validLogin = new Login("Kyle", "Smith", "kyl_1", "Ch3ck!234", "+27838968976");
        assertTrue(validLogin.checkPasswordComplexity());
    }

    @Test
    public void testCheckPasswordComplexity_TooShort() {
        Login invalidLogin = new Login("Kyle", "Smith", "kyl_1", "Ch3!12", "+27838968976");
        assertFalse(invalidLogin.checkPasswordComplexity());
    }

    @Test
    public void testCheckPasswordComplexity_NoCapital() {
        Login invalidLogin = new Login("Kyle", "Smith", "kyl_1", "ch3ck!234", "+27838968976");
        assertFalse(invalidLogin.checkPasswordComplexity());
    }

    @Test
    public void testCheckPasswordComplexity_NoNumber() {
        Login invalidLogin = new Login("Kyle", "Smith", "kyl_1", "Check!abc", "+27838968976");
        assertFalse(invalidLogin.checkPasswordComplexity());
    }

    @Test
    public void testCheckPasswordComplexity_NoSpecialChar() {
        Login invalidLogin = new Login("Kyle", "Smith", "kyl_1", "Check1234", "+27838968976");
        assertFalse(invalidLogin.checkPasswordComplexity());
    }

    // ---------- c) checkCellPhoneNumber ----------

    @Test
    public void testCheckCellPhoneNumber_Valid() {
        Login validLogin = new Login("Kyle", "Smith", "kyl_1", "Ch3ck!234", "+27838968976");
        assertTrue(validLogin.checkCellPhoneNumber());
    }

    @Test
    public void testCheckCellPhoneNumber_MissingInternationalCode() {
        Login invalidLogin = new Login("Kyle", "Smith", "kyl_1", "Ch3ck!234", "0838968976");
        assertFalse(invalidLogin.checkCellPhoneNumber());
    }

    @Test
    public void testCheckCellPhoneNumber_TooManyDigits() {
        Login invalidLogin = new Login("Kyle", "Smith", "kyl_1", "Ch3ck!234", "+278389689761234");
        assertFalse(invalidLogin.checkCellPhoneNumber());
    }

    // ---------- d) registerUser ----------

    @Test
    public void testRegisterUser_Success() {
        Login validLogin = new Login("Kyle", "Smith", "kyl_1", "Ch3ck!234", "+27838968976");
        String result = validLogin.registerUser();
        assertEquals(
            "Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.",
            result
        );
    }

    @Test
    public void testRegisterUser_InvalidUsername() {
        Login invalidLogin = new Login("Kyle", "Smith", "kyle1", "Ch3ck!234", "+27838968976");
        String result = invalidLogin.registerUser();
        assertEquals(
            "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.",
            result
        );
    }

    @Test
    public void testRegisterUser_InvalidPassword() {
        Login invalidLogin = new Login("Kyle", "Smith", "kyl_1", "weakpass", "+27838968976");
        String result = invalidLogin.registerUser();
        assertEquals(
            "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.",
            result
        );
    }

    @Test
    public void testRegisterUser_InvalidCellNumber() {
        Login invalidLogin = new Login("Kyle", "Smith", "kyl_1", "Ch3ck!234", "0838968976");
        String result = invalidLogin.registerUser();
        assertEquals(
            "Cell phone number incorrectly formatted or does not contain international code.",
            result
        );
    }

    // ---------- e) loginUser ----------

    @Test
    public void testLoginUser_CorrectCredentials() {
        assertTrue(login.loginUser("kyl_1", "Ch3ck!234"));
    }

    @Test
    public void testLoginUser_IncorrectUsername() {
        assertFalse(login.loginUser("wrong_1", "Ch3ck!234"));
    }

    @Test
    public void testLoginUser_IncorrectPassword() {
        assertFalse(login.loginUser("kyl_1", "WrongPass1!"));
    }

    // ---------- f) returnLoginStatus ----------

    @Test
    public void testReturnLoginStatus_LoggedIn() {
        String result = login.returnLoginStatus(true);
        assertEquals("Welcome Kyle, Smith it is great to see you again.", result);
    }

    @Test
    public void testReturnLoginStatus_NotLoggedIn() {
        String result = login.returnLoginStatus(false);
        assertEquals("Username or password incorrect, please try again.", result);
    }
}