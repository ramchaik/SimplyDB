package dev.vaibhavsingh;

import dev.vaibhavsingh.authentication.*;
import dev.vaibhavsingh.constants.Constants;
import dev.vaibhavsingh.controller.QueryProcessor;
import dev.vaibhavsingh.data.DatabaseManager;
import dev.vaibhavsingh.parser.query.CreateQueryParser;
import dev.vaibhavsingh.parser.query.SQLParser;
import dev.vaibhavsingh.parser.query.SQLParserFactory;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DatabaseManager.createDatabase("simplydb");

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

            if (!isValid) {
                System.out.println("Invalid query. Please enter a valid SQL query.");
                System.exit(1);
            } else {
                System.out.println("Query is valid. Executing the query...");
                QueryProcessor processor = new QueryProcessor((CreateQueryParser) parser);
                processor.process(query);
                System.out.println("Query executed successfully.");
            }
        }

        // Close scanner
        scanner.close();
    }


}


