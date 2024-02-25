package dev.vaibhavsingh.parser;

import dev.vaibhavsingh.dao.ParsedSQLQuery;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateDatabaseParser implements SQLParser {
    /**
     * Regular expression to match CREATE DATABASE query pattern
     */
    private final  String CREATE_DATABASE_REGEX = "CREATE DATABASE (\\w+)";

    /**
     * Returns the type of the query
     * @return the type of the query
     */
    @Override
    public String getQueryType() {
        return "CREATE DATABASE";
    }

    /**
     * Validates the query
     * @param query the query to be validated
     * @return true if the query is valid, false otherwise
     */
    @Override
    public boolean isValidQuery(String query) {
        String regex = "CREATE\\s+DATABASE\\s+(\\w+);";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);
        return matcher.matches();
    }

    /**
     * Returns the table name from the query
     * @param query the query to be parsed
     * @return the table name
     */
    @Override
    public String getTableName(String query) {
        return null;
    }

    /**
     * Returns the values from the query
     * @param query the query to be parsed
     * @return the values from the query
     */
    @Override
    public String getValues(String query) {
        // Extract the database name from the query
        Pattern pattern = Pattern.compile(CREATE_DATABASE_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);
        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    /**
     * Returns the conditions from the query
     * @param query the query to be parsed
     * @return the conditions from the query
     */
    @Override
    public ParsedSQLQuery parse(String query) {
        return null;
    }
}
