package sunrise.dental.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sunrise.dental.config.DBConnection;
import sunrise.dental.model.Bill;

public class BillDAO {

    // Add Bill
    public boolean addBill(Bill bill) {

        String sql = "INSERT INTO bills "
                + "(patient_id, appointment_id, total_amount, bill_date, status) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bill.getPatientId());
            statement.setInt(2, bill.getAppointmentId());
            statement.setBigDecimal(3, bill.getTotalAmount());
            statement.setDate(4, bill.getBillDate());
            statement.setString(5, bill.getStatus());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Add bill error: " + e.getMessage());
            return false;
        }
    }

    // Get Bill by ID
    public Bill getBillById(int billId) {

        String sql = "SELECT * FROM bills WHERE bill_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, billId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                Bill bill = new Bill();

                bill.setBillId(
                        resultSet.getInt("bill_id")
                );

                bill.setPatientId(
                        resultSet.getInt("patient_id")
                );

                bill.setAppointmentId(
                        resultSet.getInt("appointment_id")
                );

                bill.setTotalAmount(
                        resultSet.getBigDecimal("total_amount")
                );

                bill.setBillDate(
                        resultSet.getDate("bill_date")
                );

                bill.setStatus(
                        resultSet.getString("status")
                );

                return bill;
            }

        } catch (SQLException e) {
            System.out.println("Get bill error: " + e.getMessage());
        }

        return null;
    }

    // Get All Bills
    public List<Bill> getAllBills() {

        List<Bill> billList = new ArrayList<>();

        String sql = "SELECT * FROM bills";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Bill bill = new Bill();

                bill.setBillId(
                        resultSet.getInt("bill_id")
                );

                bill.setPatientId(
                        resultSet.getInt("patient_id")
                );

                bill.setAppointmentId(
                        resultSet.getInt("appointment_id")
                );

                bill.setTotalAmount(
                        resultSet.getBigDecimal("total_amount")
                );

                bill.setBillDate(
                        resultSet.getDate("bill_date")
                );

                bill.setStatus(
                        resultSet.getString("status")
                );

                billList.add(bill);
            }

        } catch (SQLException e) {
            System.out.println("Get bills error: " + e.getMessage());
        }

        return billList;
    }

    // Update Bill
    public boolean updateBill(Bill bill) {

        String sql = "UPDATE bills SET "
                + "patient_id = ?, "
                + "appointment_id = ?, "
                + "total_amount = ?, "
                + "bill_date = ?, "
                + "status = ? "
                + "WHERE bill_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bill.getPatientId());
            statement.setInt(2, bill.getAppointmentId());
            statement.setBigDecimal(3, bill.getTotalAmount());
            statement.setDate(4, bill.getBillDate());
            statement.setString(5, bill.getStatus());
            statement.setInt(6, bill.getBillId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Update bill error: " + e.getMessage());
            return false;
        }
    }

    // Delete Bill
    public boolean deleteBill(int billId) {

        String sql = "DELETE FROM bills WHERE bill_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, billId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Delete bill error: " + e.getMessage());
            return false;
        }
    }

    // Get Bills by Patient ID
    public List<Bill> getBillsByPatientId(int patientId) {

        List<Bill> billList = new ArrayList<>();

        String sql = "SELECT * FROM bills WHERE patient_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, patientId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Bill bill = new Bill();

                bill.setBillId(
                        resultSet.getInt("bill_id")
                );

                bill.setPatientId(
                        resultSet.getInt("patient_id")
                );

                bill.setAppointmentId(
                        resultSet.getInt("appointment_id")
                );

                bill.setTotalAmount(
                        resultSet.getBigDecimal("total_amount")
                );

                bill.setBillDate(
                        resultSet.getDate("bill_date")
                );

                bill.setStatus(
                        resultSet.getString("status")
                );

                billList.add(bill);
            }

        } catch (SQLException e) {
            System.out.println("Get patient bills error: " + e.getMessage());
        }

        return billList;
    }
}