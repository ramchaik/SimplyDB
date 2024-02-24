package dev.vaibhavsingh.parser;

import dev.vaibhavsingh.dto.ParsedSQLQuery;

public class TransactionParser implements SQLParser {
    /**
     * @return
     */
    @Override
    public String getQueryType() {
        return "TRANSACTION";
    }

    /**
     * @param query
     * @return
     */
    @Override
    public boolean isValidQuery(String query) {
        return true;
    }

    /**
     * @param query
     * @return
     */
    @Override
    public String getTableName(String query) {
        return null;
    }

    /**
     * @param query
     * @return
     */
    @Override
    public String getValues(String query) {
        return null;
    }

    /**
     * @param query
     * @return
     */
    @Override
    public ParsedSQLQuery parse(String query) {
        return null;
    }
}
