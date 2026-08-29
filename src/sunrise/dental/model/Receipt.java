package sunrise.dental.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Receipt {

    private int receiptId;
    private int paymentId;
    private int billId;
    private int patientId;
    private String patientName;
    private BigDecimal amount;
    private String paymentMethod;
    private Date receiptDate;

    public Receipt() {
    }

    public Receipt(int receiptId, int paymentId, int billId,
                   int patientId, String patientName,
                   BigDecimal amount, String paymentMethod,
                   Date receiptDate) {

        this.receiptId = receiptId;
        this.paymentId = paymentId;
        this.billId = billId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.receiptDate = receiptDate;
    }

    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }
}