package dev.vaibhavsingh.parser;

import dev.vaibhavsingh.dao.ParsedSQLQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TransactionParserTest {
    TransactionParser transactionParser = new TransactionParser();

    @Test
    void testGetQueryType() {
        String result = transactionParser.getQueryType();
        Assertions.assertEquals("TRANSACTION", result);
    }

    @Test
    void testIsValidQuery() {
        boolean result = transactionParser.isValidQuery("start transaction");
        Assertions.assertEquals(true, result);
    }

    @Test
    void testGetTableName() {
        String result = transactionParser.getTableName("start transaction");
        Assertions.assertEquals(null, result);
    }

    @Test
    void testGetValues() {
        String result = transactionParser.getValues("start transaction");
        Assertions.assertEquals(null, result);
    }

    @Test
    void testParse() {
        ParsedSQLQuery result = transactionParser.parse("start transaction");
        Assertions.assertEquals(null, result);
    }
}