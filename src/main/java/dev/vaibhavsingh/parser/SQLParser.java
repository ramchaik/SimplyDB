package dev.vaibhavsingh.parser;

import dev.vaibhavsingh.dao.ParsedSQLQuery;

/**
 * Interface for SQL Parser
 */
public interface SQLParser {
    /**
     * Returns the type of the query
     * @return the type of the query
     */
    String getQueryType();
    /**
     * Validates the query
     * @param query the query to be validated
     * @return true if the query is valid, false otherwise
     */
    boolean isValidQuery(String query);
    /**
     * Returns the table name from the query
     * @param query the query to be parsed
     * @return the table name
     */
    String getTableName(String query);
    /**
     * Returns the values from the query
     * @param query the query to be parsed
     * @return the values from the query
     */
    String getValues(String query);
    /**
     * Returns the conditions from the query
     * @param query the query to be parsed
     * @return the conditions from the query
     */
    ParsedSQLQuery parse(String query);
}
