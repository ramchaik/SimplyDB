package dev.vaibhavsingh.data;

import dev.vaibhavsingh.constants.DatabaseConstants;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static dev.vaibhavsingh.constants.DatabaseConstants.DATABASE_ROOT_FOLDER;
import static dev.vaibhavsingh.constants.DatabaseConstants.DB_SECRET_KEY;

public class TableManager {
    /**
     * Creates a new table with the specified name and columns.
     *
     * @param databaseName The name of the database containing the table.
     * @param tableName    The name of the table to create.
     * @param columns      An array of column names.
     * @param columnTypes  An array of column types.
     */
    public static void createTable(String databaseName, String tableName, String[] columns, String[] columnTypes) {
        String tableFolderPath = getTableFolderPath(databaseName, tableName);
        File tableFolder = new File(tableFolderPath);
        if (!tableFolder.exists()) {
            tableFolder.mkdirs();
        }
        String metadataFilePath = tableFolderPath + File.separator + DatabaseConstants.TABLE_METADATA_FILE;
        createTableMetadataFile(metadataFilePath, tableName, columns, columnTypes);
        String databaseMetadataFilePath = getTableFolderPath(databaseName, DatabaseConstants.TABLE_METADATA_FILE);
        updateDatabaseMetadataFile(databaseMetadataFilePath, tableName, tableFolderPath);
    }

    /**
     * Inserts values into the specified table.
     *
     * @param databaseName The name of the database containing the table.
     * @param tableName    The name of the table to insert values into.
     * @param values       An array of values to insert.
     */
    public static void insertIntoTable(String databaseName, String tableName, String[] values) {
        String tableFolderPath = getTableFolderPath(databaseName, tableName);
        File tableFolder = new File(tableFolderPath);
        if (!tableFolder.exists() || !tableFolder.isDirectory()) {
            System.out.println("Table '" + tableName + "' not found.");
            return;
        }
        String tableDataFilePath = getTableDataFilePath(tableName, tableFolderPath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tableDataFilePath, true))) {
            for (String value : values) {
                writer.write(value + ",");
            }
            writer.newLine();
            System.out.println("Values inserted into table '" + tableName + "' in database '" + databaseName + "' successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while inserting values into table: " + e.getMessage());
        }
    }

    /**
     * Reads values from the specified table.
     *
     * @param databaseName The name of the database containing the table.
     * @param tableName    The name of the table to read.
     */
    public static void readTable(String databaseName, String tableName) {
        String tableDataFilePath = getTableDataFilePath(tableName, getTableFolderPath(databaseName, tableName));
        try (BufferedReader reader = new BufferedReader(new FileReader(tableDataFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading table data: " + e.getMessage());
        }
    }

    private static void createTableMetadataFile(String metadataFilePath, String tableName, String[] columns, String[] columnTypes) {
        try (FileWriter writer = new FileWriter(metadataFilePath)) {
            writer.write("TableName:" + tableName + "\n");
            writer.write("TablePath:" + metadataFilePath + "\n");
            writer.write("Columns:" + columns.length + "\n");
            for (int i = 0; i < columns.length; i++) {
                writer.write(columns[i] + ":" + columnTypes[i] + "\n");
            }
            System.out.println("Table metadata file created successfully for table '" + tableName + "'.");
        } catch (IOException e) {
            System.out.println("An error occurred while creating table metadata file: " + e.getMessage());
        }
    }

    private static void updateDatabaseMetadataFile(String metadataFilePath, String tableName, String tableFolderPath) {
        try (FileWriter writer = new FileWriter(metadataFilePath, true)) {
            writer.write("Table:" + tableName + "," + tableFolderPath + "\n");
            System.out.println("Database metadata file updated successfully with table '" + tableName + "'.");
        } catch (IOException e) {
            System.out.println("An error occurred while updating database metadata file: " + e.getMessage());
        }
    }

    private static String getTableFolderPath(String databaseName, String tableName) {
        return DATABASE_ROOT_FOLDER + File.separator + databaseName + File.separator + tableName;
    }

    private static String getTableDataFilePath(String tableName, String tableFolderPath) {
        return tableFolderPath + File.separator + tableName + DatabaseConstants.TABLE_DATA_FILE;
    }
}
