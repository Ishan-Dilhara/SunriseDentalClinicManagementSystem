package sunrise.dental.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Report {

    private int reportId;
    private String reportType;
    private Date reportDate;
    private Date startDate;
    private Date endDate;
    private int totalAppointments;
    private int totalPatients;
    private BigDecimal totalRevenue;
    private String generatedBy;

    public Report() {
    }

    public Report(int reportId, String reportType, Date reportDate,
                  Date startDate, Date endDate,
                  int totalAppointments, int totalPatients,
                  BigDecimal totalRevenue, String generatedBy) {

        this.reportId = reportId;
        this.reportType = reportType;
        this.reportDate = reportDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalAppointments = totalAppointments;
        this.totalPatients = totalPatients;
        this.totalRevenue = totalRevenue;
        this.generatedBy = generatedBy;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getTotalAppointments() {
        return totalAppointments;
    }

    public void setTotalAppointments(int totalAppointments) {
        this.totalAppointments = totalAppointments;
    }

    public int getTotalPatients() {
        return totalPatients;
    }

    public void setTotalPatients(int totalPatients) {
        this.totalPatients = totalPatients;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }
}