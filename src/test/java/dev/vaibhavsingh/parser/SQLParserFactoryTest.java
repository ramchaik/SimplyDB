package dev.vaibhavsingh.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SQLParserFactoryTest {

    @Test
    void testGetParserForSelectQuery() {
        SQLParser result = SQLParserFactory.getParser("select * from table");
        Assertions.assertInstanceOf(SelectQueryParser.class, result);
    }

    @Test
    void testGetParserForCreateQuery() {
        SQLParser result = SQLParserFactory.getParser("create table users (column1 int, column2 varchar(255))");
        Assertions.assertInstanceOf(CreateQueryParser.class, result);
    }

    @Test
    void testGetParserForInsertQuery() {
        SQLParser result = SQLParserFactory.getParser("insert into users values (1, 'John')");
        Assertions.assertInstanceOf(InsertQueryParser.class, result);
    }

    @Test
    void testGetParserForCreateDatabaseQuery() {
        SQLParser result = SQLParserFactory.getParser("create database test");
        Assertions.assertInstanceOf(CreateDatabaseParser.class, result);
    }

    @Test
    void testGetParserForStartTransactionQuery() {
        SQLParser result = SQLParserFactory.getParser("start transaction");
        Assertions.assertInstanceOf(TransactionParser.class, result);
    }

    @Test
    void testGetParserForStartTransactionQueryWithBegin() {
        SQLParser result = SQLParserFactory.getParser("begin transaction");
        Assertions.assertInstanceOf(TransactionParser.class, result);
    }

    @Test
    void testGetParserForCommitTransactionQuery() {
        SQLParser result = SQLParserFactory.getParser("commit");
        Assertions.assertInstanceOf(TransactionParser.class, result);
    }

    @Test
    void testGetParserForRollbackTransactionQuery() {
        SQLParser result = SQLParserFactory.getParser("rollback");
        Assertions.assertInstanceOf(TransactionParser.class, result);
    }

    @Test
    void testGetParserForUnsupportedQuery() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> SQLParserFactory.getParser("unsupported query"));
    }

    @Test
    void testGetParserForEmptyQuery() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> SQLParserFactory.getParser(""));
    }

}
