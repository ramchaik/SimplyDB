package dev.vaibhavsingh.utils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * This class provides encryption and decryption functionalities using AES encryption algorithm.
 */
public class AESEncryption {

    /**
     * Encrypts the given data using AES encryption.
     *
     * @param data The data to be encrypted.
     * @param key  The encryption key.
     * @return The encrypted data as a Base64-encoded string.
     * @throws EncryptionException If an error occurs during encryption.
     */
    public static String encrypt(String data, String key) throws EncryptionException {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException |
                 IllegalBlockSizeException e) {
            throw new EncryptionException("Error occurred during encryption: " + e.getMessage());
        }
    }

    /**
     * Decrypts the given data using AES decryption.
     *
     * @param encryptedData The encrypted data to be decrypted.
     * @param key           The encryption key.
     * @return The decrypted data.
     * @throws EncryptionException If an error occurs during decryption.
     */
    public static String decrypt(String encryptedData, String key) throws EncryptionException {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException |
                 IllegalBlockSizeException e) {
            throw new EncryptionException("Error occurred during decryption: " + e.getMessage());
        }
    }

    /**
     * Custom exception class for encryption-related errors.
     */
    public static class EncryptionException extends Exception {
        public EncryptionException(String message) {
            super(message);
        }
    }
}
