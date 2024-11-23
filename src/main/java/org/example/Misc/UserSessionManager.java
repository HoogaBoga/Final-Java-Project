package org.example.Misc;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Properties;
import java.util.UUID;

public class UserSessionManager {

    private static final String DB_URL = "jdbc:sqlite:C:/Users/Spyke/IdeaProjects/FinalJavaProject/Database.db";

    /**
     * Retrieves the ID of the currently logged-in user.
     *
     * @return The user ID of the logged-in user, or -1 if no user is logged in.
     */
    public static int getLoggedInUserID() {
        String query = "SELECT id FROM Users WHERE is_logged_in = 1";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // No user is logged in
    }

    public static int getUserIdByCredentials(String username, String password) {
        String query = "SELECT id FROM Users WHERE username = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id"); // Return the user ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // Return -1 if credentials are invalid
    }

    /**
     * Marks a specific user as logged in.
     *
     * @param userId The ID of the user to mark as logged in.
     */
    public static void markAsLoggedIn(int userId) {
        String query = "UPDATE Users SET is_logged_in = 1 WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
            System.out.println("User with ID " + userId + " is now marked as logged in.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Marks all users as logged out.
     */
    public static void markAsLoggedOut() {
        String query = "UPDATE Users SET is_logged_in = 0";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(query);
            System.out.println("All users are now marked as logged out.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean sendPasswordResetToken(String email) {
        String query = "SELECT id FROM Users WHERE email = ?";
        String updateQuery = "UPDATE Users SET reset_token = ?, reset_token_time = datetime('now', 'localtime') WHERE email = ?";
        String token = UUID.randomUUID().toString(); // Generate a unique token

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement selectStatement = connection.prepareStatement(query);
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

            // Check if email exists
            selectStatement.setString(1, email);
            ResultSet resultSet = selectStatement.executeQuery();

            if (!resultSet.next()) {
                System.out.println("Email not found in the database.");
                return false; // Email does not exist
            }

            // Update the reset token and timestamp
            updateStatement.setString(1, token);
            updateStatement.setString(2, email);
            updateStatement.executeUpdate();

            // Send the token via email
            sendEmail(email, "Password Reset Token", "Here is your password reset token: " + token);

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Sends an email with the reset token.
     *
     * @param recipient The recipient's email address.
     */
    private static void sendEmail(String recipient, String subject, String body) {
        // Email configuration
        final String senderEmail = "c202301028@iacademy.edu.ph"; // Replace with your email
        final String senderPassword = "esfk yhrn dent pbvp";       // Replace with your email password
        final String smtpHost = "smtp.gmail.com";            // Replace with your SMTP host

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", "587");

        // Create a session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create the email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(body);

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully to " + recipient);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    public static boolean updatePasswordWithToken(String resetToken, String newPassword) {
        String selectQuery = "SELECT id, reset_token_time FROM Users WHERE reset_token = ?";
        String updateQuery = "UPDATE Users SET password = ?, reset_token = NULL, reset_token_time = NULL WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

            // Check if the reset token exists and is valid
            selectStatement.setString(1, resetToken);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("id");
                Timestamp resetTokenTime = resultSet.getTimestamp("reset_token_time");

                System.out.println("DEBUG: User ID = " + userId);
                System.out.println("DEBUG: Reset Token Time = " + resetTokenTime);

                // Check if the token is expired (e.g., valid for 15 minutes)
                ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.systemDefault());
                long currentTimeMillis = Timestamp.valueOf(currentTime.toLocalDateTime()).getTime();

                System.out.println("DEBUG: User ID = " + userId);
                System.out.println("DEBUG: Reset Token Time = " + resetTokenTime);

                long tokenTimeMillis = resetTokenTime.getTime();

                if ((currentTimeMillis - tokenTimeMillis) > (15 * 60 * 1000)) { // 15 minutes
                    System.out.println("DEBUG: Token expired");
                    return false; // Token is expired
                }

                // Update the password
                updateStatement.setString(1, newPassword);
                updateStatement.setInt(2, userId);
                int rowsUpdated = updateStatement.executeUpdate();

                System.out.println("DEBUG: Password updated, rows affected = " + rowsUpdated);
                return rowsUpdated > 0; // Return true if the password was updated
            } else {
                System.out.println("DEBUG: Reset token not found in the database");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Return false if the token is invalid or any error occurred
    }

    public static boolean sendUsername(String email) {
        String query = "SELECT username FROM Users WHERE email = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Check if email exists and retrieve the username
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String username = resultSet.getString("username");
                sendEmail(email, "Your Username", "Your username is: " + username);
                return true; // Email sent successfully
            } else {
                System.out.println("Email not found in the database.");
                return false; // Email does not exist
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // An error occurred
        }
    }

}
