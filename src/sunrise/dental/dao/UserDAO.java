package sunrise.dental.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import sunrise.dental.config.DBConnection;
import sunrise.dental.model.User;

public class UserDAO {

    // Login User
    public User login(String email, String password) {

        String sql = "SELECT * FROM users "
                + "WHERE email = ? "
                + "AND password = ? "
                + "AND status = 'Active'";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                User user = new User();

                user.setUserId(
                        resultSet.getInt("user_id")
                );

                user.setFirstName(
                        resultSet.getString("first_name")
                );

                user.setLastName(
                        resultSet.getString("last_name")
                );

                user.setEmail(
                        resultSet.getString("email")
                );

                user.setPassword(
                        resultSet.getString("password")
                );

                user.setPhone(
                        resultSet.getString("phone")
                );

                user.setRole(
                        resultSet.getString("role")
                );

                user.setStatus(
                        resultSet.getString("status")
                );

                user.setCreatedAt(
                        resultSet.getTimestamp("created_at")
                );

                return user;
            }

        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }

        return null;
    }
}