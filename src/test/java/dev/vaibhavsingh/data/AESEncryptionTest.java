package dev.vaibhavsingh.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AESEncryptionTest {

    @Test
    void testEncrypt() throws AESEncryption.EncryptionException {
        String result = AESEncryption.encrypt("data", "testkey@12345678");
        Assertions.assertEquals("DOk0FHUEk0c1iSL1rjTy4w==", result);
    }

    @Test
    void testDecrypt() throws AESEncryption.EncryptionException {
        String result = AESEncryption.decrypt("DOk0FHUEk0c1iSL1rjTy4w==", "testkey@12345678");
        Assertions.assertEquals("data", result);
    }
}
