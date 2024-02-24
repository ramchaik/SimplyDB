package dev.vaibhavsingh.processor;

import dev.vaibhavsingh.data.DatabaseManager;
import dev.vaibhavsingh.parser.SQLParser;

public class CreateDatabaseProcessor implements QueryProcessor {
    SQLParser parser;
    public CreateDatabaseProcessor(SQLParser parser) {
        this.parser = parser;
    }

    /**
     * @param query
     * @return
     */
    @Override
    public boolean process(String query) {
        String databaseName = parser.getValues(query);
        // Create the database
        DatabaseManager.createDatabase(databaseName);
        return true;
    }
}
