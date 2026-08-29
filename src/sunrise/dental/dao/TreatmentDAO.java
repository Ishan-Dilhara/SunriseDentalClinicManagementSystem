package sunrise.dental.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sunrise.dental.config.DBConnection;
import sunrise.dental.model.Treatment;

public class TreatmentDAO {

    // Add Treatment
    public boolean addTreatment(Treatment treatment) {

        String sql = "INSERT INTO treatments "
                + "(patient_id, dentist_id, treatment_name, "
                + "description, cost, treatment_date) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, treatment.getPatientId());
            statement.setInt(2, treatment.getDentistId());
            statement.setString(3, treatment.getTreatmentName());
            statement.setString(4, treatment.getDescription());
            statement.setDouble(5, treatment.getCost());
            statement.setDate(6, treatment.getTreatmentDate());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Add treatment error: " + e.getMessage());
            return false;
        }
    }

    // Find Treatment by ID
    public Treatment getTreatmentById(int treatmentId) {

        String sql = "SELECT * FROM treatments WHERE treatment_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, treatmentId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractTreatment(resultSet);
            }

        } catch (SQLException e) {
            System.out.println("Find treatment error: " + e.getMessage());
        }

        return null;
    }

    // Get All Treatments
    public List<Treatment> getAllTreatments() {

        List<Treatment> treatmentList = new ArrayList<>();

        String sql = "SELECT * FROM treatments";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                treatmentList.add(extractTreatment(resultSet));
            }

        } catch (SQLException e) {
            System.out.println("Get treatments error: " + e.getMessage());
        }

        return treatmentList;
    }

    // Get Treatments by Patient ID
    public List<Treatment> getTreatmentsByPatientId(int patientId) {

        List<Treatment> treatmentList = new ArrayList<>();

        String sql = "SELECT * FROM treatments WHERE patient_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, patientId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                treatmentList.add(extractTreatment(resultSet));
            }

        } catch (SQLException e) {
            System.out.println("Get patient treatments error: "
                    + e.getMessage());
        }

        return treatmentList;
    }

    // Get Treatments by Dentist ID
    public List<Treatment> getTreatmentsByDentistId(int dentistId) {

        List<Treatment> treatmentList = new ArrayList<>();

        String sql = "SELECT * FROM treatments WHERE dentist_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, dentistId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                treatmentList.add(extractTreatment(resultSet));
            }

        } catch (SQLException e) {
            System.out.println("Get dentist treatments error: "
                    + e.getMessage());
        }

        return treatmentList;
    }

    // Update Treatment
    public boolean updateTreatment(Treatment treatment) {

        String sql = "UPDATE treatments SET "
                + "patient_id = ?, "
                + "dentist_id = ?, "
                + "treatment_name = ?, "
                + "description = ?, "
                + "cost = ?, "
                + "treatment_date = ? "
                + "WHERE treatment_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, treatment.getPatientId());
            statement.setInt(2, treatment.getDentistId());
            statement.setString(3, treatment.getTreatmentName());
            statement.setString(4, treatment.getDescription());
            statement.setDouble(5, treatment.getCost());
            statement.setDate(6, treatment.getTreatmentDate());
            statement.setInt(7, treatment.getTreatmentId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Update treatment error: " + e.getMessage());
            return false;
        }
    }

    // Delete Treatment
    public boolean deleteTreatment(int treatmentId) {

        String sql = "DELETE FROM treatments WHERE treatment_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, treatmentId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Delete treatment error: " + e.getMessage());
            return false;
        }
    }

    // Convert ResultSet to Treatment Object
    private Treatment extractTreatment(ResultSet resultSet)
            throws SQLException {

        Treatment treatment = new Treatment();

        treatment.setTreatmentId(
                resultSet.getInt("treatment_id")
        );

        treatment.setPatientId(
                resultSet.getInt("patient_id")
        );

        treatment.setDentistId(
                resultSet.getInt("dentist_id")
        );

        treatment.setTreatmentName(
                resultSet.getString("treatment_name")
        );

        treatment.setDescription(
                resultSet.getString("description")
        );

        treatment.setCost(
                resultSet.getDouble("cost")
        );

        treatment.setTreatmentDate(
                resultSet.getDate("treatment_date")
        );

        treatment.setCreatedAt(
                resultSet.getTimestamp("created_at")
        );

        return treatment;
    }
}