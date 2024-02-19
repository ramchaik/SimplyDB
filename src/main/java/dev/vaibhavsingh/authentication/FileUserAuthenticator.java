package dev.vaibhavsingh.authentication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Authenticates users against a file-based user database.
 */
public class FileUserAuthenticator implements UserAuthenticator {
    private static final String USER_FILE = "/Users/lib-user/vaibhav/CSCI5408/Project/Assignment1/simple-dbms/src/main/java/dev/vaibhavsingh/data/admin/user/users.txt";

    /**
     * Authenticates a user with the given username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return True if the user is authenticated, false otherwise.
     */
    @Override
    public boolean authenticate(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].equals(username)) {
                    String hashedPassword = hashPassword(password);
                    return hashedPassword.equals(parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Hashes the given password.
     *
     * @param password The password to hash.
     * @return The hashed password.
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts a byte array to a hexadecimal string.
     *
     * @param bytes The byte array to convert.
     * @return The hexadecimal string.
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
