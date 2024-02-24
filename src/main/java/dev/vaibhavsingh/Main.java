package dev.vaibhavsingh;

import dev.vaibhavsingh.authentication.*;
import dev.vaibhavsingh.constants.Constants;
import dev.vaibhavsingh.constants.DatabaseConstants;
import dev.vaibhavsingh.processor.CreateQueryProcessor;
import dev.vaibhavsingh.data.DatabaseManager;
import dev.vaibhavsingh.data.TableManager;
import dev.vaibhavsingh.parser.SQLParser;
import dev.vaibhavsingh.parser.SQLParserFactory;
import dev.vaibhavsingh.processor.QueryProcessor;
import dev.vaibhavsingh.processor.QueryProcessorFactory;

import javax.management.Query;
import java.util.Date;
import java.util.Scanner;

public class Main {
    private static void init() {
        try {
            DatabaseManager.createDatabase("admin");
            TableManager.createTable("admin", "users", new String[]{"username", "password"}, new String[]{"varchar(255)", "varchar(255)"});
            TableManager.createTable("admin", "sessions_log", new String[]{"username", "query", "timestamp"}, new String[]{"varchar(255)", "varchar(255)", "varchar(255)"});
            FileUserAuthenticator fileUserAuthenticator = new FileUserAuthenticator();
            String hashedPassword = fileUserAuthenticator.hashPassword("root");
            TableManager.insertIntoTable("admin", "users", new String[]{"root", hashedPassword});
        } catch (Exception e) {
            System.out.println("Database already exists.");
        }
    }

    private static void handleUserAuth(Scanner scanner) {
        // Instantiate dependencies
        UserAuthenticator userAuthenticator = new FileUserAuthenticator();
        CaptchaGenerator captchaGenerator = new RandomCaptchaGenerator();

        // Instantiate UserAuthenticatorManager
        UserAuthenticatorManager authenticatorManager = new UserAuthenticatorManager(userAuthenticator, captchaGenerator);

        // Initiate the authentication process
        authenticatorManager.init(scanner);
    }

    public static void main(String[] args) {
        // setup admin database
        init();

        // Create a database
        String databaseName = DatabaseConstants.DATABASE_NAME;
        DatabaseManager.createDatabase(databaseName);
        DatabaseManager.setCurrentDatabase(databaseName);

        System.out.println("Using Database '" + DatabaseManager.currentDatabase + "'");

        // Read user input
        Scanner scanner = new Scanner(System.in);

        // Print initial prompt
        System.out.println(Constants.WELCOME_MESSAGE);

        // Auth
        handleUserAuth(scanner);

        while (true) {
            try {
                // Read user input
                System.out.print("\nEnter your SQL query (type 'exit' to leave) \n> ");
                String query = scanner.nextLine();

                if (query.equalsIgnoreCase("exit")) {
                    break;
                }

                processQuery(query);
            } catch (Exception e) {
                System.out.println("An error occurred while processing the query: " + e.getMessage());
            }
        }

        // Close scanner
        scanner.close();
    }

    public static void processQuery(String query) {
        SQLParser parser = SQLParserFactory.getParser(query);
        boolean isValid = parser.isValidQuery(query);

        if (!isValid) {
            System.out.println("Invalid query. Please enter a valid SQL query.");
        } else {
//                    System.out.println("Query is valid. Executing the query...");
            QueryProcessor processor = QueryProcessorFactory.getQueryProcessor(parser);
            processor.process(query);
        }
    }
}


