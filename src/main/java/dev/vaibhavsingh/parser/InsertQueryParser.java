package dev.vaibhavsingh.parser;

import dev.vaibhavsingh.dao.ParsedColumn;
import dev.vaibhavsingh.dao.ParsedSQLQuery;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsertQueryParser implements SQLParser {
    /**
     * Regular expression to match INSERT INTO query pattern
     */
    private static final String INSERT_QUERY_REGEX = "^\\s*INSERT\\s+INTO\\s+(\\w+)\\s*\\((.+)\\)\\s*VALUES\\s*\\((.+)\\);?$";

    /**
     * This method returns the type of the SQL query
     * @return type of the SQL query
     */
    @Override
    public String getQueryType() {
        return "INSERT";
    }

    /**
     * This method checks if the given query is a valid INSERT INTO query
     * @param query SQL query
     * @return true if the query is valid, false otherwise
     */
    @Override
    public boolean isValidQuery(String query) {
        Pattern pattern = Pattern.compile(INSERT_QUERY_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query.trim());
        return matcher.matches();
    }

    /**
     * Returns the values from the query
     * @param query the query to be parsed
     * @return the values from the query
     */
    @Override
    public String getValues(String query) {
        Pattern pattern = Pattern.compile(INSERT_QUERY_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query.trim());
        if (matcher.matches()) {
            return matcher.group(3);
        } else {
            return null;
        }
    }

    /**
     * get columns from query
     * @param query SQL query
     * @return columns
     */
    public String getColumns(String query) {
        Pattern pattern = Pattern.compile(INSERT_QUERY_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query.trim());
        if (matcher.matches()) {
            return matcher.group(2);
        } else {
            return null;
        }
    }

    /**
     * get table name from query
     * @param query SQL query
     * @return table name
     */
    @Override
    public String getTableName(String query) {
        Pattern pattern = Pattern.compile(INSERT_QUERY_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query.trim());
        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    /**
     * This method parses the given query and returns a ParsedSQLQuery object
     * @param query SQL query
     * @return parsed SQL response
     */
    @Override
    public ParsedSQLQuery parse(String query) {
        ArrayList<ParsedColumn> columnList = new ArrayList<>();

        String tableName = getTableName(query);
        String[] values = getValues(query).split(",");
        String[] columns = getColumns(query).split(",");

        for (int i = 0; i < columns.length; i++) {
            String columnName = columns[i].trim();
            String value = values[i].replaceAll("^\\s*['\"]\\s*|\\s*['\"]\\s*$", "");;

            ParsedColumn columnObj = new ParsedColumn(columnName, null, value);

            columnList.add(columnObj);
        }

        ParsedSQLQuery parsedSQLQuery = new ParsedSQLQuery();

        parsedSQLQuery.tableName = tableName;
        parsedSQLQuery.columns = columnList;

        return parsedSQLQuery;
    }
}
