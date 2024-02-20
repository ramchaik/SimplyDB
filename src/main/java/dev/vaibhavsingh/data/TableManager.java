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
    public static void create(String databaseName, String tableName, String[] columns, String[] columnTypes) {
        // Create the table folder within the database folder
        String tableFolderPath = getTableFolderPath(databaseName, tableName);
        File tableFolder = new File(tableFolderPath);
        if (!tableFolder.exists()) {
            tableFolder.mkdirs();
        }

        // Create the table metadata file
        String metadataFilePath = tableFolderPath + File.separator + DatabaseConstants.TABLE_METADATA_FILE;
        createTableMetadataFile(metadataFilePath, tableName, columns, columnTypes);

        // Update the database metadata file with table details
        String databaseMetadataFilePath = getTableFolderPath(databaseName, DatabaseConstants.TABLE_METADATA_FILE);
        updateDatabaseMetadataFile(databaseMetadataFilePath, tableName, tableFolderPath);
    }

    public static void insert(String databaseName, String tableName, String[] values) {
        String tableFolderPath = getTableFolderPath(databaseName, tableName);
        File tableFolder = new File(tableFolderPath);

        if (!tableFolder.exists() || !tableFolder.isDirectory()) {
            System.out.println("Table '" + tableName + "' not found.");
            return;
        }

        String tableDataFilePath = getTableDataFilePath(tableName, tableFolderPath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tableDataFilePath, true))) {
            // Encrypt and write values to the file
            for (String value : values) {
                // TODO: add encryption flag on db metadata file; use that to enable/disable encryption
                String encryptedValue = Encryption.encrypt(value, DB_SECRET_KEY);
                writer.write(encryptedValue + ",");
            }
            writer.newLine();
            System.out.println("Values inserted into table '" + tableName + "' in database '" + databaseName + "' successfully.");
        } catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                 BadPaddingException | IllegalBlockSizeException e) {
            System.out.println("An error occurred while inserting values into table: " + e.getMessage());
        }
    }

    public static void read(String databaseName, String tableName) {
        String tableFolderPath = getTableFolderPath(databaseName, tableName);
        File tableFolder = new File(tableFolderPath);

        if (!tableFolder.exists() || !tableFolder.isDirectory()) {
            System.out.println("Table '" + tableName + "' not found.");
            return;
        }

        String tableDataFilePath = getTableDataFilePath(tableName, tableFolderPath);
        try (BufferedReader reader = new BufferedReader(new FileReader(tableDataFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Decrypt and log the values
                String[] encryptedValues = line.split(",");
                for (String encryptedValue : encryptedValues) {
                    // TODO: add flag on db metadata file to check if encryption is enabled
                    String decryptedValue = Encryption.decrypt(encryptedValue, DatabaseConstants.DB_SECRET_KEY);
                    System.out.print(decryptedValue + "\t");
                }
                System.out.println();
            }
        } catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                 BadPaddingException | IllegalBlockSizeException e) {
            System.out.println("An error occurred while reading table data: " + e.getMessage());
        }
    }

    private static void createTableMetadataFile(String metadataFilePath, String tableName, String[] columns, String[] columnTypes) {
        try (FileWriter writer = new FileWriter(metadataFilePath)) {
            writer.write("TableName:" + tableName + "\n");
            writer.write("TablePath:" + metadataFilePath + "\n");
            writer.write("Columns:"+ columns.length +"\n");
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
