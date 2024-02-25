package dev.vaibhavsingh.data;

import dev.vaibhavsingh.constants.DatabaseConstants;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import static dev.vaibhavsingh.constants.DatabaseConstants.DB_SECRET_KEY;

public class TableManager {
    public static final String TABLE_DELIMITER = ":";
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
//            System.out.println("Table '" + tableName + "' already exists.");
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
//            System.out.println("Table '" + tableName + "' not found.");
            throw new IllegalArgumentException("Table '" + tableName + "' not found.");
        }

        String tableDataFilePath = getTableDataFilePath(tableName, tableFolderPath);
        try (
                FileChannel channel = FileChannel.open(Paths.get(tableDataFilePath), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
                FileLock lock = channel.lock();
                FileWriter fileWriter = new FileWriter(tableDataFilePath, true);
                BufferedWriter writer = new BufferedWriter(fileWriter)
        ) {

            // trim the values
            for (int i = 0; i < values.length; i++) {
                values[i] = values[i].trim();
            }

            for (String value : values) {
                if (DatabaseConstants.isEncryptionEnabled && databaseName != "admin") {
                    String encryptedValue = AESEncryption.encrypt(value, DB_SECRET_KEY);
                    writer.write(encryptedValue + TABLE_DELIMITER);
                } else {
                    writer.write(value + TABLE_DELIMITER);
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
    public static ArrayList<String> readTable(String databaseName, String tableName) {
        String tableDataFilePath = getTableDataFilePath(tableName, getTableFolderPath(databaseName, tableName));
        ArrayList<String> columns = new ArrayList<>();


        try (
                FileChannel channel = FileChannel.open(Paths.get(tableDataFilePath), StandardOpenOption.READ);
                FileLock lock = channel.lock(0, Long.MAX_VALUE, true);
                FileReader fileReader = new FileReader(tableDataFilePath);
                BufferedReader reader = new BufferedReader(fileReader)
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Decrypt and log the values
                String[] values = line.split(TABLE_DELIMITER);
                StringBuilder valueString = new StringBuilder();

                for (String value : values) {
                    if (DatabaseConstants.isEncryptionEnabled && databaseName != "admin") {
                        String decryptedValue = AESEncryption.decrypt(value, DB_SECRET_KEY);
                        valueString.append(decryptedValue).append(TABLE_DELIMITER);
                    } else {
                        valueString.append(value).append(TABLE_DELIMITER);
                    }
                }

                columns.add(valueString.toString());
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading table data: " + e.getMessage());
        } catch (AESEncryption.EncryptionException e) {
            throw new RuntimeException(e);
        }
        return columns;
    }

    /**
     * Creates a metadata file for the specified table.
     *
     * @param metadataFilePath The path to the table metadata file.
     * @param tableName        The name of the table.
     * @param columns          An array of column names.
     * @param columnTypes      An array of column types.
     */
    private static void createTableMetadataFile(String metadataFilePath, String tableName, String[] columns, String[] columnTypes) {
        try (
                FileChannel channel = FileChannel.open(Paths.get(metadataFilePath), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
                FileLock lock = channel.lock();
                FileWriter fileWriter = new FileWriter(metadataFilePath, true);
                BufferedWriter writer = new BufferedWriter(fileWriter)
        ) {
            writer.write("TableName:" + tableName + "\n");
            writer.write("TablePath:" + metadataFilePath + "\n");
            writer.write("Columns:" + columns.length + "\n");
            for (int i = 0; i < columns.length; i++) {
                writer.write(columns[i] + TABLE_DELIMITER + columnTypes[i] + "\n");
            }
            System.out.println("Table metadata file created successfully for table '" + tableName + "'.");
        } catch (IOException e) {
            System.out.println("An error occurred while creating table metadata file: " + e.getMessage());
        }
    }

    /**
     * Updates the database metadata file with the specified table.
     *
     * @param metadataFilePath The path to the database metadata file.
     * @param tableName        The name of the table to update.
     * @param tableFolderPath  The path to the folder containing the table.
     */
    private static void updateDatabaseMetadataFile(String metadataFilePath, String tableName, String tableFolderPath) {
        try (
                FileChannel channel = FileChannel.open(Paths.get(metadataFilePath), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
                FileLock lock = channel.lock();
                FileWriter fileWriter = new FileWriter(metadataFilePath, true);
                BufferedWriter writer = new BufferedWriter(fileWriter)
        ) {
            writer.write("Table:" + tableName + "," + tableFolderPath + "\n");
            System.out.println("Database metadata file updated successfully with table '" + tableName + "'.");
        } catch (IOException e) {
            System.out.println("An error occurred while updating database metadata file: " + e.getMessage());
        }
    }

    /**
     * Reads the metadata of the specified table.
     *
     * @param databaseName The name of the database containing the table.
     * @param tableName    The name of the table to read.
     * @return The metadata of the table.
     */
    public static String readTableMetadata(String databaseName, String tableName) {
        String tableFolderPath = getTableFolderPath(databaseName, tableName);
        String metadataFilePath = tableFolderPath + File.separator + DatabaseConstants.TABLE_METADATA_FILE;
        StringBuilder metadata = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(metadataFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                metadata.append(line).append("\n");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading table metadata: " + e.getMessage());
        }
        return metadata.toString();
    }

    /**
     * Returns the columns of the specified table.
     *
     * @param databaseName The name of the database containing the table.
     * @param tableName    The name of the table.
     * @return An array of column names.
     */
    public static ArrayList<String> getTableColumns(String databaseName, String tableName) {
        String tableFolderPath = getTableFolderPath(databaseName, tableName);
        String metadataFilePath = tableFolderPath + File.separator + DatabaseConstants.TABLE_METADATA_FILE;
        ArrayList<String> columns = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(metadataFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // check if line contains 'columns' and then append it to the columns list
                if (line.contains("Columns")) {
                    Integer columnCount = Integer.valueOf(line.split(TABLE_DELIMITER)[1]);
                    for (int i = 0; i < columnCount; i++) {
                        line = reader.readLine();
                        var columnName = line.split(TABLE_DELIMITER)[0];
                        columns.add(columnName);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading table metadata: " + e.getMessage());
        }
        return columns;
    }


    /**
     * Returns the index of the specified column in the table.
     *
     * @param databaseName The name of the database containing the table.
     * @param tableName    The name of the table.
     * @param columnName   The name of the column.
     * @return The index of the column in the table.
     */
    public static int getColumnIndex(String databaseName, String tableName, String columnName) {
        String tableFolderPath = getTableFolderPath(databaseName, tableName);
        String metadataFilePath = tableFolderPath + File.separator + DatabaseConstants.TABLE_METADATA_FILE;
        int columnIndex = -1;
        try (BufferedReader reader = new BufferedReader(new FileReader(metadataFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // check if line contains 'columns' and then append it to the columns list
                if (line.contains("Columns")) {
                    Integer columnCount = Integer.valueOf(line.split(TABLE_DELIMITER)[1]);
                    for (int i = 0; i < columnCount; i++) {
                        line = reader.readLine();
                        var column = line.split(TABLE_DELIMITER)[0];
                        if (column.equals(columnName)) {
                            columnIndex = i;
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading table metadata: " + e.getMessage());
        }
        return columnIndex;
    }

    /**
     * Returns the path to the folder containing the specified table.
     *
     * @param databaseName The name of the database containing the table.
     * @param tableName    The name of the table.
     * @return The path to the folder containing the table.
     */
    private static String getTableFolderPath(String databaseName, String tableName) {
        return DatabaseConstants.DATABASE_ROOT_FOLDER + File.separator + databaseName + File.separator + tableName;
    }

    /**
     * Returns the path to the table data file.
     *
     * @param tableName       The name of the table.
     * @param tableFolderPath The path to the folder containing the table.
     * @return The path to the table data file.
     */
    private static String getTableDataFilePath(String tableName, String tableFolderPath) {
        return tableFolderPath + File.separator + tableName + DatabaseConstants.TABLE_DATA_FILE;
    }

    /**
     * Checks if a table exists in the specified database.
     *
     * @param databaseName The name of the database containing the table.
     * @param tableName    The name of the table to check.
     * @return True if the table exists, false otherwise.
     */
    public static boolean tableExists(String databaseName, String tableName) {
        String tableFolderPath = getTableFolderPath(databaseName, tableName);
        File tableFolder = new File(tableFolderPath);
        return tableFolder.exists();
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

    /**
     * Deletes the specified table from the specified database.
     *
     * @param databaseName The name of the database containing the table.
     * @param tableName    The name of the table to delete.
     */
    public static boolean updateTable(String databaseName, String tableName, ArrayList<String> valuesFromTable) {
        String tableFolderPath = getTableFolderPath(databaseName, tableName);
        String tableDataFilePath = getTableDataFilePath(tableName, tableFolderPath);
        try (
                FileChannel channel = FileChannel.open(Paths.get(tableDataFilePath), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
                FileLock lock = channel.lock();
                FileWriter fileWriter = new FileWriter(tableDataFilePath, false);
                BufferedWriter writer = new BufferedWriter(fileWriter)
        ) {
            for (String value : valuesFromTable) {
                writer.write(value);
                writer.newLine();
            }
            System.out.println("Table updated successfully.");
            return true;
        } catch (IOException e) {
            System.out.println("An error occurred while updating table: " + e.getMessage());
            return false;
        }
    }
}
