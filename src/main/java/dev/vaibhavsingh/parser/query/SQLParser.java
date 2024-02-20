package dev.vaibhavsingh.parser.query;

public interface SQLParser {
    boolean isValidQuery(String query);
    String getTableName(String query);
    String getValues(String query);
}
