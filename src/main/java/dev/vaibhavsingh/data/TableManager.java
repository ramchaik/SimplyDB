package dev.vaibhavsingh.data;

import dev.vaibhavsingh.constants.DatabaseConstants;
import dev.vaibhavsingh.utils.AESEncryption;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import static dev.vaibhavsingh.constants.DatabaseConstants.DB_SECRET_KEY;

public class TableManager {
    private static FileLock tableFileLock;

    /**
     * Creates a new table with the specified name and columns.
     *
     * @param databaseName The name of the database containing the table.
     * @param tableName    The name of the table to create.
     * @param columns      An array of column names.
     * @param columnTypes  An array of column types.
     */
    public static boolean createTable(String databaseName, String tableName, String[] columns, String[] columnTypes) {
        System.out.println("Creating table '" + tableName + "' in database '" + databaseName + "'.");
        String tableFolderPath = getTableFolderPath(databaseName, tableName);
        File tableFolder = new File(tableFolderPath);
        if (!tableFolder.exists()) {
            tableFolder.mkdirs();
        } else {
            throw new IllegalArgumentException("Table '" + tableName + "' already exists.");
        }
        String metadataFilePath = tableFolderPath + File.separator + DatabaseConstants.TABLE_METADATA_FILE;
        System.out.println("Creating meta data table '" + tableName + "' in database '" + databaseName + "'.");
        createTableMetadataFile(metadataFilePath, tableName, columns, columnTypes);
        String databaseMetadataFilePath = getTableFolderPath(databaseName, DatabaseConstants.TABLE_METADATA_FILE);
        updateDatabaseMetadataFile(databaseMetadataFilePath, tableName, tableFolderPath);
        System.out.println("Table '" + tableName + "' created successfully in database '" + databaseName + "'.");
        return true;
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
                if (DatabaseConstants.isEncryptionEnabled) {
                    String encryptedValue = AESEncryption.encrypt(value, DB_SECRET_KEY);
                    writer.write(encryptedValue + ",");
                } else {
                    writer.write(value + ",");
                }
            }
            writer.newLine();
            System.out.println("Values inserted into table '" + tableName + "' in database '" + databaseName + "' successfully.");
        } catch (IOException | AESEncryption.EncryptionException e) {
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
                // Decrypt and log the values
                String[] values = line.split(",");
                for (String value : values) {
                    if (DatabaseConstants.isEncryptionEnabled) {
                        String decryptedValue = AESEncryption.decrypt(value, DB_SECRET_KEY);
                        System.out.print(decryptedValue + "\t");
                    } else {
                        System.out.print(value + "\t");
                    }
                }
                System.out.println();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading table data: " + e.getMessage());
        } catch (AESEncryption.EncryptionException e) {
            throw new RuntimeException(e);
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
        return DatabaseConstants.DATABASE_ROOT_FOLDER + File.separator + databaseName + File.separator + tableName;
    }

    private static String getTableDataFilePath(String tableName, String tableFolderPath) {
        return tableFolderPath + File.separator + tableName + DatabaseConstants.TABLE_DATA_FILE;
    }

    /**
     * Locks a table file.
     *
     * @param file The table file to lock.
     * @return The file lock if successful, null otherwise.
     */
    public static FileLock lockTable(File file) {
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            FileChannel channel = raf.getChannel();
            tableFileLock = channel.lock(); // Locks the entire file
            return tableFileLock;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Release gable lock.
     */
    public static void releaseTable() {
        FileLock lock = tableFileLock;
        if (lock != null && lock.isValid()) {
            try {
                lock.release(); // Releases the lock
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
