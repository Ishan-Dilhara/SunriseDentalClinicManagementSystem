package sunrise.dental.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Treatment {

    private int treatmentId;
    private int patientId;
    private int dentistId;
    private String treatmentName;
    private String description;
    private double cost;
    private Date treatmentDate;
    private Timestamp createdAt;

    public Treatment() {
    }

    public Treatment(int treatmentId, int patientId, int dentistId,
                     String treatmentName, String description,
                     double cost, Date treatmentDate,
                     Timestamp createdAt) {

        this.treatmentId = treatmentId;
        this.patientId = patientId;
        this.dentistId = dentistId;
        this.treatmentName = treatmentName;
        this.description = description;
        this.cost = cost;
        this.treatmentDate = treatmentDate;
        this.createdAt = createdAt;
    }

    public int getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(int treatmentId) {
        this.treatmentId = treatmentId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDentistId() {
        return dentistId;
    }

    public void setDentistId(int dentistId) {
        this.dentistId = dentistId;
    }

    public String getTreatmentName() {
        return treatmentName;
    }

    public void setTreatmentName(String treatmentName) {
        this.treatmentName = treatmentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Date getTreatmentDate() {
        return treatmentDate;
    }

    public void setTreatmentDate(Date treatmentDate) {
        this.treatmentDate = treatmentDate;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}