package sunrise.dental.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sunrise.dental.config.DBConnection;
import sunrise.dental.model.Appointment;

public class AppointmentDAO {

    // Add Appointment
    public boolean addAppointment(Appointment appointment) {

        String sql = "INSERT INTO appointments "
                + "(patient_id, dentist_id, appointment_date, "
                + "appointment_time, reason, status, notes) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, appointment.getPatientId());
            statement.setInt(2, appointment.getDentistId());
            statement.setDate(3, appointment.getAppointmentDate());
            statement.setTime(4, appointment.getAppointmentTime());
            statement.setString(5, appointment.getReason());
            statement.setString(6, appointment.getStatus());
            statement.setString(7, appointment.getNotes());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Add appointment error: " + e.getMessage());
            return false;
        }
    }

    // Find Appointment by ID
    public Appointment getAppointmentById(int appointmentId) {

        String sql = "SELECT * FROM appointments WHERE appointment_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, appointmentId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                return extractAppointment(resultSet);
            }

        } catch (SQLException e) {
            System.out.println("Find appointment error: " + e.getMessage());
        }

        return null;
    }

    // Get All Appointments
    public List<Appointment> getAllAppointments() {

        List<Appointment> appointmentList = new ArrayList<>();

        String sql = "SELECT * FROM appointments";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                appointmentList.add(extractAppointment(resultSet));
            }

        } catch (SQLException e) {
            System.out.println("Get appointments error: " + e.getMessage());
        }

        return appointmentList;
    }

    // Get Appointments by Patient ID
    public List<Appointment> getAppointmentsByPatientId(int patientId) {

        List<Appointment> appointmentList = new ArrayList<>();

        String sql = "SELECT * FROM appointments WHERE patient_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, patientId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                appointmentList.add(extractAppointment(resultSet));
            }

        } catch (SQLException e) {
            System.out.println("Get patient appointments error: "
                    + e.getMessage());
        }

        return appointmentList;
    }

    // Get Appointments by Dentist ID
    public List<Appointment> getAppointmentsByDentistId(int dentistId) {

        List<Appointment> appointmentList = new ArrayList<>();

        String sql = "SELECT * FROM appointments WHERE dentist_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, dentistId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                appointmentList.add(extractAppointment(resultSet));
            }

        } catch (SQLException e) {
            System.out.println("Get dentist appointments error: "
                    + e.getMessage());
        }

        return appointmentList;
    }

    // Update Appointment
    public boolean updateAppointment(Appointment appointment) {

        String sql = "UPDATE appointments SET "
                + "patient_id = ?, "
                + "dentist_id = ?, "
                + "appointment_date = ?, "
                + "appointment_time = ?, "
                + "reason = ?, "
                + "status = ?, "
                + "notes = ? "
                + "WHERE appointment_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, appointment.getPatientId());
            statement.setInt(2, appointment.getDentistId());
            statement.setDate(3, appointment.getAppointmentDate());
            statement.setTime(4, appointment.getAppointmentTime());
            statement.setString(5, appointment.getReason());
            statement.setString(6, appointment.getStatus());
            statement.setString(7, appointment.getNotes());
            statement.setInt(8, appointment.getAppointmentId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Update appointment error: " + e.getMessage());
            return false;
        }
    }

    // Update Appointment Status
    public boolean updateAppointmentStatus(int appointmentId,
                                           String status) {

        String sql = "UPDATE appointments SET status = ? "
                + "WHERE appointment_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status);
            statement.setInt(2, appointmentId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Update appointment status error: "
                    + e.getMessage());
            return false;
        }
    }

    // Delete Appointment
    public boolean deleteAppointment(int appointmentId) {

        String sql = "DELETE FROM appointments "
                + "WHERE appointment_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, appointmentId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Delete appointment error: " + e.getMessage());
            return false;
        }
    }

    // Convert ResultSet to Appointment Object
    private Appointment extractAppointment(ResultSet resultSet)
            throws SQLException {

        Appointment appointment = new Appointment();

        appointment.setAppointmentId(
                resultSet.getInt("appointment_id")
        );

        appointment.setPatientId(
                resultSet.getInt("patient_id")
        );

        appointment.setDentistId(
                resultSet.getInt("dentist_id")
        );

        appointment.setAppointmentDate(
                resultSet.getDate("appointment_date")
        );

        appointment.setAppointmentTime(
                resultSet.getTime("appointment_time")
        );

        appointment.setReason(
                resultSet.getString("reason")
        );

        appointment.setStatus(
                resultSet.getString("status")
        );

        appointment.setNotes(
                resultSet.getString("notes")
        );

        appointment.setCreatedAt(
                resultSet.getTimestamp("created_at")
        );

        return appointment;
    }
}