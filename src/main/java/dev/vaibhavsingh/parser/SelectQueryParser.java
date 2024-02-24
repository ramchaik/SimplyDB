package dev.vaibhavsingh.parser;

import dev.vaibhavsingh.dao.ParsedColumn;
import dev.vaibhavsingh.dao.ParsedSQLQuery;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectQueryParser implements SQLParser {
    // Regular expression to match SELECT query pattern
    private static final String SELECT_QUERY_REGEX = "^\\s*SELECT\\s+(.+)\\s+FROM\\s+(\\w+)(\\s+WHERE\\s+(.+))?;$";

    /**
     * This method returns the type of the SQL query
     * @return type of the SQL query
     */
    @Override
    public String getQueryType() {
        return "SELECT";
    }

    @Override
    public boolean isValidQuery(String query) {
        Pattern pattern = Pattern.compile(SELECT_QUERY_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query.trim());
        return matcher.matches();
    }

    @Override
    public String getTableName(String query) {
        Pattern pattern = Pattern.compile(SELECT_QUERY_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query.trim());
        if (matcher.matches()) {
            return matcher.group(2);
        } else {
            return null;
        }
    }

    @Override
    public String getValues(String query) {
        Pattern pattern = Pattern.compile(SELECT_QUERY_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query.trim());
        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    /**
     * This method parses the given SQL query and returns a ParsedSQLQuery object
     * @param query SQL query
     * @return ParsedSQLQuery object
     */
    @Override
    public ParsedSQLQuery parse(String query) {
        ArrayList<ParsedColumn> columnList = new ArrayList<>();

        Pattern pattern = Pattern.compile(SELECT_QUERY_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query.trim());

        if (matcher.matches()) {
            String columns = matcher.group(1);
            String tableName = matcher.group(2);

            // Optional WHERE clause; should we handle this?
            String whereClause = matcher.group(4);

           for (String column : columns.split(",")) {
                ParsedColumn parsedColumn = new ParsedColumn(column.trim(), null, null);
                columnList.add(parsedColumn);
            }

            ParsedSQLQuery parsedQuery = new ParsedSQLQuery();

            parsedQuery.tableName = tableName;
            parsedQuery.columns = columnList;

            return parsedQuery;
        } else {
            return null;
        }
    }

    public String getWhereClause(String query) {
        Pattern pattern = Pattern.compile(SELECT_QUERY_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query.trim());
        if (matcher.matches()) {
            return matcher.group(4);
        } else {
            return null;
        }
    }

    public ArrayList<ParsedColumn> parseWhere(String query) {
        String whereClause = getWhereClause(query);
        if (whereClause == null) {
            return null;
        }

        ArrayList<ParsedColumn> parsedColumns = new ArrayList<>();
        String[] conditions = whereClause.split("AND|OR");
        for (String condition : conditions) {
            String[] parts = condition.split("=|<|>|<=|>=");
            String columnName = parts[0].trim();
            String value = parts[1].trim();
            String operator = condition.replaceAll("[^=<>]", "");

            ParsedColumn parsedColumn = new ParsedColumn(columnName, operator, value);
            parsedColumns.add(parsedColumn);
        }

        return parsedColumns;
    }


}
