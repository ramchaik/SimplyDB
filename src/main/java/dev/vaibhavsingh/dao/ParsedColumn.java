package dev.vaibhavsingh.dao;

/**
 * The parsed column
 */
public class ParsedColumn {
    /**
     * The name of the column
     */
    public String name;
    /**
     * The type of the column
     */
    public String type;

    /**
     * The value of the column
     */
    public String value;

    /**
     * Constructor for ParsedColumn
     * @param columnName the name of the column
     * @param columnType the type of the column
     * @param value the value of the column
     */
    public ParsedColumn(String columnName, String columnType, String value) {
        this.name = columnName;
        this.type = columnType;
        this.value = value;
    }
}
