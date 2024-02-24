package dev.vaibhavsingh.data;

import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AESEncryptionTest {

    @Test
    public void testEncryptionAndDecryption() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, AESEncryption.EncryptionException {
        // Test data and key
        String originalData = "Hello, World!";
        String key = "MySecretKey12345";

        // Encrypt the data
        String encryptedData = AESEncryption.encrypt(originalData, key);

        // Decrypt the encrypted data
        String decryptedData = AESEncryption.decrypt(encryptedData, key);

        // Check if the decrypted data matches the original data
        assertEquals(originalData, decryptedData);
    }

    @Test
    public void testEncryptionWithInvalidKey() {
        // Test data and invalid key
        String data = "Hello";
        String invalidKey = "";

        // Check if InvalidKeyException is thrown when encrypting with an empty key
        assertThrows(IllegalArgumentException.class, () -> AESEncryption.encrypt(data, invalidKey));
    }

    @Test
    public void testDecryptionWithInvalidKey() {
        // Test data and invalid key
        String encryptedData = "encryptedData";
        String invalidKey = "";

        // Check if InvalidKeyException is thrown when decrypting with an empty key
        assertThrows(IllegalArgumentException.class, () -> AESEncryption.decrypt(encryptedData, invalidKey));
    }

    // Add more test cases as needed
}
