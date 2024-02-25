package dev.vaibhavsingh.parser;

import dev.vaibhavsingh.dao.ParsedSQLQuery;

public class TransactionParser implements SQLParser {
    /**
     * Returns the type of the query
     * @return the type of the query
     */
    @Override
    public String getQueryType() {
        return "TRANSACTION";
    }

    /**
     * Validates the query
     * @param query the query to be validated
     * @return true if the query is valid, false otherwise
     */
    @Override
    public boolean isValidQuery(String query) {
        return true;
    }

    /**
     * Returns the table name from the query
     * @param query the query to be parsed
     * @return the table name
     */
    @Override
    public String getTableName(String query) {
        return null;
    }

    /**
     * Returns the values from the query
     * @param query the query to be parsed
     * @return the values from the query
     */
    @Override
    public String getValues(String query) {
        return null;
    }

    /**
     * Returns the conditions from the query
     * @param query the query to be parsed
     * @return the conditions from the query
     */
    @Override
    public ParsedSQLQuery parse(String query) {
        return null;
    }
}
