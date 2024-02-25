package dev.vaibhavsingh.parser;

import dev.vaibhavsingh.dao.ParsedColumn;
import dev.vaibhavsingh.dao.ParsedSQLQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class SelectQueryParserTest {
    SelectQueryParser selectQueryParser = new SelectQueryParser();

    @Test
    void testGetQueryType() {
        String result = selectQueryParser.getQueryType();
        Assertions.assertEquals("SELECT", result);
    }

    @Test
    void testIsValidQuery() {
        boolean result = selectQueryParser.isValidQuery("query");
        Assertions.assertEquals(true, result);
    }

    @Test
    void testGetTableName() {
        String result = selectQueryParser.getTableName("select * from table where column = value;");
        Assertions.assertEquals("table", result);
    }

    @Test
    void testGetValues() {
        String result = selectQueryParser.getValues("select * from table where column = value;");
        Assertions.assertEquals("*", result);
    }

    @Test
    void testParse() {
        ParsedSQLQuery result = selectQueryParser.parse("select id, age from table where column = value;");
        Assertions.assertEquals(result.tableName, "table");
        Assertions.assertEquals(result.columns.size(), 2);
        Assertions.assertEquals(result.columns.get(0).name, "id");
        Assertions.assertEquals(result.columns.get(1).name, "age");
    }

    @Test
    void testGetWhereClause() {
        String result = selectQueryParser.getWhereClause("select * from table where column = value;");
        Assertions.assertEquals("column = value", result);
    }

    @Test
    void testParseWhere() {
        ArrayList<ParsedColumn> result = selectQueryParser.parseWhere("select * from table where column = value;");
        Assertions.assertEquals(result.get(0).name, "column");
        Assertions.assertEquals(result.get(0).value, "value");
    }
}