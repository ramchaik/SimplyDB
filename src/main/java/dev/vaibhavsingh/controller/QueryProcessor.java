package dev.vaibhavsingh.controller;

import dev.vaibhavsingh.data.TableManager;
import dev.vaibhavsingh.dto.ParsedCreateColumn;
import dev.vaibhavsingh.dto.response.ParsedSQLResponse;
import dev.vaibhavsingh.parser.query.CreateQueryParser;
import dev.vaibhavsingh.parser.query.SQLParser;

import static dev.vaibhavsingh.constants.DatabaseConstants.DATABASE_NAME;

public class QueryProcessor {
    CreateQueryParser parser;
    public QueryProcessor(CreateQueryParser parser) {
        this.parser = parser;
    }
    // takes string input and returns boolean true if successful else false
    public boolean process(String query) {
        // parse the query using the SQLParserFactory
        ParsedSQLResponse parsedCreateColumn = parser.parse(query);

        String[] columns = parsedCreateColumn.columns.stream().map(column -> column.name).toArray(String[]::new);
        String[] columnTypes = parsedCreateColumn.columns.stream().map(column -> column.type).toArray(String[]::new);


        TableManager.createTable(DATABASE_NAME, parsedCreateColumn.tableName, columns, columnTypes);

        // parse the response using the ParsedSQLResponse
        // Use the TableManager to create a new table with the parsed response


        return true;
    }
}
