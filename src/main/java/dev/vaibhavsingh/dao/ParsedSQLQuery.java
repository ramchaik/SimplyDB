package dev.vaibhavsingh.dao;

import java.util.ArrayList;

/**
 * The parsed SQL query
 */
public class ParsedSQLQuery {
    /**
     * The type of the query
     */
    public String tableName;
    /**
     * The columns in the table
     */
    public ArrayList<ParsedColumn> columns;
}

