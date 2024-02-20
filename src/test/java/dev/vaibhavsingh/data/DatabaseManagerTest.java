package dev.vaibhavsingh.data;

import org.junit.jupiter.api.Test;

class DatabaseManagerTest {

    @Test
    void testCreateDatabase() {
        DatabaseManager.createDatabase("databaseName");
    }

    @Test
    void testReadDatabaseMetadata() {
        DatabaseManager.readDatabaseMetadata("databaseName");
    }
}
