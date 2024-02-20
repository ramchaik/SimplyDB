package dev.vaibhavsingh.data;

import dev.vaibhavsingh.constants.DatabaseConstants;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static dev.vaibhavsingh.constants.DatabaseConstants.DATABASE_METADATA_FILE;
import static dev.vaibhavsingh.constants.DatabaseConstants.DATABASE_ROOT_FOLDER;

public class DatabaseManager {
    // Function to create a folder with the specified database name
    public static void create(String databaseName) {
        // Create the main databases directory if it doesn't exist
        File rootFolder = new File(DATABASE_ROOT_FOLDER);
        if (!rootFolder.exists()) {
            rootFolder.mkdirs();
        }

        // Create the subfolder for the database
        String subfolderPath = DATABASE_ROOT_FOLDER + File.separator + databaseName;
        File subfolder = new File(subfolderPath);
        if (!subfolder.exists()) {
            subfolder.mkdirs();
            System.out.println("Database '" + subfolderPath + "' created successfully.");
        } else {
            System.out.println("Database '" + subfolderPath + "' already exists.");
            System.exit(1);
        }

        // Create metadata text file for the database
        createMetadata(databaseName, subfolderPath);
    }

    // Function to create a metadata text file inside the database folder
    private static void createMetadata(String databaseName, String folderPath) {
        try {
            // Define the metadata file path
            String metadataFilePath = folderPath + File.separator + DATABASE_METADATA_FILE;

            // Create a FileWriter object for the metadata file
            FileWriter writer = new FileWriter(metadataFilePath);

            // Write metadata to the file
            writer.write("DatabaseName:" + databaseName + "\n");
            writer.write("FolderPath:" + folderPath + "\n");

            // Close the FileWriter
            writer.close();

            System.out.println("Metadata file created successfully for database '" + databaseName + "'.");
        } catch (IOException e) {
            System.out.println("An error occurred while creating metadata file: " + e.getMessage());
        }
    }

    public static void read(String databaseName) {
        // Get the folder path using the database name
        String folderPath = DATABASE_ROOT_FOLDER + File.separator + databaseName;

        // Check if the folder exists
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Folder '" + folderPath + "' not found.");
            return;
        }

        // Read metadata file inside the folder
        String metadataFilePath = folderPath + File.separator + DatabaseConstants.DATABASE_METADATA_FILE;
        try {
            // Create a Scanner object to read the metadata file
            Scanner scanner = new Scanner(new File(metadataFilePath));

            // Read and print metadata from the file
            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }

            // Close the Scanner
            scanner.close();
        } catch (IOException e) {
            System.out.println("An error occurred while reading metadata file: " + e.getMessage());
        }
    }

}

