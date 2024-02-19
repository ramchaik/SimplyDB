package dev.vaibhavsingh.queryParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsertQueryParser implements SQLParser {
    // Regular expression to match INSERT INTO query pattern
    private static final String INSERT_QUERY_REGEX = "^\\s*INSERT\\s+INTO\\s+(\\w+)\\s*\\((.+)\\)\\s*VALUES\\s*\\((.+)\\);?$";

    @Override
    public boolean isValidQuery(String query) {
        Pattern pattern = Pattern.compile(INSERT_QUERY_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query.trim());
        if (matcher.matches()) {
            String tableName = matcher.group(1);
            String columns = matcher.group(2);
            String values = matcher.group(3);

            // perform further validation on table name, columns, and values
            System.out.println("Table Name: " + tableName);
            System.out.println("Columns: " + columns);
            System.out.println("Values: " + values);

            return true; // For now, we'll just return true if the pattern matches
        } else {
            return false;
        }
    }
}
