package dev.vaibhavsingh.authentication;

/**
 * Generates CAPTCHA codes.
 */
public interface CaptchaGenerator {
    /**
     * Generates a random CAPTCHA code.
     *
     * @return CAPTCHA code.
     */
    String generateCaptcha();
}
