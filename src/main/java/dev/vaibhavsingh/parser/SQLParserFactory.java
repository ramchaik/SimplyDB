package dev.vaibhavsingh.parser;

public class SQLParserFactory {
    public static SQLParser getParser(String query) {
        String queryType = detectQueryType(query);
        switch (queryType) {
            case "SELECT":
                return new SelectQueryParser();
            case "CREATE":
                return new CreateQueryParser();
            case "INSERT":
                return new InsertQueryParser();
            case "TRANSACTION":
                return new TransactionParser();
            case "CREATE DATABASE":
                return new CreateDatabaseParser();
            default:
                throw new IllegalArgumentException("Unsupported query type: " + queryType);
        }
    }

    private static String detectQueryType(String query) {
        // handle transaction queries
        if (query.equalsIgnoreCase("START TRANSACTION") || query.equalsIgnoreCase("BEGIN TRANSACTION") || query.equalsIgnoreCase("COMMIT") || query.equalsIgnoreCase("ROLLBACK")) {
            return "TRANSACTION";
        }

        // handle create database query
        if (query.equalsIgnoreCase("CREATE DATABASE")) {
            return "CREATE DATABASE";
        }

        // Extract the first word as query type
        String[] words = query.trim().split("\\s+");
        return words[0].toUpperCase();
    }
}
