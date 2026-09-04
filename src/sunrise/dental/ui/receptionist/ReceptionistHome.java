/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package sunrise.dental.ui.receptionist;

import javax.swing.plaf.basic.BasicInternalFrameUI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import sunrise.dental.config.DBConnection;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReceptionistHome extends javax.swing.JInternalFrame {
    
    private int selectedAppointmentId = -1;
    private int lastPaymentId = -1;

    public ReceptionistHome() {
        initComponents();

        removeInternalFrameTitleBar();

        setBorder(null);

        setClosable(false);
        setIconifiable(false);
        setMaximizable(false);
        setResizable(false);

        // =========================================
        // Hard-coded Payment Method Options
        // =========================================
        jComboBox2.removeAllItems();

        jComboBox2.addItem("-- Select Payment Method --");
        jComboBox2.addItem("Cash");
        jComboBox2.addItem("Card");
        jComboBox2.addItem("Bank Transfer");

        // Select default option
        jComboBox2.setSelectedIndex(0);

        // Clear patient details when form opens
        clearPatientDetails();

        // Load completed patients
        loadCompletedPatients();

        // Select default patient option
        jComboBox1.setSelectedIndex(0);
    }
    
    private void loadCompletedPatients() {
        jComboBox1.removeAllItems();

        // Default option
        jComboBox1.addItem("Select Patient");

        String sql = """
            SELECT DISTINCT
                   a.appointment_id,
                   p.patient_id,
                   p.first_name,
                   p.last_name
            FROM appointments a
            INNER JOIN patients p
                ON a.patient_id = p.patient_id
            WHERE a.status_id = 3
            ORDER BY p.first_name, p.last_name
            """;

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery()
        ) {

            while (rs.next()) {

                int appointmentId =
                        rs.getInt("appointment_id");

                String patientName =
                        rs.getString("first_name")
                        + " "
                        + rs.getString("last_name");

                // ComboBox එකට patient name එක පමණක් පෙන්වයි
                jComboBox1.addItem(patientName);
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading completed patients:\n"
                    + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }
    
    private void removeInternalFrameTitleBar() {

        BasicInternalFrameUI ui = (BasicInternalFrameUI) getUI();

        if (ui != null) {
            ui.setNorthPane(null);
        }

        setBorder(null);

        revalidate();
        repaint();
    }
    
    private void loadSelectedPatientDetails(String patientName) {
        String sql = """
            SELECT
                p.patient_id,
                p.first_name,
                p.last_name,
                p.phone,
                p.address,

                a.appointment_id,
                a.appointment_date,
                a.appointment_time,
                a.reason,

                CONCAT(u.first_name, ' ', u.last_name) AS dentist_name

            FROM patients p

            INNER JOIN appointments a
                ON p.patient_id = a.patient_id

            INNER JOIN users u
                ON a.dentist_id = u.user_id

            WHERE CONCAT(p.first_name, ' ', p.last_name) = ?
              AND a.status_id = 3

            ORDER BY a.appointment_date DESC,
                     a.appointment_time DESC

            LIMIT 1
            """;

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(sql)
        ) {

            pst.setString(1, patientName);

            try (ResultSet rs = pst.executeQuery()) {

                if (rs.next()) {

                    // =========================================
                    // Appointment ID
                    // =========================================

                    selectedAppointmentId =
                            rs.getInt("appointment_id");

                    jLabel2.setText(
                            String.valueOf(selectedAppointmentId)
                    );

                    // =========================================
                    // Date & Time
                    // =========================================

                    jLabel3.setText(
                            rs.getDate("appointment_date")
                            + " "
                            + rs.getTime("appointment_time")
                    );

                    // =========================================
                    // Reason
                    // =========================================

                    jLabel5.setText(
                            rs.getString("reason")
                    );

                    // =========================================
                    // Dentist Name
                    // =========================================

                    jLabel7.setText(
                            rs.getString("dentist_name")
                    );

                    // =========================================
                    // Patient Information
                    // =========================================

                    jLabel15.setText(
                            rs.getString("first_name")
                            + " "
                            + rs.getString("last_name")
                    );

                    jLabel22.setText(
                            rs.getString("phone")
                    );

                    jLabel23.setText(
                            rs.getString("address")
                    );

                    // =========================================
                    // Load Treatments
                    // =========================================

                    int appointmentId =
                            rs.getInt("appointment_id");

                    loadTreatments(appointmentId);

                } else {

                    clearPatientDetails();

                    JOptionPane.showMessageDialog(
                            this,
                            "No completed appointment found for this patient.",
                            "Information",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading patient details:\n"
                    + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void loadTreatments(int appointmentId) {
        DefaultTableModel model =
                (DefaultTableModel) jTable1.getModel();

        // Clear previous patient's treatments
        model.setRowCount(0);

        double totalCost = 0.00;

        String sql = """
            SELECT
                t.treatment_name,
                at.cost

            FROM appointment_treatments at

            INNER JOIN treatments t
                ON at.treatment_id = t.treatment_id

            WHERE at.appointment_id = ?

            ORDER BY at.appointment_treatment_id
            """;

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(sql)
        ) {

            pst.setInt(1, appointmentId);

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {

                    String treatmentName =
                            rs.getString("treatment_name");

                    double cost =
                            rs.getDouble("cost");

                    // Add treatment to table
                    model.addRow(new Object[]{
                        treatmentName,
                        String.format("%.2f", cost)
                    });

                    // Calculate total
                    totalCost += cost;
                }
            }

            // Display total
            jLabel9.setText(
                    String.format("%.2f", totalCost)
            );

            // Automatically put total into Paid Amount
            jTextField1.setText(
                    String.format("%.2f", totalCost)
            );

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading treatments:\n"
                    + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();

            jLabel9.setText("0.00");
            jTextField1.setText("");
        }
    }
    
    private void clearPatientDetails() {

        selectedAppointmentId = -1;

        jLabel2.setText("");
        jLabel3.setText("");
        jLabel5.setText("");
        jLabel7.setText("");

        jLabel15.setText("");
        jLabel22.setText("");
        jLabel23.setText("");

        jLabel9.setText("0.00");

        jTextField1.setText("");

        // Reset payment method
        if (jComboBox2.getItemCount() > 0) {
            jComboBox2.setSelectedIndex(0);
        }

        DefaultTableModel model =
                (DefaultTableModel) jTable1.getModel();

        model.setRowCount(0);
    }
    
    private void generateReceiptPDF(int paymentId, int billId, int appointmentId) {

        String patientName = "";
        String phone = "";
        String address = "";
        String dentistName = "";
        String appointmentDate = "";
        String reason = "";

        double totalAmount = 0.00;
        double paidAmount = 0.00;
        String paymentMethod = "";
        String paymentStatus = "";

        String pdfFolderPath =
                System.getProperty("user.home")
                + File.separator
                + "SunriseDentalReceipts";

        File pdfFolder = new File(pdfFolderPath);

        if (!pdfFolder.exists()) {
            pdfFolder.mkdirs();
        }

        String filePath =
                pdfFolderPath
                + File.separator
                + "Receipt_" + paymentId + ".pdf";

        String sql = """
            SELECT
                p.first_name,
                p.last_name,
                p.phone,
                p.address,

                a.appointment_date,
                a.reason,

                CONCAT(u.first_name, ' ', u.last_name) AS dentist_name,

                b.total_amount,

                py.amount,
                py.payment_method,
                py.payment_status

            FROM payments py

            INNER JOIN bills b
                ON py.bill_id = b.bill_id

            INNER JOIN appointments a
                ON b.appointment_id = a.appointment_id

            INNER JOIN patients p
                ON a.patient_id = p.patient_id

            INNER JOIN users u
                ON a.dentist_id = u.user_id

            WHERE py.payment_id = ?
            """;

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(sql)
        ) {

            pst.setInt(1, paymentId);

            try (ResultSet rs = pst.executeQuery()) {

                if (!rs.next()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Payment details not found.",
                            "Receipt Error",
                            JOptionPane.ERROR_MESSAGE
                    );

                    return;
                }

                // ==========================================
                // Patient Information
                // ==========================================

                patientName =
                        rs.getString("first_name")
                        + " "
                        + rs.getString("last_name");

                phone = rs.getString("phone");
                address = rs.getString("address");

                // ==========================================
                // Appointment Information
                // ==========================================

                appointmentDate =
                        rs.getDate("appointment_date").toString();

                reason = rs.getString("reason");

                dentistName =
                        rs.getString("dentist_name");

                // ==========================================
                // Payment Information
                // ==========================================

                totalAmount =
                        rs.getDouble("total_amount");

                paidAmount =
                        rs.getDouble("amount");

                paymentMethod =
                        rs.getString("payment_method");

                paymentStatus =
                        rs.getString("payment_status");
            }

            // ==========================================
            // Create PDF Document
            // ==========================================

            Document document = new Document();

            PdfWriter.getInstance(
                    document,
                    new FileOutputStream(filePath)
            );

            document.open();

            // ==========================================
            // Fonts
            // ==========================================

            Font titleFont =
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,
                            20,
                            BaseColor.BLACK
                    );

            Font headingFont =
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,
                            12,
                            BaseColor.BLACK
                    );

            Font normalFont =
                    FontFactory.getFont(
                            FontFactory.HELVETICA,
                            10,
                            BaseColor.BLACK
                    );

            // ==========================================
            // Clinic Title
            // ==========================================

            Paragraph title =
                    new Paragraph(
                            "SUNRISE DENTAL CLINIC",
                            titleFont
                    );

            title.setAlignment(Element.ALIGN_CENTER);

            document.add(title);

            Paragraph receiptTitle =
                    new Paragraph(
                            "PAYMENT RECEIPT",
                            headingFont
                    );

            receiptTitle.setAlignment(Element.ALIGN_CENTER);

            document.add(receiptTitle);

            document.add(new Paragraph(" "));

            // ==========================================
            // Receipt Information
            // ==========================================

            PdfPTable receiptInfo =
                    new PdfPTable(2);

            receiptInfo.setWidthPercentage(100);

            receiptInfo.addCell(
                    new PdfPCell(
                            new Phrase(
                                    "Receipt No",
                                    headingFont
                            )
                    )
            );

            receiptInfo.addCell(
                    new PdfPCell(
                            new Phrase(
                                    "REC-" + paymentId,
                                    normalFont
                            )
                    )
            );

            receiptInfo.addCell(
                    new PdfPCell(
                            new Phrase(
                                    "Bill No",
                                    headingFont
                            )
                    )
            );

            receiptInfo.addCell(
                    new PdfPCell(
                            new Phrase(
                                    String.valueOf(billId),
                                    normalFont
                            )
                    )
            );

            receiptInfo.addCell(
                    new PdfPCell(
                            new Phrase(
                                    "Appointment No",
                                    headingFont
                            )
                    )
            );

            receiptInfo.addCell(
                    new PdfPCell(
                            new Phrase(
                                    String.valueOf(appointmentId),
                                    normalFont
                            )
                    )
            );

            receiptInfo.addCell(
                    new PdfPCell(
                            new Phrase(
                                    "Appointment Date",
                                    headingFont
                            )
                    )
            );

            receiptInfo.addCell(
                    new PdfPCell(
                            new Phrase(
                                    appointmentDate,
                                    normalFont
                            )
                    )
            );

            document.add(receiptInfo);

            document.add(new Paragraph(" "));

            // ==========================================
            // Patient Information
            // ==========================================

            Paragraph patientHeading =
                    new Paragraph(
                            "PATIENT INFORMATION",
                            headingFont
                    );

            document.add(patientHeading);

            PdfPTable patientTable =
                    new PdfPTable(2);

            patientTable.setWidthPercentage(100);

            patientTable.addCell(
                    new PdfPCell(
                            new Phrase(
                                    "Name",
                                    headingFont
                            )
                    )
            );

            patientTable.addCell(
                    new PdfPCell(
                            new Phrase(
                                    patientName,
                                    normalFont
                            )
                    )
            );

            patientTable.addCell(
                    new PdfPCell(
                            new Phrase(
                                    "Phone",
                                    headingFont
                            )
                    )
            );

            patientTable.addCell(
                    new PdfPCell(
                            new Phrase(
                                    phone,
                                    normalFont
                            )
                    )
            );

            patientTable.addCell(
                    new PdfPCell(
                            new Phrase(
                                    "Address",
                                    headingFont
                            )
                    )
            );

            patientTable.addCell(
                    new PdfPCell(
                            new Phrase(
                                    address,
                                    normalFont
                            )
                    )
            );

            patientTable.addCell(
                    new PdfPCell(
                            new Phrase(
                                    "Dentist",
                                    headingFont
                            )
                    )
            );

            patientTable.addCell(
                    new PdfPCell(
                            new Phrase(
                                    dentistName,
                                    normalFont
                            )
                    )
            );

            patientTable.addCell(
                    new PdfPCell(
                            new Phrase(
                                    "Reason",
                                    headingFont
                            )
                    )
            );

            patientTable.addCell(
                    new PdfPCell(
                            new Phrase(
                                    reason,
                                    normalFont
                            )
                    )
            );

            document.add(patientTable);

            document.add(new Paragraph(" "));

            // ==========================================
            // Treatments
            // ==========================================

            Paragraph treatmentHeading =
                    new Paragraph(
                            "TREATMENTS",
                            headingFont
                    );

            document.add(treatmentHeading);

            PdfPTable treatmentTable =
                    new PdfPTable(2);

            treatmentTable.setWidthPercentage(100);

            PdfPCell treatmentHeader =
                    new PdfPCell(
                            new Phrase(
                                    "Treatment",
                                    headingFont
                            )
                    );

            PdfPCell costHeader =
                    new PdfPCell(
                            new Phrase(
                                    "Cost (LKR)",
                                    headingFont
                            )
                    );

            treatmentTable.addCell(treatmentHeader);
            treatmentTable.addCell(costHeader);

            String treatmentSQL = """
                SELECT
                    t.treatment_name,
                    at.cost

                FROM appointment_treatments at

                INNER JOIN treatments t
                    ON at.treatment_id = t.treatment_id

                WHERE at.appointment_id = ?

                ORDER BY at.appointment_treatment_id
                """;

            try (PreparedStatement pstTreatment =
                    con.prepareStatement(treatmentSQL)) {

                pstTreatment.setInt(
                        1,
                        appointmentId
                );

                try (ResultSet rsTreatment =
                        pstTreatment.executeQuery()) {

                    while (rsTreatment.next()) {

                        String treatmentName =
                                rsTreatment.getString(
                                        "treatment_name"
                                );

                        double cost =
                                rsTreatment.getDouble("cost");

                        treatmentTable.addCell(
                                new PdfPCell(
                                        new Phrase(
                                                treatmentName,
                                                normalFont
                                        )
                                )
                        );

                        treatmentTable.addCell(
                                new PdfPCell(
                                        new Phrase(
                                                String.format(
                                                        "%.2f",
                                                        cost
                                                ),
                                                normalFont
                                        )
                                )
                        );
                    }
                }
            }

            document.add(treatmentTable);

            document.add(new Paragraph(" "));

            // ==========================================
            // Payment Summary
            // ==========================================

            PdfPTable paymentTable =
                    new PdfPTable(2);

            paymentTable.setWidthPercentage(100);

            paymentTable.addCell(
                    new PdfPCell(
                            new Phrase(
                                    "Total Amount",
                                    headingFont
                            )
                    )
            );

            paymentTable.addCell(
                    new PdfPCell(
                            new Phrase(
                                    String.format(
                                            "LKR %.2f",
                                            totalAmount
                                    ),
                                    normalFont
                            )
                    )
            );

            paymentTable.addCell(
                    new PdfPCell(
                            new Phrase(
                                    "Paid Amount",
                                    headingFont
                            )
                    )
            );

            paymentTable.addCell(
                    new PdfPCell(
                            new Phrase(
                                    String.format(
                                            "LKR %.2f",
                                            paidAmount
                                    ),
                                    normalFont
                            )
                    )
            );

            paymentTable.addCell(
                    new PdfPCell(
                            new Phrase(
                                    "Payment Method",
                                    headingFont
                            )
                    )
            );

            paymentTable.addCell(
                    new PdfPCell(
                            new Phrase(
                                    paymentMethod,
                                    normalFont
                            )
                    )
            );

            paymentTable.addCell(
                    new PdfPCell(
                            new Phrase(
                                    "Payment Status",
                                    headingFont
                            )
                    )
            );

            paymentTable.addCell(
                    new PdfPCell(
                            new Phrase(
                                    paymentStatus,
                                    normalFont
                            )
                    )
            );

            document.add(paymentTable);

            document.add(new Paragraph(" "));

            Paragraph thankYou =
                    new Paragraph(
                            "Thank you for choosing Sunrise Dental Clinic.",
                            normalFont
                    );

            thankYou.setAlignment(
                    Element.ALIGN_CENTER
            );

            document.add(thankYou);

            // ==========================================
            // Close PDF
            // ==========================================

            document.close();

            // ==========================================
            // Open PDF in Google Chrome
            // ==========================================

            openPdfInChrome(filePath);

        } catch (SQLException |
                 DocumentException |
                 IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error generating receipt:\n"
                    + e.getMessage(),
                    "Receipt Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }
    
    private void openPdfInChrome(String filePath) {
        try {

            File pdfFile = new File(filePath);

            if (!pdfFile.exists()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Receipt PDF was not created.",
                        "Receipt Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            String chromePath = null;

            // ==========================================
            // Check Google Chrome Installation
            // ==========================================

            String[] possibleChromePaths = {

                "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe",

                "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe",

                System.getProperty("user.home")
                + "\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe"
            };

            for (String path : possibleChromePaths) {

                File chromeFile = new File(path);

                if (chromeFile.exists()) {

                    chromePath = path;

                    break;
                }
            }

            // ==========================================
            // Open with Chrome
            // ==========================================

            if (chromePath != null) {

                new ProcessBuilder(
                        chromePath,
                        pdfFile.getAbsolutePath()
                ).start();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Google Chrome was not found.",
                        "Chrome Error",
                        JOptionPane.WARNING_MESSAGE
                );

                // Fallback: open with default application
                if (Desktop.isDesktopSupported()) {

                    Desktop.getDesktop().open(pdfFile);
                }
            }

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to open receipt PDF:\n"
                    + e.getMessage(),
                    "PDF Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();

        jLabel4.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel4.setText("Manage patient payments and generate receipts");

        jLabel1.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        jLabel1.setText("Payment & Receipt");

        jLabel6.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jLabel6.setText("Completed Appointments");

        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jLabel16.setText("Date & Time :");

        jLabel14.setText("Appointment No :");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel17.setText("Reson :");

        jLabel18.setText("Dentist :");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel14)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Treatment", "Cost (LKR)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel8.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jLabel8.setText("Treatments");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel10.setText("Total Treatment Cost (LKR)");

        jLabel11.setText("Payment Method :");

        jLabel12.setText("Paid Amount :");

        jButton1.setText("Save Payment");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Clear");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel8)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 38, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton1))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(6, 6, 6)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel13.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jLabel13.setText("Patient Information");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel19.setText("Name");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel21.setText("Phone");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel24.setText("Address");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel19)
                            .addComponent(jLabel21)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(316, 316, 316)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {

            // "-- Select Patient --"
            if (jComboBox1.getSelectedIndex() <= 0) {

                selectedAppointmentId = -1;
                clearPatientDetails();
                return;
            }

            String selectedPatient =
                    jComboBox1.getSelectedItem().toString();

            loadSelectedPatientDetails(selectedPatient);
        }

    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // =====================================================
        // 1. Check Patient Selection
        // =====================================================

        if (jComboBox1.getSelectedIndex() <= 0
                || selectedAppointmentId == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a patient first.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        // =====================================================
        // 2. Check Payment Method
        // =====================================================

        if (jComboBox2.getSelectedIndex() <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a payment method.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String paymentMethod =
                jComboBox2.getSelectedItem().toString();


        // =====================================================
        // 3. Get Paid Amount
        // =====================================================

        double paidAmount;

        try {

            paidAmount = Double.parseDouble(
                    jTextField1.getText().trim()
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid paid amount.",
                    "Invalid Amount",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        // =====================================================
        // 4. Get Total Treatment Cost
        // =====================================================

        double totalAmount;

        try {

            totalAmount = Double.parseDouble(
                    jLabel9.getText()
                            .replace("LKR", "")
                            .replace(",", "")
                            .trim()
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid total amount.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        // =====================================================
        // 5. Validate Paid Amount
        // =====================================================

        if (paidAmount <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Paid amount must be greater than 0.",
                    "Invalid Amount",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        if (paidAmount > totalAmount) {

            JOptionPane.showMessageDialog(
                    this,
                    "Paid amount cannot be greater than the total bill.",
                    "Invalid Amount",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        // =====================================================
        // 6. Bill Status
        // =====================================================

        String billStatus;

        if (Math.abs(paidAmount - totalAmount) < 0.01) {

            billStatus = "Paid";

        } else {

            billStatus = "Partially Paid";
        }


        // =====================================================
        // 7. Payment Status
        // =====================================================

        String paymentStatus;

        if (Math.abs(paidAmount - totalAmount) < 0.01) {

            paymentStatus = "Paid";

        } else {

            paymentStatus = "Pending";
        }


        // =====================================================
        // Database Connection
        // =====================================================

        Connection con = null;

        int billId = -1;
        int paymentId = -1;


        try {

            // =================================================
            // 8. Connect Database
            // =================================================

            con = DBConnection.getConnection();

            con.setAutoCommit(false);


            // =================================================
            // 9. INSERT BILL
            // =================================================

            String billSQL = """
                INSERT INTO bills
                (
                    appointment_id,
                    total_amount,
                    bill_date,
                    status
                )
                VALUES
                (
                    ?,
                    ?,
                    CURDATE(),
                    ?
                )
                """;


            try (
                PreparedStatement pstBill =
                        con.prepareStatement(
                                billSQL,
                                Statement.RETURN_GENERATED_KEYS
                        )
            ) {

                pstBill.setInt(
                        1,
                        selectedAppointmentId
                );

                pstBill.setDouble(
                        2,
                        totalAmount
                );

                pstBill.setString(
                        3,
                        billStatus
                );

                pstBill.executeUpdate();


                // =============================================
                // Get Generated Bill ID
                // =============================================

                try (
                    ResultSet rs =
                            pstBill.getGeneratedKeys()
                ) {

                    if (!rs.next()) {

                        throw new SQLException(
                                "Unable to get generated Bill ID."
                        );
                    }

                    billId = rs.getInt(1);
                }
            }


            // =================================================
            // 10. INSERT PAYMENT
            // =================================================

            String paymentSQL = """
                INSERT INTO payments
                (
                    bill_id,
                    amount,
                    payment_method,
                    payment_date,
                    payment_status
                )
                VALUES
                (
                    ?,
                    ?,
                    ?,
                    CURDATE(),
                    ?
                )
                """;


            try (
                PreparedStatement pstPayment =
                        con.prepareStatement(
                                paymentSQL,
                                Statement.RETURN_GENERATED_KEYS
                        )
            ) {

                pstPayment.setInt(
                        1,
                        billId
                );

                pstPayment.setDouble(
                        2,
                        paidAmount
                );

                pstPayment.setString(
                        3,
                        paymentMethod
                );

                pstPayment.setString(
                        4,
                        paymentStatus
                );

                pstPayment.executeUpdate();


                // =============================================
                // Get Generated Payment ID
                // =============================================

                try (
                    ResultSet rs =
                            pstPayment.getGeneratedKeys()
                ) {

                    if (!rs.next()) {

                        throw new SQLException(
                                "Unable to get generated Payment ID."
                        );
                    }

                    paymentId = rs.getInt(1);
                }
            }


            // =================================================
            // 11. UPDATE APPOINTMENT STATUS
            // =================================================
            // status_id = 5
            // =================================================

            String updateAppointmentSQL = """
                UPDATE appointments
                SET status_id = 5
                WHERE appointment_id = ?
                """;


            try (
                PreparedStatement pstUpdate =
                        con.prepareStatement(
                                updateAppointmentSQL
                        )
            ) {

                pstUpdate.setInt(
                        1,
                        selectedAppointmentId
                );

                int updatedRows =
                        pstUpdate.executeUpdate();


                if (updatedRows == 0) {

                    throw new SQLException(
                            "Appointment status could not be updated."
                    );
                }
            }


            // =================================================
            // 12. INSERT RECEIPT
            // =================================================

            String receiptSQL = """
                INSERT INTO receipts
                (
                    payment_id,
                    receipt_date
                )
                VALUES
                (
                    ?,
                    CURDATE()
                )
                """;


            try (
                PreparedStatement pstReceipt =
                        con.prepareStatement(
                                receiptSQL
                        )
            ) {

                pstReceipt.setInt(
                        1,
                        paymentId
                );

                pstReceipt.executeUpdate();
            }


            // =================================================
            // 13. COMMIT TRANSACTION
            // =================================================

            con.commit();


            // =================================================
            // 14. Generate Receipt PDF
            // =================================================

            generateReceiptPDF(
                    paymentId,
                    billId,
                    selectedAppointmentId
            );


            // =================================================
            // 15. Success Message
            // =================================================

            JOptionPane.showMessageDialog(
                    this,
                    "Payment saved successfully!\n"
                    + "Appointment status updated to completed.\n"
                    + "Receipt generated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );


            // =================================================
            // 16. Clear Payment Fields
            // =================================================

            jTextField1.setText("");

            if (jComboBox2.getItemCount() > 0) {
                jComboBox2.setSelectedIndex(0);
            }


            // =================================================
            // 17. Clear Selected Appointment ID
            // =================================================

            selectedAppointmentId = -1;


        } catch (SQLException e) {

            // =================================================
            // ROLLBACK
            // =================================================

            try {

                if (con != null) {

                    con.rollback();
                }

            } catch (SQLException ex) {

                ex.printStackTrace();
            }


            JOptionPane.showMessageDialog(
                    this,
                    "Error saving payment:\n"
                    + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();


        } finally {

            // =================================================
            // CLOSE CONNECTION
            // =================================================

            try {

                if (con != null) {

                    con.setAutoCommit(true);
                    con.close();
                }

            } catch (SQLException e) {

                e.printStackTrace();
            }
        }

    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
