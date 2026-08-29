package sunrise.dental.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sunrise.dental.config.DBConnection;
import sunrise.dental.model.Report;

public class ReportDAO {

    // Save Report
    public boolean saveReport(Report report) {

        String sql = "INSERT INTO reports "
                + "(report_type, report_date, start_date, end_date, "
                + "total_appointments, total_patients, total_revenue, generated_by) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, report.getReportType());
            statement.setDate(2, report.getReportDate());
            statement.setDate(3, report.getStartDate());
            statement.setDate(4, report.getEndDate());
            statement.setInt(5, report.getTotalAppointments());
            statement.setInt(6, report.getTotalPatients());
            statement.setBigDecimal(7, report.getTotalRevenue());
            statement.setString(8, report.getGeneratedBy());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Save report error: " + e.getMessage());
            return false;
        }
    }

    // Get Report by ID
    public Report getReportById(int reportId) {

        String sql = "SELECT * FROM reports WHERE report_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, reportId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                Report report = new Report();

                report.setReportId(
                        resultSet.getInt("report_id")
                );

                report.setReportType(
                        resultSet.getString("report_type")
                );

                report.setReportDate(
                        resultSet.getDate("report_date")
                );

                report.setStartDate(
                        resultSet.getDate("start_date")
                );

                report.setEndDate(
                        resultSet.getDate("end_date")
                );

                report.setTotalAppointments(
                        resultSet.getInt("total_appointments")
                );

                report.setTotalPatients(
                        resultSet.getInt("total_patients")
                );

                report.setTotalRevenue(
                        resultSet.getBigDecimal("total_revenue")
                );

                report.setGeneratedBy(
                        resultSet.getString("generated_by")
                );

                return report;
            }

        } catch (SQLException e) {
            System.out.println("Get report error: " + e.getMessage());
        }

        return null;
    }

    // Get All Reports
    public List<Report> getAllReports() {

        List<Report> reportList = new ArrayList<>();

        String sql = "SELECT * FROM reports ORDER BY report_date DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Report report = new Report();

                report.setReportId(
                        resultSet.getInt("report_id")
                );

                report.setReportType(
                        resultSet.getString("report_type")
                );

                report.setReportDate(
                        resultSet.getDate("report_date")
                );

                report.setStartDate(
                        resultSet.getDate("start_date")
                );

                report.setEndDate(
                        resultSet.getDate("end_date")
                );

                report.setTotalAppointments(
                        resultSet.getInt("total_appointments")
                );

                report.setTotalPatients(
                        resultSet.getInt("total_patients")
                );

                report.setTotalRevenue(
                        resultSet.getBigDecimal("total_revenue")
                );

                report.setGeneratedBy(
                        resultSet.getString("generated_by")
                );

                reportList.add(report);
            }

        } catch (SQLException e) {
            System.out.println("Get reports error: " + e.getMessage());
        }

        return reportList;
    }

    // Get Reports by Date Range
    public List<Report> getReportsByDateRange(
            Date startDate,
            Date endDate) {

        List<Report> reportList = new ArrayList<>();

        String sql = "SELECT * FROM reports "
                + "WHERE report_date BETWEEN ? AND ? "
                + "ORDER BY report_date DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDate(1, startDate);
            statement.setDate(2, endDate);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Report report = new Report();

                report.setReportId(
                        resultSet.getInt("report_id")
                );

                report.setReportType(
                        resultSet.getString("report_type")
                );

                report.setReportDate(
                        resultSet.getDate("report_date")
                );

                report.setStartDate(
                        resultSet.getDate("start_date")
                );

                report.setEndDate(
                        resultSet.getDate("end_date")
                );

                report.setTotalAppointments(
                        resultSet.getInt("total_appointments")
                );

                report.setTotalPatients(
                        resultSet.getInt("total_patients")
                );

                report.setTotalRevenue(
                        resultSet.getBigDecimal("total_revenue")
                );

                report.setGeneratedBy(
                        resultSet.getString("generated_by")
                );

                reportList.add(report);
            }

        } catch (SQLException e) {
            System.out.println("Get reports by date error: " + e.getMessage());
        }

        return reportList;
    }

    // Delete Report
    public boolean deleteReport(int reportId) {

        String sql = "DELETE FROM reports WHERE report_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, reportId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Delete report error: " + e.getMessage());
            return false;
        }
    }

    // Get Total Patients
    public int getTotalPatients() {

        String sql = "SELECT COUNT(*) AS total FROM patients";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }

        } catch (SQLException e) {
            System.out.println("Total patients error: " + e.getMessage());
        }

        return 0;
    }

    // Get Total Appointments
    public int getTotalAppointments() {

        String sql = "SELECT COUNT(*) AS total FROM appointments";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }

        } catch (SQLException e) {
            System.out.println("Total appointments error: " + e.getMessage());
        }

        return 0;
    }

    // Get Total Revenue
    public BigDecimal getTotalRevenue() {

        String sql = "SELECT COALESCE(SUM(amount), 0) AS total "
                + "FROM payments";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getBigDecimal("total");
            }

        } catch (SQLException e) {
            System.out.println("Total revenue error: " + e.getMessage());
        }

        return BigDecimal.ZERO;
    }

    // Get Total Payments
    public int getTotalPayments() {

        String sql = "SELECT COUNT(*) AS total FROM payments";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }

        } catch (SQLException e) {
            System.out.println("Total payments error: " + e.getMessage());
        }

        return 0;
    }
}