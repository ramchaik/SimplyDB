package dev.vaibhavsingh.processor;

import dev.vaibhavsingh.data.DatabaseManager;
import dev.vaibhavsingh.parser.SQLParser;

/**
 * Processor for CREATE DATABASE query
 */
public class CreateDatabaseProcessor implements QueryProcessor {
    /**
     * The SQL parser
     */
    SQLParser parser;
    /**
     * Constructor for CreateDatabaseProcessor
     * @param parser the SQL parser
     */
    public CreateDatabaseProcessor(SQLParser parser) {
        this.parser = parser;
    }

    /**
     * Processes the CREATE DATABASE query
     * @param query the query to be processed
     * @return true if the query is processed successfully, false otherwise
     */
    @Override
    public boolean process(String query) {
        String databaseName = parser.getValues(query);
        // Create the database
        DatabaseManager.createDatabase(databaseName);
        return true;
    }
}
