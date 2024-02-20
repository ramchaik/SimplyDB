package dev.vaibhavsingh.parser.query;

import dev.vaibhavsingh.dto.ParsedCreateColumn;
import dev.vaibhavsingh.dto.response.ParsedSQLResponse;

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
        VALID_SQL_DATA_TYPES.add("smallint");
        VALID_SQL_DATA_TYPES.add("bigint");
        VALID_SQL_DATA_TYPES.add("float");
        VALID_SQL_DATA_TYPES.add("double");
        VALID_SQL_DATA_TYPES.add("decimal");
        VALID_SQL_DATA_TYPES.add("numeric");
        VALID_SQL_DATA_TYPES.add("real");
        VALID_SQL_DATA_TYPES.add("date");
        VALID_SQL_DATA_TYPES.add("time");
        VALID_SQL_DATA_TYPES.add("datetime");
        VALID_SQL_DATA_TYPES.add("timestamp");
        VALID_SQL_DATA_TYPES.add("char");
        VALID_SQL_DATA_TYPES.add("varchar");
        VALID_SQL_DATA_TYPES.add("nvarchar");
        VALID_SQL_DATA_TYPES.add("text");
        VALID_SQL_DATA_TYPES.add("blob");
        VALID_SQL_DATA_TYPES.add("clob");
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
        if (matcher.matches()) {
            String tableName = matcher.group(1);
            String columns = matcher.group(2);

            // perform further validation on table name and columns
            System.out.println("Table Name: " + tableName);
            System.out.println("Columns: " + columns);

            return true; // For now, we'll just return true if the pattern matches
        } else {
            return false;
        }
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
     * This method parses the given query and returns a ParsedSQLResponse object
     * @param query SQL query
     * @return parsed SQL response
     */
    public ParsedSQLResponse parse(String query) {
        ArrayList<ParsedCreateColumn> columnList = new ArrayList<>();

        String tableName = getTableName(query);
        String columns = getValues(query);

        String[] columnArray = columns.split(",");
        for (String column : columnArray) {
            column = column.trim();
            String[] columnDetails = column.split(" ");

            String columnName = columnDetails[0];
            String columnType = columnDetails[1];

            if (!isValidSQLDataType(columnType)) {
                throw new IllegalArgumentException("Invalid SQL data type: " + columnType);
            }

            ParsedCreateColumn columnObj = new ParsedCreateColumn(columnName, columnType);

            columnList.add(columnObj);
        }

        ParsedSQLResponse response = new ParsedSQLResponse();

        response.tableName = tableName;
        response.columns = columnList;

        return response;
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


