package dev.vaibhavsingh.dto.response;

import dev.vaibhavsingh.dto.ParsedCreateColumn;

import java.util.ArrayList;

public class ParsedSQLResponse {
    public String tableName;
    public ArrayList<ParsedCreateColumn> columns;
}

