package dev.vaibhavsingh.parser;

import dev.vaibhavsingh.dao.ParsedColumn;
import dev.vaibhavsingh.dao.ParsedSQLQuery;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateQueryParser implements SQLParser {
    /**
     * Regular expression to match UPDATE query pattern
     */
    private final String UPDATE_QUERY_REGEX = "^\\\\s*UPDATE\\\\s+\\\\w+\\\\s+SET\\\\s+.*\\\\s+WHERE\\\\s+.*;\\\\s*$";

    /**
     * Returns the type of the query
     * @return - the type of the query
     */
    @Override
    public String getQueryType() {
        return "UPDATE";
    }

    /**
     * Validates the query
     * @param query - the query to be validated
     * @return - true if the query is valid, false otherwise
     */
    @Override
    public boolean isValidQuery(String query) {
        Pattern pattern = Pattern.compile(UPDATE_QUERY_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);
        // log each groups of the query
        if (matcher.find()) {
            for (int i = 0; i <= matcher.groupCount(); i++) {
                System.out.println("Group " + i + ": " + matcher.group(i));
            }
        }

        return matcher.matches();

    }

    /**
     * Returns the table name from the query
     * @param query - the query to be parsed
     * @return - the table name
     */
    @Override
    public String getTableName(String query) {
        String tablePattern = "\\bUPDATE\\s+(\\w+)\\b";
        Pattern tablePatternCompiled = Pattern.compile(tablePattern, Pattern.CASE_INSENSITIVE);
        Matcher tableMatcher = tablePatternCompiled.matcher(query);
        return tableMatcher.find() ? tableMatcher.group(1) : null;
    }

    /**
     * Returns the conditions part of the query
     * @param query - the query to be parsed
     * @return - the conditions part of the query
     */
    public String getConditions(String query) {
        String conditionsPattern = "\\bWHERE\\s+(.*?)\\s*;\\s*$";
        Pattern conditionsPatternCompiled = Pattern.compile(conditionsPattern, Pattern.CASE_INSENSITIVE);
        Matcher conditionsMatcher = conditionsPatternCompiled.matcher(query);
        return conditionsMatcher.find() ? conditionsMatcher.group(1) : null;
    }

    /**
     * Returns the values part of the query
     * @param query - the query to be parsed
     * @return - the values part of the query
     */
    @Override
    public String getValues(String query) {
        String valuesPattern = "\\bSET\\s+(.*?)\\s+WHERE\\b";
        Pattern valuesPatternCompiled = Pattern.compile(valuesPattern, Pattern.CASE_INSENSITIVE);
        Matcher valuesMatcher = valuesPatternCompiled.matcher(query);
        return valuesMatcher.find() ? valuesMatcher.group(1) : null;
    }

    /**
     * Returns the where clause from the query
     * @param query - the query to be parsed
     * @return - the where clause from the query
     */
    public String getWhereClause(String query) {
        // match and return the values
        Pattern pattern = Pattern.compile(UPDATE_QUERY_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);
        if (matcher.find()) {
            return matcher.group(3);
        }
        return null;
    }

    /**
     * Parses the query and returns the parsed query
     * @param query - the query to be parsed
     * @return - the parsed query
     */
    @Override
    public ParsedSQLQuery parse(String query) {
        ParsedUpdateQuery parsedSQLQuery = new ParsedUpdateQuery();
//        parsedSQLQuery.tableName = getTableName(query);

//        String values = getValues(query);
//        String[] valuePairs = values.split(",");
//
//        // get conditions
//        String conditions = getConditions(query);
//        String[] conditionPairs = conditions.split("AND");
//
//        // parse the values
//        for (String valuePair : valuePairs) {
//            String[] value = valuePair.split("=");
//            ParsedColumn parsedColumn = new ParsedColumn(value[0].trim(), null, value[1].trim());
//            parsedSQLQuery.columns.add(parsedColumn);
//        }
//
//
//        for (String valuePair : valuePairs) {
//            String[] value = valuePair.split("=");
//            ParsedColumn parsedColumn = new ParsedColumn(value[0].trim(), null, value[1].trim());
//            parsedSQLQuery.columns.add(parsedColumn);
//        }

//        return parsedSQLQuery;
        return null;
    }

    class ParsedUpdateQuery {
        String tableName;
        String values;
        String conditions;
    }

    public ParsedSQLQuery parseWhereClause(String query) {
        ParsedSQLQuery parsedSQLQuery = new ParsedSQLQuery();
        parsedSQLQuery.tableName = getTableName(query);

        String whereClause = getWhereClause(query);
        String[] wherePairs = whereClause.split("AND");

        for (String wherePair : wherePairs) {
            String[] where = wherePair.split("=");
            ParsedColumn parsedColumn = new ParsedColumn(where[0].trim(), where[1].trim(), null);
            parsedSQLQuery.columns.add(parsedColumn);
        }

        return parsedSQLQuery;
    }
}
