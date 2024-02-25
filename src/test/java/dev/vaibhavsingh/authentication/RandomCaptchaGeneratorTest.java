package dev.vaibhavsingh.authentication;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RandomCaptchaGeneratorTest {
    RandomCaptchaGenerator randomCaptchaGenerator = new RandomCaptchaGenerator();

    @Test
    void testGenerateCaptcha() {
        String result = randomCaptchaGenerator.generateCaptcha();
        Assertions.assertEquals(6, result.length());
        Assertions.assertTrue(result.matches("[a-zA-Z0-9]+"));
    }
}
