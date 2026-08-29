package sunrise.dental.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Payment {

    private int paymentId;
    private int billId;
    private BigDecimal amount;
    private String paymentMethod;
    private Date paymentDate;
    private String paymentStatus;

    public Payment() {
    }

    public Payment(int paymentId, int billId,
                   BigDecimal amount,
                   String paymentMethod,
                   Date paymentDate,
                   String paymentStatus) {

        this.paymentId = paymentId;
        this.billId = billId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
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

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}