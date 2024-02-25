package dev.vaibhavsingh.authentication;

import java.util.Random;

/**
 * Generates random CAPTCHA codes.
 */
public class RandomCaptchaGenerator implements CaptchaGenerator {
    /**
     * Generates a random CAPTCHA code.
     *
     * @return CAPTCHA code.
     */
    @Override
    public String generateCaptcha() {
        // Define the characters to use for the CAPTCHA code
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        // Length of the CAPTCHA code
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
