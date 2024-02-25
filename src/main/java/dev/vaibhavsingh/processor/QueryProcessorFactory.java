package dev.vaibhavsingh.processor;

import dev.vaibhavsingh.parser.SQLParser;

public class QueryProcessorFactory {
    /**
     * Returns the query processor based on the query type
     * @param parser the SQL parser
     * @return the query processor
     */
    public static QueryProcessor getQueryProcessor(SQLParser parser) {
        String queryType = parser.getQueryType();
        switch (queryType) {
            case "SELECT":
                return new SelectQueryProcessor(parser);
            case "INSERT":
                return new InsertQueryProcessor(parser);
            case "CREATE":
                return new CreateQueryProcessor(parser);
            case "UPDATE":
                return new UpdateQueryProcessor(parser);
            case "TRANSACTION":
                return new TransactionProcessor();
            case "CREATE DATABASE":
                return new CreateDatabaseProcessor(parser);
            default:
                throw new IllegalArgumentException("Invalid query type: " + queryType);
        }
    }
}
