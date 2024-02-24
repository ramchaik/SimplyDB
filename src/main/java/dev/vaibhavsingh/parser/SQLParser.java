package dev.vaibhavsingh.parser;

import dev.vaibhavsingh.dto.ParsedSQLQuery;

public interface SQLParser {
    String getQueryType();
    boolean isValidQuery(String query);
    String getTableName(String query);
    String getValues(String query);
    ParsedSQLQuery parse(String query);
}
