package org.example.Misc;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String DB_URL;

    static {
        DB_URL = initializeDatabase();
    }

    private static String initializeDatabase() {
        try {
            // Permanent location for database file
            String userDir = System.getProperty("user.home") + "/MyAppData";
            System.out.println("Database path: " + DB_URL);
            File appDir = new File(userDir);
            if (!appDir.exists() && !appDir.mkdirs()) {
                throw new IOException("Failed to create application directory: " + userDir);
            }

            File databaseFile = new File(appDir, "Database.db");

            // Copy database from resources if it doesn't exist
            if (!databaseFile.exists()) {
                try (InputStream inputStream = DatabaseManager.class.getResourceAsStream("/Database.db");
                     OutputStream outputStream = new FileOutputStream(databaseFile)) {
                    if (inputStream == null) {
                        throw new FileNotFoundException("Database.db not found in resources.");
                    }
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
                System.out.println("Database copied to: " + databaseFile.getAbsolutePath());
            } else {
                System.out.println("Using existing database: " + databaseFile.getAbsolutePath());
            }

            return "jdbc:sqlite:" + databaseFile.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
