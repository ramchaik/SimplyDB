package dev.vaibhavsingh.queryParser;

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
            default:
                throw new IllegalArgumentException("Unsupported query type: " + queryType);
        }
    }

    private static String detectQueryType(String query) {
        String[] words = query.trim().split("\\s+");
        return words[0].toUpperCase(); // Extract the first word as query type
    }
}
