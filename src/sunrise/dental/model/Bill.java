package sunrise.dental.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Bill {

    private int billId;
    private int patientId;
    private int appointmentId;
    private BigDecimal totalAmount;
    private Date billDate;
    private String status;

    public Bill() {
    }

    public Bill(int billId, int patientId, int appointmentId,
                BigDecimal totalAmount, Date billDate, String status) {

        this.billId = billId;
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.totalAmount = totalAmount;
        this.billDate = billDate;
        this.status = status;
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

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}