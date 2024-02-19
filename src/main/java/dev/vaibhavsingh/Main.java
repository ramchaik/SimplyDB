package dev.vaibhavsingh;

import dev.vaibhavsingh.authentication.*;
import dev.vaibhavsingh.constants.Constants;
import dev.vaibhavsingh.queryParser.SQLParser;
import dev.vaibhavsingh.queryParser.SQLParserFactory;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // Read user input
        Scanner scanner = new Scanner(System.in);

        // Print initial prompt
        System.out.println(Constants.WELCOME_MESSAGE);

        // Auth
        // Instantiate dependencies
        UserAuthenticator userAuthenticator = new FileUserAuthenticator();
        CaptchaGenerator captchaGenerator = new RandomCaptchaGenerator();

        // Instantiate UserAuthenticatorManager
        UserAuthenticatorManager authenticatorManager = new UserAuthenticatorManager(userAuthenticator, captchaGenerator);

        // Initiate the authentication process
        authenticatorManager.init(scanner);

        // TODO: Need to create a session for the user

        // SQL Parser
        while (true) {
            System.out.print("Enter your SQL query (type 'exit' to leave) \n> ");
            String query = scanner.nextLine();

            if (query.equalsIgnoreCase("exit")) {
                break;
            }

            SQLParser parser = SQLParserFactory.getParser(query);
            boolean isValid = parser.isValidQuery(query);

            if (isValid) {
                System.out.println("Query is valid. Executing the query...");
            } else {
                System.out.println("Invalid query. Please enter a valid SQL query.");
            }
        }

        // Close scanner
        scanner.close();
    }


}


