package dev.vaibhavsingh.processor;

import dev.vaibhavsingh.data.DatabaseManager;
import dev.vaibhavsingh.data.TableManager;
import dev.vaibhavsingh.dao.ParsedColumn;
import dev.vaibhavsingh.dao.ParsedSQLQuery;
import dev.vaibhavsingh.parser.SQLParser;
import dev.vaibhavsingh.parser.SelectQueryParser;

import java.util.ArrayList;
import java.util.List;

import static dev.vaibhavsingh.data.TableManager.TABLE_DELIMITER;

public class SelectQueryProcessor implements QueryProcessor {
    SQLParser parser;

    public SelectQueryProcessor(SQLParser parser) {
        this.parser = parser;
    }

    @Override
    public boolean process(String query) {
        // parse the query using the SQLParserFactory
        ParsedSQLQuery parsedQuery = parser.parse(query);

        String databaseName = DatabaseManager.currentDatabase;
        String tableName = parser.getTableName(query);

        // check if table exists
        if (!TableManager.tableExists(databaseName, tableName)) {
            throw new RuntimeException("Table " + tableName + " does not exist");
        }

        ArrayList<String> valuesFromTable = TableManager.readTable(databaseName, tableName);
        ArrayList<String> tableColumns = TableManager.getTableColumns(databaseName, tableName);

        ArrayList<ParsedColumn> parsedWhereClauses = ((SelectQueryParser) parser).parseWhere(query);

        // select the name field for the parsedQuery columns as ArrayList<String>

        if (TransactionProcessor.isInTransaction()) {
            TransactionProcessor.addQueryToBuffer(query);
            return true;
        }

        // handle * case
        if (parsedQuery.columns.size() == 1 && parsedQuery.columns.get(0).name.equals("*")) {
            printColumnNames(tableColumns);
            printAllTable(tableName, valuesFromTable, parsedWhereClauses);
            return true;
        }

        List<String> columnsToShow = parsedQuery.columns.stream().map(column -> column.name).toList();
        printSelectedColumns(tableColumns, valuesFromTable, columnsToShow);

        return true;
    }

    private void printSelectedColumns(ArrayList<String> tableColumns, ArrayList<String> valuesFromTable, List<String> columnsToShow) {
        List<Integer> valueIdxToPick = new ArrayList<>();
        for (String column : columnsToShow) {
            // find the index of the column in the tableColumns
            int columnIndex = tableColumns.indexOf(column);
            if (columnIndex == -1) {
                throw new RuntimeException("Column " + column + " not found in table");
            }
            valueIdxToPick.add(columnIndex);
        }

        List<Integer> sortedIndexes = valueIdxToPick.stream().sorted().toList();

        // To make the output look consistent, we sort the index to manage the order of the columns
        // print the column names from tableColumns of the sorted index
        for (int idx : sortedIndexes) {
            // print as all caps
            System.out.print(tableColumns.get(idx).trim().toUpperCase() + "\t");
        }
        System.out.println();

        // print the values from the table of the selected columns
        for (String row : valuesFromTable) {
            String[] columns = row.split(TABLE_DELIMITER);
            for (int idx : sortedIndexes) {
                System.out.print(columns[idx].trim() + "\t");
            }
            System.out.println();
        }
    }

    public void printColumnNames(List<String> tableColumns) {
        for (String column : tableColumns) {
            System.out.print(column.trim().toUpperCase() + "\t");
        }
        System.out.println();
    }


    public void printAllTable(String tableName, ArrayList<String> valuesFromTable, ArrayList<ParsedColumn> parsedWhereClauses) {
        for (String row : valuesFromTable) {
            String[] columns = row.split(TABLE_DELIMITER);
            if (parsedWhereClauses == null || parsedWhereClauses.isEmpty()) {
                for (String column : columns) {
                    System.out.print(column.trim() + "\t");
                }
                System.out.println();
            } else {
                checkIfRowMatchesWhereClause(tableName, columns, parsedWhereClauses);
            }
        }
    }

    private void checkIfRowMatchesWhereClause(String tableName, String[] columns, ArrayList<ParsedColumn> parsedWhereClauses) {
        // go through each where clause and check if the row matches the where clause
        for (ParsedColumn parsedColumn : parsedWhereClauses) {
            int columnIndex = TableManager.getColumnIndex(DatabaseManager.currentDatabase, tableName, parsedColumn.name);
            if (columnIndex == -1) {
                throw new RuntimeException("Column " + parsedColumn.name + " not found in table");
            }
            String columnValue = columns[columnIndex];
            if (columnValue.trim().equals(parsedColumn.value.trim())) {
                for (String column : columns) {
                    System.out.print(column.trim() + "\t");
                }
                System.out.println();
                break;
            }
        }
    }
}
