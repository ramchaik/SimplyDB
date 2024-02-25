package dev.vaibhavsingh.parser;

/**
 * Factory for SQL parser
 */
public class SQLParserFactory {
    /**
     * Returns the parser for the given query
     * @param query the query to be parsed
     * @return the parser for the given query
     */
    public static SQLParser getParser(String query) {
        String queryType = detectQueryType(query);
        switch (queryType) {
            case "CREATE DATABASE":
                return new CreateDatabaseParser();
            case "SELECT":
                return new SelectQueryParser();
            case "CREATE":
                return new CreateQueryParser();
            case "INSERT":
                return new InsertQueryParser();
            case "UPDATE":
                return new UpdateQueryParser();
            case "TRANSACTION":
                return new TransactionParser();
            default:
                throw new IllegalArgumentException("Unsupported query type: " + queryType);
        }
    }

    /**
     * Detects the type of the query
     * @param query the query to be detected
     * @return the type of the query
     */
    private static String detectQueryType(String query) {
        // handle transaction queries
        if (query.toUpperCase().startsWith("START TRANSACTION") || query.toUpperCase().startsWith("BEGIN TRANSACTION") || query.toUpperCase().startsWith("COMMIT") || query.toUpperCase().startsWith("ROLLBACK")) {
            return "TRANSACTION";
        }

        // handle if query starts with "CREATE DATABASE"
        if (query.toUpperCase().startsWith("CREATE DATABASE")) {
            return "CREATE DATABASE";
        }


        // Extract the first word as query type
        String[] words = query.trim().split("\\s+");
        return words[0].toUpperCase();
    }
}
