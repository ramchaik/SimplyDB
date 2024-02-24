package dev.vaibhavsingh.parser;

import dev.vaibhavsingh.dto.ParsedColumn;
import dev.vaibhavsingh.dto.ParsedSQLQuery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateQueryParser implements SQLParser {
    // Regular expression to match CREATE TABLE query pattern
    private static final String CREATE_TABLE_REGEX = "^\\s*CREATE\\s+TABLE\\s+(\\w+)\\s*\\((.+)\\);?$";

    private static final Set<String> VALID_SQL_DATA_TYPES = new HashSet<>();

    static {
        // Add valid SQL data types to the set
        VALID_SQL_DATA_TYPES.add("int");
        VALID_SQL_DATA_TYPES.add("integer");
        VALID_SQL_DATA_TYPES.add("double");
        VALID_SQL_DATA_TYPES.add("date");
        VALID_SQL_DATA_TYPES.add("time");
        VALID_SQL_DATA_TYPES.add("datetime");
        VALID_SQL_DATA_TYPES.add("timestamp");
        VALID_SQL_DATA_TYPES.add("char");
        VALID_SQL_DATA_TYPES.add("varchar");
        VALID_SQL_DATA_TYPES.add("text");
    }

    /**
     * This method returns the type of the SQL query
     * @return type of the SQL query
     */
    @Override
    public String getQueryType() {
        return "CREATE";
    }

    /**
     * This method checks if the given query is a valid CREATE TABLE query
     * @param query SQL query
     * @return true if the query is valid, false otherwise
     */
    @Override
    public boolean isValidQuery(String query) {
        Pattern pattern = Pattern.compile(CREATE_TABLE_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query.trim());
        return matcher.matches();
    }

    /**
     * get table name from query
     * @param query SQL query
     * @return table name
     */
    public String getTableName(String query) {
        Pattern pattern = Pattern.compile(CREATE_TABLE_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query.trim());
        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    /**
     * get column values from query
     * @param query SQL query
     * @return column values
     */
    public String getValues(String query) {
        Pattern pattern = Pattern.compile(CREATE_TABLE_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query.trim());
        if (matcher.matches()) {
            return matcher.group(2);
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
        String columns = getValues(query);

        if (tableName == null || columns == null) {
            System.out.println("Table Name" + tableName);
            System.out.println("Columns" + columns);
            throw new IllegalArgumentException("Invalid CREATE TABLE query: " + query);
        }

        String[] columnArray = columns.split(",");
        for (String column : columnArray) {
            column = column.trim();
            String[] columnDetails = column.split(" ");

            String columnName = columnDetails[0];
            String columnType = columnDetails[1];

            if (!isValidSQLDataType(columnType)) {
                throw new IllegalArgumentException("Invalid SQL data type: " + columnType);
            }

            ParsedColumn columnObj = new ParsedColumn(columnName, columnType, null);

            columnList.add(columnObj);
        }

        ParsedSQLQuery parsedSQLQuery = new ParsedSQLQuery();

        parsedSQLQuery.tableName = tableName;
        parsedSQLQuery.columns = columnList;

        return parsedSQLQuery;
    }

    /**
     * Validates if a given SQL data type is valid.
     *
     * @param sqlDataType The SQL data type to validate.
     * @return true if the SQL data type is valid, false otherwise.
     */
    public static boolean isValidSQLDataType(String sqlDataType) {
        return VALID_SQL_DATA_TYPES.contains(sqlDataType.toLowerCase());
    }
}


