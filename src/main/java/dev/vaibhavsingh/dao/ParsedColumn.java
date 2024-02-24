package dev.vaibhavsingh.dao;

public class ParsedColumn {
    public String name;
    public String type;

    public String value;

    public ParsedColumn(String columnName, String columnType, String value) {
        this.name = columnName;
        this.type = columnType;
        this.value = value;
    }
}
