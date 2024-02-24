package dev.vaibhavsingh.data;

import dev.vaibhavsingh.constants.DatabaseConstants;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * This class manages database operations such as creation and reading.
 */
public class DatabaseManager {
    public static String currentDatabase;

    public static void setCurrentDatabase(String currentDatabase) {
        DatabaseManager.currentDatabase = currentDatabase;
    }

    /**
     * Creates a new database with the specified name.
     *
     * @param databaseName The name of the database to create.
     */
    public static void createDatabase(String databaseName) {
        createDatabaseFolder(databaseName);
    }

    /**
     * Reads the metadata of the specified database.
     *
     * @param databaseName The name of the database to read.
     */
    public static void readDatabaseMetadata(String databaseName) {
        readMetadata(databaseName);
    }

    /**
     * Creates a folder for the database and writes metadata.
     *
     * @param databaseName The name of the database.
     */
    private static void createDatabaseFolder(String databaseName) {
        File rootFolder = new File(DatabaseConstants.DATABASE_ROOT_FOLDER);
        if (!rootFolder.exists()) {
            rootFolder.mkdirs();
        }

        String subfolderPath = DatabaseConstants.DATABASE_ROOT_FOLDER + File.separator + databaseName;
        File subfolder = new File(subfolderPath);
        if (!subfolder.exists()) {
            subfolder.mkdirs();
            createMetadata(databaseName, subfolderPath);
            System.out.println("Database '" + subfolderPath + "' created successfully.");
        } else {
            System.out.println("Database '" + subfolderPath + "' already exists.");
        }
    }

    /**
     * Creates metadata file for the database.
     *
     * @param databaseName The name of the database.
     * @param folderPath   The path to the folder containing the database.
     */
    private static void createMetadata(String databaseName, String folderPath) {
        try (FileWriter writer = new FileWriter(folderPath + File.separator + DatabaseConstants.DATABASE_METADATA_FILE)) {
            writer.write("DatabaseName:" + databaseName + "\n");
            writer.write("FolderPath:" + folderPath + "\n");
            System.out.println("Metadata file created successfully for database '" + databaseName + "'.");
        } catch (IOException e) {
            System.out.println("An error occurred while creating metadata file: " + e.getMessage());
        }
    }

    /**
     * Reads metadata of the specified database.
     *
     * @param databaseName The name of the database.
     */
    private static void readMetadata(String databaseName) {
        String folderPath = DatabaseConstants.DATABASE_ROOT_FOLDER + File.separator + databaseName;
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Folder '" + folderPath + "' not found.");
            return;
        }

        String metadataFilePath = folderPath + File.separator + DatabaseConstants.DATABASE_METADATA_FILE;
        try (Scanner scanner = new Scanner(new File(metadataFilePath))) {
            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading metadata file: " + e.getMessage());
        }
    }
}