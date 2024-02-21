package dev.vaibhavsingh.parser.query;

import dev.vaibhavsingh.dto.ParsedColumn;
import dev.vaibhavsingh.dto.ParsedSQLQuery;

import java.util.ArrayList;
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

    @Override
    public ParsedSQLQuery parse(String query) {
        ArrayList<ParsedColumn> columnList = new ArrayList<>();

        System.out.println("Parsing SELECT query");
        Pattern pattern = Pattern.compile(SELECT_QUERY_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query.trim());

        if (matcher.matches()) {
            String columns = matcher.group(1);
            String tableName = matcher.group(2);

            // Optional WHERE clause; should we handle this?
            String whereClause = matcher.group(4);

           for (String column : columns.split(",")) {
                ParsedColumn parsedColumn = new ParsedColumn(column, null, null);
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
}
