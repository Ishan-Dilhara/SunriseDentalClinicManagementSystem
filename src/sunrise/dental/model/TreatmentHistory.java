package sunrise.dental.model;

import java.sql.Date;

public class TreatmentHistory {

    private int historyId;
    private int patientId;
    private int dentistId;
    private int treatmentId;
    private String treatmentName;
    private String description;
    private Date treatmentDate;
    private String status;

    public TreatmentHistory() {
    }

    public TreatmentHistory(int historyId, int patientId, int dentistId,
                            int treatmentId, String treatmentName,
                            String description, Date treatmentDate,
                            String status) {

        this.historyId = historyId;
        this.patientId = patientId;
        this.dentistId = dentistId;
        this.treatmentId = treatmentId;
        this.treatmentName = treatmentName;
        this.description = description;
        this.treatmentDate = treatmentDate;
        this.status = status;
    }

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
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

    public int getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(int treatmentId) {
        this.treatmentId = treatmentId;
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

    public Date getTreatmentDate() {
        return treatmentDate;
    }

    public void setTreatmentDate(Date treatmentDate) {
        this.treatmentDate = treatmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}