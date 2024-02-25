package dev.vaibhavsingh.processor;

import dev.vaibhavsingh.dao.ParsedSQLQuery;
import dev.vaibhavsingh.data.DatabaseManager;
import dev.vaibhavsingh.data.TableManager;
import dev.vaibhavsingh.parser.SQLParser;
import dev.vaibhavsingh.parser.UpdateQueryParser;

import java.util.ArrayList;

public class UpdateQueryProcessor implements QueryProcessor {
    SQLParser parser;
    public UpdateQueryProcessor(SQLParser parser) {
        this.parser = parser;
    }

    /**
     * Process the query
     * @param query the query to process
     * @return true if the query was processed successfully, false otherwise
     */
    @Override
    public boolean process(String query) {
        // get the table name
        String tableName = parser.getTableName(query);
        // get the database name
        String databaseName = DatabaseManager.currentDatabase;

        // check if table exists
        if (!TableManager.tableExists(databaseName, tableName)) {
            throw new RuntimeException("Table " + tableName + " does not exist");
        }

        ParsedSQLQuery parsedSQLQuery = parser.parse(query);

        ParsedSQLQuery whereClause = ((UpdateQueryParser) parser).parseWhereClause(query);

        ArrayList<String> valuesFromTable = TableManager.readTable(databaseName, tableName);

        // if where clause is not present, update all the rows
        if (whereClause.columns.size() == 0) {
            for (int i = 0; i < valuesFromTable.size(); i++) {
                String[] row = valuesFromTable.get(i).split(TableManager.TABLE_DELIMITER);
                for (int j = 0; j < parsedSQLQuery.columns.size(); j++) {
                    String columnName = parsedSQLQuery.columns.get(j).name;
                    String columnValue = parsedSQLQuery.columns.get(j).value;
                    int columnIndex = TableManager.getColumnIndex(databaseName, tableName, columnName);
                    row[columnIndex] = columnValue;
                }
                valuesFromTable.set(i, String.join(TableManager.TABLE_DELIMITER, row));
            }
            TableManager.updateTable(databaseName, tableName, valuesFromTable);
            return true;
        }

        // go through each value and update the value
        for (int i = 0; i < valuesFromTable.size(); i++) {
            String[] row = valuesFromTable.get(i).split(TableManager.TABLE_DELIMITER);
            if (whereClause.columns.get(0).value.equals(row[0])) {
                for (int j = 0; j < parsedSQLQuery.columns.size(); j++) {
                    String columnName = parsedSQLQuery.columns.get(j).name;
                    String columnValue = parsedSQLQuery.columns.get(j).value;
                    int columnIndex = TableManager.getColumnIndex(databaseName, tableName, columnName);
                    row[columnIndex] = columnValue;
                }
                valuesFromTable.set(i, String.join(TableManager.TABLE_DELIMITER, row));
            }
        }

        TableManager.updateTable(databaseName, tableName, valuesFromTable);
        return true;
    }
}
