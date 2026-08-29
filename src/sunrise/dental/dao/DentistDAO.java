package sunrise.dental.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import sunrise.dental.config.DBConnection;
import sunrise.dental.model.Dentist;

public class DentistDAO {

    // Add Dentist
    public boolean addDentist(Dentist dentist) {

        String sql = "INSERT INTO dentists "
                + "(first_name, last_name, specialization, phone, email, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, dentist.getFirstName());
            statement.setString(2, dentist.getLastName());
            statement.setString(3, dentist.getSpecialization());
            statement.setString(4, dentist.getPhone());
            statement.setString(5, dentist.getEmail());
            statement.setTimestamp(6, dentist.getCreatedAt());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Add dentist error: " + e.getMessage());
            return false;
        }
    }

    // Find Dentist by ID
    public Dentist getDentistById(int dentistId) {

        String sql = "SELECT * FROM dentists WHERE dentist_id = ?";

        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, dentistId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                Dentist dentist = new Dentist();

                dentist.setDentistId(
                        resultSet.getInt("dentist_id")
                );

                dentist.setFirstName(
                        resultSet.getString("first_name")
                );

                dentist.setLastName(
                        resultSet.getString("last_name")
                );

                dentist.setSpecialization(
                        resultSet.getString("specialization")
                );

                dentist.setPhone(
                        resultSet.getString("phone")
                );

                dentist.setEmail(
                        resultSet.getString("email")
                );

                dentist.setCreatedAt(
                        resultSet.getTimestamp("created_at")
                );

                return dentist;
            }

        } catch (SQLException e) {
            System.out.println("Find dentist error: " + e.getMessage());
        }

        return null;
    }

    // Get All Dentists
    public List<Dentist> getAllDentists() {

        List<Dentist> dentistList = new ArrayList<>();

        String sql = "SELECT * FROM dentists";

        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Dentist dentist = new Dentist();

                dentist.setDentistId(
                        resultSet.getInt("dentist_id")
                );

                dentist.setFirstName(
                        resultSet.getString("first_name")
                );

                dentist.setLastName(
                        resultSet.getString("last_name")
                );

                dentist.setSpecialization(
                        resultSet.getString("specialization")
                );

                dentist.setPhone(
                        resultSet.getString("phone")
                );

                dentist.setEmail(
                        resultSet.getString("email")
                );

                dentist.setCreatedAt(
                        resultSet.getTimestamp("created_at")
                );

                dentistList.add(dentist);
            }

        } catch (SQLException e) {
            System.out.println("Get dentists error: " + e.getMessage());
        }

        return dentistList;
    }

    // Update Dentist
    public boolean updateDentist(Dentist dentist) {

        String sql = "UPDATE dentists SET "
                + "first_name = ?, "
                + "last_name = ?, "
                + "specialization = ?, "
                + "phone = ?, "
                + "email = ? "
                + "WHERE dentist_id = ?";

        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, dentist.getFirstName());
            statement.setString(2, dentist.getLastName());
            statement.setString(3, dentist.getSpecialization());
            statement.setString(4, dentist.getPhone());
            statement.setString(5, dentist.getEmail());
            statement.setInt(6, dentist.getDentistId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Update dentist error: " + e.getMessage());
            return false;
        }
    }

    // Delete Dentist
    public boolean deleteDentist(int dentistId) {

        String sql = "DELETE FROM dentists WHERE dentist_id = ?";

        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, dentistId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Delete dentist error: " + e.getMessage());
            return false;
        }
    }

    // Search Dentists
    public List<Dentist> searchDentists(String keyword) {

        List<Dentist> dentistList = new ArrayList<>();

        String sql = "SELECT * FROM dentists "
                + "WHERE first_name LIKE ? "
                + "OR last_name LIKE ? "
                + "OR specialization LIKE ?";

        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            String searchKeyword = "%" + keyword + "%";

            statement.setString(1, searchKeyword);
            statement.setString(2, searchKeyword);
            statement.setString(3, searchKeyword);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Dentist dentist = new Dentist();

                dentist.setDentistId(
                        resultSet.getInt("dentist_id")
                );

                dentist.setFirstName(
                        resultSet.getString("first_name")
                );

                dentist.setLastName(
                        resultSet.getString("last_name")
                );

                dentist.setSpecialization(
                        resultSet.getString("specialization")
                );

                dentist.setPhone(
                        resultSet.getString("phone")
                );

                dentist.setEmail(
                        resultSet.getString("email")
                );

                dentist.setCreatedAt(
                        resultSet.getTimestamp("created_at")
                );

                dentistList.add(dentist);
            }

        } catch (SQLException e) {
            System.out.println("Search dentist error: " + e.getMessage());
        }

        return dentistList;
    }
}
