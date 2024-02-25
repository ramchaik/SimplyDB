package dev.vaibhavsingh.authentication;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FileUserAuthenticatorTest {
    FileUserAuthenticator fileUserAuthenticator = new FileUserAuthenticator();

    @Test
    void testAuthenticate() {
        boolean result = fileUserAuthenticator.authenticate("root", "root");
        Assertions.assertEquals(true, result);
    }

    @Test
    void testHashPassword() {
        String result = fileUserAuthenticator.hashPassword("password");
        Assertions.assertEquals("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8", result);
    }
}
