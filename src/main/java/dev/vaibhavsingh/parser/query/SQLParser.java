package dev.vaibhavsingh.parser.query;

import dev.vaibhavsingh.dto.ParsedSQLQuery;

public interface SQLParser {
    boolean isValidQuery(String query);
    String getTableName(String query);
    String getValues(String query);
    ParsedSQLQuery parse(String query);
}
