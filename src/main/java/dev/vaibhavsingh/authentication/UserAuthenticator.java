package dev.vaibhavsingh.authentication;

/**
 * Handles user authentication.
 */
public interface UserAuthenticator {
    /**
     * Authenticates user with given credentials.
     *
     * @param username User's username.
     * @param password User's password.
     * @return True if authentication is successful, false otherwise.
     */
    boolean authenticate(String username, String password);
}
