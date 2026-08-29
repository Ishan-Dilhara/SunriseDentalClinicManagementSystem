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
import sunrise.dental.model.Receipt;

public class ReceiptDAO {

    // Add Receipt
    public boolean addReceipt(Receipt receipt) {

        String sql = "INSERT INTO receipts "
                + "(payment_id, bill_id, patient_id, patient_name, "
                + "amount, payment_method, receipt_date) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, receipt.getPaymentId());
            statement.setInt(2, receipt.getBillId());
            statement.setInt(3, receipt.getPatientId());
            statement.setString(4, receipt.getPatientName());
            statement.setBigDecimal(5, receipt.getAmount());
            statement.setString(6, receipt.getPaymentMethod());
            statement.setDate(7, receipt.getReceiptDate());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Add receipt error: " + e.getMessage());
            return false;
        }
    }

    // Get Receipt by ID
    public Receipt getReceiptById(int receiptId) {

        String sql = "SELECT * FROM receipts WHERE receipt_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, receiptId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return createReceiptFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            System.out.println("Get receipt error: " + e.getMessage());
        }

        return null;
    }

    // Get All Receipts
    public List<Receipt> getAllReceipts() {

        List<Receipt> receiptList = new ArrayList<>();

        String sql = "SELECT * FROM receipts";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                receiptList.add(
                        createReceiptFromResultSet(resultSet)
                );
            }

        } catch (SQLException e) {
            System.out.println("Get receipts error: " + e.getMessage());
        }

        return receiptList;
    }

    // Update Receipt
    public boolean updateReceipt(Receipt receipt) {

        String sql = "UPDATE receipts SET "
                + "payment_id = ?, "
                + "bill_id = ?, "
                + "patient_id = ?, "
                + "patient_name = ?, "
                + "amount = ?, "
                + "payment_method = ?, "
                + "receipt_date = ? "
                + "WHERE receipt_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, receipt.getPaymentId());
            statement.setInt(2, receipt.getBillId());
            statement.setInt(3, receipt.getPatientId());
            statement.setString(4, receipt.getPatientName());
            statement.setBigDecimal(5, receipt.getAmount());
            statement.setString(6, receipt.getPaymentMethod());
            statement.setDate(7, receipt.getReceiptDate());
            statement.setInt(8, receipt.getReceiptId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Update receipt error: " + e.getMessage());
            return false;
        }
    }

    // Delete Receipt
    public boolean deleteReceipt(int receiptId) {

        String sql = "DELETE FROM receipts WHERE receipt_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, receiptId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Delete receipt error: " + e.getMessage());
            return false;
        }
    }

    // Get Receipts by Payment ID
    public List<Receipt> getReceiptsByPaymentId(int paymentId) {

        List<Receipt> receiptList = new ArrayList<>();

        String sql = "SELECT * FROM receipts WHERE payment_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, paymentId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                receiptList.add(
                        createReceiptFromResultSet(resultSet)
                );
            }

        } catch (SQLException e) {
            System.out.println("Get payment receipts error: " + e.getMessage());
        }

        return receiptList;
    }

    // Create Receipt Object from ResultSet
    private Receipt createReceiptFromResultSet(ResultSet resultSet)
            throws SQLException {

        int receiptId = resultSet.getInt("receipt_id");
        int paymentId = resultSet.getInt("payment_id");
        int billId = resultSet.getInt("bill_id");
        int patientId = resultSet.getInt("patient_id");
        String patientName = resultSet.getString("patient_name");
        BigDecimal amount = resultSet.getBigDecimal("amount");
        String paymentMethod = resultSet.getString("payment_method");
        Date receiptDate = resultSet.getDate("receipt_date");

        return new Receipt(
                receiptId,
                paymentId,
                billId,
                patientId,
                patientName,
                amount,
                paymentMethod,
                receiptDate
        );
    }
}