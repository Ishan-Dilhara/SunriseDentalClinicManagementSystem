package sunrise.dental.model;

import java.sql.Timestamp;

public class Dentist {

    private int dentistId;
    private String firstName;
    private String lastName;
    private String specialization;
    private String phone;
    private String email;
    private String licenseNumber;
    private String status;
    private Timestamp createdAt;

    public Dentist() {
    }

    public Dentist(int dentistId, String firstName, String lastName,
                   String specialization, String phone, String email,
                   String licenseNumber, String status, Timestamp createdAt) {

        this.dentistId = dentistId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
        this.licenseNumber = licenseNumber;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getDentistId() {
        return dentistId;
    }

    public void setDentistId(int dentistId) {
        this.dentistId = dentistId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}