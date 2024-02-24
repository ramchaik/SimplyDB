package dev.vaibhavsingh.parser;

import dev.vaibhavsingh.dao.ParsedSQLQuery;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateDatabaseParser implements SQLParser {
    private final  String CREATE_DATABASE_REGEX = "CREATE DATABASE (\\w+)";
    @Override
    public String getQueryType() {
        return "CREATE DATABASE";
    }

    @Override
    public boolean isValidQuery(String query) {
        String regex = "CREATE\\s+DATABASE\\s+(\\w+);";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);
        return matcher.matches();
    }

    @Override
    public String getTableName(String query) {
        return null;
    }

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

    @Override
    public ParsedSQLQuery parse(String query) {
        return null;
    }
}
