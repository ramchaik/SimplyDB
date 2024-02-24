package dev.vaibhavsingh.controller;

import dev.vaibhavsingh.dao.ParsedSQLQuery;
import dev.vaibhavsingh.parser.CreateQueryParser;
import dev.vaibhavsingh.processor.CreateQueryProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class QueryProcessorTest {
    @Mock
    CreateQueryParser parser;
    @InjectMocks
    CreateQueryProcessor queryProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testProcess() {
        when(parser.parse(anyString())).thenReturn(new ParsedSQLQuery());

        boolean result = queryProcessor.process("query");
        Assertions.assertEquals(true, result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme