package dev.vaibhavsingh.authentication;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FileUserAuthenticatorTest {
    private final FileUserAuthenticator userAuthenticator = new FileUserAuthenticator();

    @Test
    void authenticate_ValidCredentials_ReturnsTrue() {
        assertTrue(userAuthenticator.authenticate("root", "root"));
    }

    @Test
    void authenticate_InvalidCredentials_ReturnsFalse() {
        assertFalse(userAuthenticator.authenticate("username", "password"));
    }
}
