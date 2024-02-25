package dev.vaibhavsingh.processor;

import dev.vaibhavsingh.data.TableManager;
import dev.vaibhavsingh.dao.ParsedSQLQuery;
import dev.vaibhavsingh.parser.SQLParser;

import static dev.vaibhavsingh.constants.DatabaseConstants.DATABASE_NAME;

public class CreateQueryProcessor implements QueryProcessor {
    /**
     * The SQL parser
     */
    SQLParser parser;
    /**
     * Constructor for CreateQueryProcessor
     * @param parser the SQL parser
     */
    public CreateQueryProcessor(SQLParser parser) {
        this.parser = parser;
    }

    /**
     * Processes the CREATE query
     * @param query the query to be processed
     * @return true if the query is processed successfully, false otherwise
     */
    @Override
    public boolean process(String query) {
        // parse the query using the SQLParserFactory
        ParsedSQLQuery parsedCreateColumn = parser.parse(query);

        String[] columns = parsedCreateColumn.columns.stream().map(column -> column.name).toArray(String[]::new);
        String[] columnTypes = parsedCreateColumn.columns.stream().map(column -> column.type).toArray(String[]::new);

        if (TransactionProcessor.isInTransaction()) {
            TransactionProcessor.addQueryToBuffer(query);
            return true;
        }

        TableManager.createTable(DATABASE_NAME, parsedCreateColumn.tableName, columns, columnTypes);
        return true;
    }
}
