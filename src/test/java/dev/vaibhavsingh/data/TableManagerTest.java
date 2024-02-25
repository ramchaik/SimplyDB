package dev.vaibhavsingh.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class TableManagerTest {
    @Mock
    FileLock tableFileLock;
    @InjectMocks
    TableManager tableManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTable() {
        boolean result = TableManager.createTable("databaseName", "tableName", new String[]{"columns"}, new String[]{"columnTypes"});
        Assertions.assertEquals(true, result);
    }

    @Test
    void testInsertIntoTable() {
        TableManager.insertIntoTable("databaseName", "tableName", new String[]{"values"});
    }

    @Test
    void testReadTable() {
        ArrayList<String> result = TableManager.readTable("databaseName", "tableName");
        Assertions.assertEquals(new ArrayList<>(List.of("replaceMeWithExpectedResult")), result);
    }

    @Test
    void testReadTableMetadata() {
        String result = TableManager.readTableMetadata("databaseName", "tableName");
        Assertions.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    void testGetTableColumns() {
        ArrayList<String> result = TableManager.getTableColumns("databaseName", "tableName");
        Assertions.assertEquals(new ArrayList<>(List.of("replaceMeWithExpectedResult")), result);
    }

    @Test
    void testGetColumnIndex() {
        int result = TableManager.getColumnIndex("databaseName", "tableName", "columnName");
        Assertions.assertEquals(0, result);
    }

    @Test
    void testTableExists() {
        boolean result = TableManager.tableExists("databaseName", "tableName");
        Assertions.assertEquals(true, result);
    }

    @Test
    void testLockTable() {
        FileLock result = TableManager.lockTable(new File(getClass().getResource("/dev/vaibhavsingh/data/PleaseReplaceMeWithTestFile.txt").getFile()));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testReleaseTable() {
        when(tableFileLock.isValid()).thenReturn(true);

        TableManager.releaseTable();
    }

    @Test
    void testUpdateTable() {
        boolean result = TableManager.updateTable("databaseName", "tableName", new ArrayList<>(List.of("valuesFromTable")));
        Assertions.assertEquals(true, result);
    }
}