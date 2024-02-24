package dev.vaibhavsingh.authentication;

import java.util.Scanner;

/**
 * Handles user authentication.
 */
public class UserAuthenticatorManager {
    private final UserAuthenticator userAuthenticator;
    private final CaptchaGenerator captchaGenerator;

    /**
     * Initializes the Authentication instance with required collaborators.
     *
     * @param userAuthenticator Handles user authentication.
     * @param captchaGenerator  Generates CAPTCHA codes.
     */
    public UserAuthenticatorManager(UserAuthenticator userAuthenticator, CaptchaGenerator captchaGenerator) {
        this.userAuthenticator = userAuthenticator;
        this.captchaGenerator = captchaGenerator;
    }

    /**
     * Initiates the authentication process.
     *
     * @param scanner Scanner object to read user input.
     */
    public void init(Scanner scanner) {
        String captchaCode = captchaGenerator.generateCaptcha();

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
            if (userAuthenticator.authenticate(username, password)) {
                System.out.println("Login successful!");
            } else {
                System.out.println("Login failed: Invalid username or password.");
                System.exit(1);
            }
        } else {
            System.out.println("CAPTCHA validation failed. Login failed!");
            System.exit(1);
        }
    }
}
