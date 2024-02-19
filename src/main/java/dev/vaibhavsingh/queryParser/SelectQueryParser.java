package dev.vaibhavsingh.queryParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectQueryParser implements SQLParser {
    // Regular expression to match SELECT query pattern
    private static final String SELECT_QUERY_REGEX = "^\\s*SELECT\\s+(.+)\\s+FROM\\s+(\\w+)(\\s+WHERE\\s+(.+))?;$";

    @Override
    public boolean isValidQuery(String query) {
        System.out.println("Validating SELECT query");
        Pattern pattern = Pattern.compile(SELECT_QUERY_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query.trim());
        if (matcher.matches()) {
            System.out.println("Select query matched");
            String columns = matcher.group(1);
            System.out.println("Columns: " + columns);
            String tableName = matcher.group(2);
            String whereClause = matcher.group(4); // Optional WHERE clause

            // perform further validation on columns, table name, and where clause
            System.out.println("Columns: " + columns);
            System.out.println("Table Name: " + tableName);
            System.out.println("Where Clause: " + whereClause);

            return true; // For now, we'll just return true if the pattern matches
        } else {
            System.out.println("Select query did not match");
            return false;
        }
    }
}
