package sunrise.dental.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sunrise.dental.config.DBConnection;
import sunrise.dental.model.Payment;

public class PaymentDAO {

    // Add Payment
    public boolean addPayment(Payment payment) {

        String sql = "INSERT INTO payments "
                + "(bill_id, amount, payment_method, payment_date, payment_status) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, payment.getBillId());
            statement.setBigDecimal(2, payment.getAmount());
            statement.setString(3, payment.getPaymentMethod());
            statement.setDate(4, payment.getPaymentDate());
            statement.setString(5, payment.getPaymentStatus());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Add payment error: " + e.getMessage());
            return false;
        }
    }

    // Get Payment by ID
    public Payment getPaymentById(int paymentId) {

        String sql = "SELECT * FROM payments WHERE payment_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, paymentId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                Payment payment = new Payment();

                payment.setPaymentId(
                        resultSet.getInt("payment_id")
                );

                payment.setBillId(
                        resultSet.getInt("bill_id")
                );

                payment.setAmount(
                        resultSet.getBigDecimal("amount")
                );

                payment.setPaymentMethod(
                        resultSet.getString("payment_method")
                );

                payment.setPaymentDate(
                        resultSet.getDate("payment_date")
                );

                payment.setPaymentStatus(
                        resultSet.getString("payment_status")
                );

                return payment;
            }

        } catch (SQLException e) {
            System.out.println("Get payment error: " + e.getMessage());
        }

        return null;
    }

    // Get All Payments
    public List<Payment> getAllPayments() {

        List<Payment> paymentList = new ArrayList<>();

        String sql = "SELECT * FROM payments";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Payment payment = new Payment();

                payment.setPaymentId(
                        resultSet.getInt("payment_id")
                );

                payment.setBillId(
                        resultSet.getInt("bill_id")
                );

                payment.setAmount(
                        resultSet.getBigDecimal("amount")
                );

                payment.setPaymentMethod(
                        resultSet.getString("payment_method")
                );

                payment.setPaymentDate(
                        resultSet.getDate("payment_date")
                );

                payment.setPaymentStatus(
                        resultSet.getString("payment_status")
                );

                paymentList.add(payment);
            }

        } catch (SQLException e) {
            System.out.println("Get payments error: " + e.getMessage());
        }

        return paymentList;
    }

    // Update Payment
    public boolean updatePayment(Payment payment) {

        String sql = "UPDATE payments SET "
                + "bill_id = ?, "
                + "amount = ?, "
                + "payment_method = ?, "
                + "payment_date = ?, "
                + "payment_status = ? "
                + "WHERE payment_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, payment.getBillId());
            statement.setBigDecimal(2, payment.getAmount());
            statement.setString(3, payment.getPaymentMethod());
            statement.setDate(4, payment.getPaymentDate());
            statement.setString(5, payment.getPaymentStatus());
            statement.setInt(6, payment.getPaymentId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Update payment error: " + e.getMessage());
            return false;
        }
    }

    // Delete Payment
    public boolean deletePayment(int paymentId) {

        String sql = "DELETE FROM payments WHERE payment_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, paymentId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Delete payment error: " + e.getMessage());
            return false;
        }
    }

    // Get Payments by Bill ID
    public List<Payment> getPaymentsByBillId(int billId) {

        List<Payment> paymentList = new ArrayList<>();

        String sql = "SELECT * FROM payments WHERE bill_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, billId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Payment payment = new Payment();

                payment.setPaymentId(
                        resultSet.getInt("payment_id")
                );

                payment.setBillId(
                        resultSet.getInt("bill_id")
                );

                payment.setAmount(
                        resultSet.getBigDecimal("amount")
                );

                payment.setPaymentMethod(
                        resultSet.getString("payment_method")
                );

                payment.setPaymentDate(
                        resultSet.getDate("payment_date")
                );

                payment.setPaymentStatus(
                        resultSet.getString("payment_status")
                );

                paymentList.add(payment);
            }

        } catch (SQLException e) {
            System.out.println("Get bill payments error: " + e.getMessage());
        }

        return paymentList;
    }
}