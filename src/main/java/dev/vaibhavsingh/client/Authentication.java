package dev.vaibhavsingh.client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Scanner;

public class Authentication {
    private static final String USER_FILE = "/Users/lib-user/vaibhav/CSCI5408/Project/Assignment1/simple-dbms/src/main/java/dev/vaibhavsingh/data/admin/user/users.txt";

    public static void init(Scanner scanner) {
        String captchaCode = generateCaptcha();

        // Print initial prompt
        System.out.println("================================================== ");
        System.out.println("Hello there! 👋 Welcome to SimplyDB! 🎉");
        System.out.println("================================================== ");
        System.out.println("Please enter your username, password, and the following CAPTCHA code: " + captchaCode);

        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter CAPTCHA: ");
        String userInputCaptcha = scanner.nextLine();

        // Validate CAPTCHA
        if (userInputCaptcha.equals(captchaCode)) {
            // Check username and password
            if (authenticate(username, password)) {
                System.out.println("Authentication successful!");
            } else {
                System.out.println("Authentication failed: Invalid username or password.");
                System.exit(1);
            }
        } else {
            System.out.println("CAPTCHA validation failed. Authentication failed!");
            System.exit(1);
        }
    }

    private static boolean authenticate(String username, String password) {
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

    private static String hashPassword(String password) {
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
     * Covert bytes to hex
     *
     * @param bytes
     * @return Hex string
     */
    private static String bytesToHex(byte[] bytes) {
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

    // Method to generate a random CAPTCHA code
    private static String generateCaptcha() {
        // Define the characters to use for the CAPTCHA code
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        // Set the length of the CAPTCHA code
        int captchaLength = 6;

        // Generate the random CAPTCHA code
        Random random = new Random();
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < captchaLength; i++) {
            int index = random.nextInt(allowedChars.length());
            captcha.append(allowedChars.charAt(index));
        }

        return captcha.toString();
    }
}
