package dev.vaibhavsingh;

import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void testProcessQuery() {
        Main.processQuery("create table test (column1 int, column2 text)");
    }
}
