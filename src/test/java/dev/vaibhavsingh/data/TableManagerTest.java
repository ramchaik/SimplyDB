package dev.vaibhavsingh.data;

import org.junit.jupiter.api.Test;

class TableManagerTest {

    @Test
    void testCreateTable() {
        TableManager.createTable("databaseName", "tableName", new String[]{"columns"}, new String[]{"columnTypes"});
    }

    @Test
    void testInsertIntoTable() {
        TableManager.insertIntoTable("databaseName", "tableName", new String[]{"values"});
    }

    @Test
    void testReadTable() {
        TableManager.readTable("databaseName", "tableName");
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme