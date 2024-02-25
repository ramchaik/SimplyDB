package dev.vaibhavsingh.processor;

import dev.vaibhavsingh.data.DatabaseManager;
import dev.vaibhavsingh.data.TableManager;
import dev.vaibhavsingh.dao.ParsedSQLQuery;
import dev.vaibhavsingh.parser.SQLParser;

public class InsertQueryProcessor implements QueryProcessor {
    /**
     * The SQL parser
     */
    SQLParser parser;

    /**
     * Constructor for InsertQueryProcessor
     * @param parser the SQL parser
     */
    public InsertQueryProcessor(SQLParser parser) {
        this.parser = parser;
    }

    /**
     * Processes the INSERT query
     * @param query the query to be processed
     * @return true if the query is processed successfully, false otherwise
     */
    @Override
    public boolean process(String query) {
        // parse the query using the SQLParserFactory
        ParsedSQLQuery parsedCreateColumn = parser.parse(query);

        String databaseName = DatabaseManager.currentDatabase;
        String tableName = parser.getTableName(query);

        // check if table exists
        if (!TableManager.tableExists(databaseName, tableName)) {
            throw new RuntimeException("Table " + tableName + " does not exist");
        }

        parsedCreateColumn.columns.forEach(column -> {
            if (column.value == null) {
                throw new RuntimeException("Error: Missing value, value cannot be null");
            }
            int columnIndex = TableManager.getColumnIndex(DatabaseManager.currentDatabase, tableName, column.name);
            if (columnIndex == -1) {
                throw new RuntimeException("Column " + column.name + " not found in table");
            }
        });

        if (TransactionProcessor.isInTransaction()) {
            TransactionProcessor.addQueryToBuffer(query);
            return true;
        }

        TableManager.insertIntoTable(DatabaseManager.currentDatabase, parsedCreateColumn.tableName, parsedCreateColumn.columns.stream().map(column -> column.value).toArray(String[]::new));

        return true;
    }
}
