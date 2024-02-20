package dev.vaibhavsingh.dto;

public class ParsedCreateColumn {
    public String name;
    public String type;

    public ParsedCreateColumn(String columnName, String columnType) {
        this.name = columnName;
        this.type = columnType;
    }
}
