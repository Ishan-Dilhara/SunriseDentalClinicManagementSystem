package sunrise.dental.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static Connection connection;

    public static Connection getConnection() {

        try {
            if (connection == null || connection.isClosed()) {

                connection = DriverManager.getConnection(
                        DatabaseConfig.URL,
                        DatabaseConfig.USERNAME,
                        DatabaseConfig.PASSWORD
                );

                System.out.println("Database connected successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }

        return connection;
    }
}