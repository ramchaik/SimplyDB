package dev.vaibhavsingh.processor;

import dev.vaibhavsingh.parser.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class QueryProcessorFactoryTest {

    @Test
    void testGetSelectQueryProcessor() {
        SelectQueryParser parser = new SelectQueryParser();
        QueryProcessor result = QueryProcessorFactory.getQueryProcessor(parser);
        Assertions.assertInstanceOf(SelectQueryProcessor.class, result);
    }

    @Test
    void testGetCreateQueryProcessor() {
        CreateQueryParser parser = new CreateQueryParser();
        QueryProcessor result = QueryProcessorFactory.getQueryProcessor(parser);
        Assertions.assertInstanceOf(CreateQueryProcessor.class, result);
    }

    @Test
    void testGetInsertQueryProcessor() {
        InsertQueryParser parser = new InsertQueryParser();
        QueryProcessor result = QueryProcessorFactory.getQueryProcessor(parser);
        Assertions.assertInstanceOf(InsertQueryProcessor.class, result);
    }

    @Test
    void testGetTransactionProcessor() {
        TransactionParser parser = new TransactionParser();
        QueryProcessor result = QueryProcessorFactory.getQueryProcessor(parser);
        Assertions.assertInstanceOf(TransactionProcessor.class, result);
    }

    @Test
    void testGetUpdateQueryProcessor() {
        UpdateQueryParser parser = new UpdateQueryParser();
        QueryProcessor result = QueryProcessorFactory.getQueryProcessor(parser);
        Assertions.assertInstanceOf(UpdateQueryProcessor.class, result);
    }

    @Test
    void testGetCreateDatabaseProcessor() {
        CreateDatabaseParser parser = new CreateDatabaseParser();
        QueryProcessor result = QueryProcessorFactory.getQueryProcessor(parser);
        Assertions.assertInstanceOf(CreateDatabaseProcessor.class, result);
    }
}
