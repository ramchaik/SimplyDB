package dev.vaibhavsingh.queryParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateQueryParser implements SQLParser {
    // Regular expression to match CREATE TABLE query pattern
    private static final String CREATE_TABLE_REGEX = "^\\s*CREATE\\s+TABLE\\s+(\\w+)\\s*\\((.+)\\);?$";

    @Override
    public boolean isValidQuery(String query) {
        Pattern pattern = Pattern.compile(CREATE_TABLE_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query.trim());
        if (matcher.matches()) {
            String tableName = matcher.group(1);
            String columns = matcher.group(2);

            // Here you can perform further validation on table name and columns
            System.out.println("Table Name: " + tableName);
            System.out.println("Columns: " + columns);

            return true; // For now, we'll just return true if the pattern matches
        } else {
            return false;
        }
    }
}
