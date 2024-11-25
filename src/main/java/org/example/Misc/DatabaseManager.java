package org.example.Misc;

import java.io.*;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String DB_URL;

    static {
        DB_URL = extractDatabaseFile();
    }

    private static String extractDatabaseFile() {
        try {
            // Get the database file from resources
            InputStream inputStream = DatabaseManager.class.getResourceAsStream("/Database.db");
            if (inputStream == null) {
                throw new FileNotFoundException("Database.db not found in resources.");
            }

            // Define the output location for the extracted file
            File tempFile = new File(System.getProperty("java.io.tmpdir"), "Database.db");

            // Copy the file from the JAR to the temp location
            try (OutputStream outputStream = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            System.out.println("Extracted database to: " + tempFile.getAbsolutePath());
            return "jdbc:sqlite:" + tempFile.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException("Failed to extract database file", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
