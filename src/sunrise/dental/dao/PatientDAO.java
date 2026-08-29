package sunrise.dental.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sunrise.dental.config.DBConnection;
import sunrise.dental.model.Patient;

public class PatientDAO {

    // Add Patient
    public boolean addPatient(Patient patient) {

        String sql = "INSERT INTO patients "
                + "(first_name, last_name, date_of_birth, gender, "
                + "phone, email, address, emergency_contact) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, patient.getFirstName());
            statement.setString(2, patient.getLastName());
            statement.setDate(3, patient.getDateOfBirth());
            statement.setString(4, patient.getGender());
            statement.setString(5, patient.getPhone());
            statement.setString(6, patient.getEmail());
            statement.setString(7, patient.getAddress());
            statement.setString(8, patient.getEmergencyContact());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Add patient error: " + e.getMessage());
            return false;
        }
    }

    // Find Patient by ID
    public Patient getPatientById(int patientId) {

        String sql = "SELECT * FROM patients WHERE patient_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, patientId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                Patient patient = new Patient();

                patient.setPatientId(
                        resultSet.getInt("patient_id")
                );

                patient.setFirstName(
                        resultSet.getString("first_name")
                );

                patient.setLastName(
                        resultSet.getString("last_name")
                );

                patient.setDateOfBirth(
                        resultSet.getDate("date_of_birth")
                );

                patient.setGender(
                        resultSet.getString("gender")
                );

                patient.setPhone(
                        resultSet.getString("phone")
                );

                patient.setEmail(
                        resultSet.getString("email")
                );

                patient.setAddress(
                        resultSet.getString("address")
                );

                patient.setEmergencyContact(
                        resultSet.getString("emergency_contact")
                );

                patient.setCreatedAt(
                        resultSet.getTimestamp("created_at")
                );

                return patient;
            }

        } catch (SQLException e) {
            System.out.println("Find patient error: " + e.getMessage());
        }

        return null;
    }

    // Get All Patients
    public List<Patient> getAllPatients() {

        List<Patient> patientList = new ArrayList<>();

        String sql = "SELECT * FROM patients";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Patient patient = new Patient();

                patient.setPatientId(
                        resultSet.getInt("patient_id")
                );

                patient.setFirstName(
                        resultSet.getString("first_name")
                );

                patient.setLastName(
                        resultSet.getString("last_name")
                );

                patient.setDateOfBirth(
                        resultSet.getDate("date_of_birth")
                );

                patient.setGender(
                        resultSet.getString("gender")
                );

                patient.setPhone(
                        resultSet.getString("phone")
                );

                patient.setEmail(
                        resultSet.getString("email")
                );

                patient.setAddress(
                        resultSet.getString("address")
                );

                patient.setEmergencyContact(
                        resultSet.getString("emergency_contact")
                );

                patient.setCreatedAt(
                        resultSet.getTimestamp("created_at")
                );

                patientList.add(patient);
            }

        } catch (SQLException e) {
            System.out.println("Get patients error: " + e.getMessage());
        }

        return patientList;
    }

    // Update Patient
    public boolean updatePatient(Patient patient) {

        String sql = "UPDATE patients SET "
                + "first_name = ?, "
                + "last_name = ?, "
                + "date_of_birth = ?, "
                + "gender = ?, "
                + "phone = ?, "
                + "email = ?, "
                + "address = ?, "
                + "emergency_contact = ? "
                + "WHERE patient_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, patient.getFirstName());
            statement.setString(2, patient.getLastName());
            statement.setDate(3, patient.getDateOfBirth());
            statement.setString(4, patient.getGender());
            statement.setString(5, patient.getPhone());
            statement.setString(6, patient.getEmail());
            statement.setString(7, patient.getAddress());
            statement.setString(8, patient.getEmergencyContact());
            statement.setInt(9, patient.getPatientId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Update patient error: " + e.getMessage());
            return false;
        }
    }

    // Delete Patient
    public boolean deletePatient(int patientId) {

        String sql = "DELETE FROM patients WHERE patient_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, patientId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Delete patient error: " + e.getMessage());
            return false;
        }
    }

    // Search Patients by Name
    public List<Patient> searchPatients(String keyword) {

        List<Patient> patientList = new ArrayList<>();

        String sql = "SELECT * FROM patients "
                + "WHERE first_name LIKE ? "
                + "OR last_name LIKE ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            String searchKeyword = "%" + keyword + "%";

            statement.setString(1, searchKeyword);
            statement.setString(2, searchKeyword);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Patient patient = new Patient();

                patient.setPatientId(
                        resultSet.getInt("patient_id")
                );

                patient.setFirstName(
                        resultSet.getString("first_name")
                );

                patient.setLastName(
                        resultSet.getString("last_name")
                );

                patient.setDateOfBirth(
                        resultSet.getDate("date_of_birth")
                );

                patient.setGender(
                        resultSet.getString("gender")
                );

                patient.setPhone(
                        resultSet.getString("phone")
                );

                patient.setEmail(
                        resultSet.getString("email")
                );

                patient.setAddress(
                        resultSet.getString("address")
                );

                patient.setEmergencyContact(
                        resultSet.getString("emergency_contact")
                );

                patient.setCreatedAt(
                        resultSet.getTimestamp("created_at")
                );

                patientList.add(patient);
            }

        } catch (SQLException e) {
            System.out.println("Search patient error: " + e.getMessage());
        }

        return patientList;
    }
}