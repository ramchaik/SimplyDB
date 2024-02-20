package dev.vaibhavsingh.controller;

import dev.vaibhavsingh.dto.response.ParsedSQLResponse;
import dev.vaibhavsingh.parser.query.CreateQueryParser;
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
    QueryProcessor queryProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testProcess() {
        when(parser.parse(anyString())).thenReturn(new ParsedSQLResponse());

        boolean result = queryProcessor.process("query");
        Assertions.assertEquals(true, result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme