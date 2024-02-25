package dev.vaibhavsingh.processor;

import dev.vaibhavsingh.parser.TransactionParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Queue;

import static org.mockito.Mockito.*;

class TransactionProcessorTest {
    @Mock
    Queue<String> buffer;
    @InjectMocks
    TransactionProcessor transactionProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStart() {
        TransactionProcessor.inTransaction = false;
        boolean result = TransactionProcessor.start();
        Assertions.assertEquals(true, result);
    }

    @Test
    void testRollback() {
        TransactionProcessor.inTransaction = true;
        boolean result = TransactionProcessor.rollback();
        Assertions.assertEquals(true, result);
    }

    @Test
    void testCommit() {
        TransactionProcessor.inTransaction = true;
        boolean result = TransactionProcessor.commit();
        Assertions.assertEquals(true, result);
    }

    @Test
    void testAddQueryToBuffer() {
        when(buffer.add(anyString())).thenReturn(true);

        boolean result = TransactionProcessor.addQueryToBuffer("query");
        Assertions.assertEquals(true, result);
    }

    @Test
    void testProcess() {
        when(buffer.add(anyString())).thenReturn(true);

        boolean result = transactionProcessor.process("query");
        Assertions.assertEquals(true, result);
    }
}