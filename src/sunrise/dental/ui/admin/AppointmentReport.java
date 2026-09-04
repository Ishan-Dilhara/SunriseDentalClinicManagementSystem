/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package sunrise.dental.ui.admin;

import javax.swing.plaf.basic.BasicInternalFrameUI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import sunrise.dental.config.DBConnection;

/**
 *
 * @author HP
 */
public class AppointmentReport extends javax.swing.JInternalFrame {
    private int selectedPatientId = -1;
    private int selectedDentistId = -1;
    private String selectedTime = "";
    
    public AppointmentReport() {
        initComponents();

        removeInternalFrameTitleBar();

        setBorder(null);

        setClosable(false);
        setIconifiable(false);
        setMaximizable(false);
        setResizable(false);

        loadPatients();
        loadDentists();
        updateTimeButtons();
        loadAppointments();
    }
    
    private void loadPatients() {
        try {

            jComboBox1.removeAllItems();

            String sql = "SELECT patient_id, first_name, last_name "
                    + "FROM patients "
                    + "ORDER BY first_name, last_name";

            Connection con = sunrise.dental.config.DBConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                int patientId = rs.getInt("patient_id");

                String patientName = rs.getString("first_name")
                        + " "
                        + rs.getString("last_name");

                jComboBox1.addItem(patientId + " - " + patientName);
            }

            rs.close();
            pst.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadDentists() {
        try {

            jComboBox2.removeAllItems();

            String sql = "SELECT d.dentist_id, "
                    + "u.first_name, u.last_name "
                    + "FROM dentists d "
                    + "INNER JOIN users u ON d.user_id = u.user_id "
                    + "ORDER BY u.first_name, u.last_name";

            Connection con = sunrise.dental.config.DBConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                int dentistId = rs.getInt("dentist_id");

                String dentistName = "Dr. "
                        + rs.getString("first_name")
                        + " "
                        + rs.getString("last_name");

                jComboBox2.addItem(dentistId + " - " + dentistName);
            }

            rs.close();
            pst.close();

        } catch (Exception e) {
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
    
    private void updateTimeButtons() {

        java.time.LocalTime now = java.time.LocalTime.now();

        jButton1.setEnabled(
                now.isBefore(java.time.LocalTime.of(12, 0))
        );

        jButton2.setEnabled(
                now.isBefore(java.time.LocalTime.of(17, 0))
        );

        jButton3.setEnabled(
                now.isBefore(java.time.LocalTime.of(21, 0))
        );
    }
    
    private void loadAppointments() {

        try {

            javax.swing.table.DefaultTableModel model =
                    (javax.swing.table.DefaultTableModel) jTable1.getModel();

            // Clear existing rows
            model.setRowCount(0);

            String sql =
                    "SELECT "
                    + "a.appointment_id, "
                    + "p.first_name AS patient_first_name, "
                    + "p.last_name AS patient_last_name, "
                    + "u.first_name AS dentist_first_name, "
                    + "u.last_name AS dentist_last_name, "
                    + "a.appointment_date, "
                    + "a.appointment_time, "
                    + "a.reason, "
                    + "s.status_name "
                    + "FROM appointments a "
                    + "INNER JOIN patients p "
                    + "ON a.patient_id = p.patient_id "
                    + "INNER JOIN dentists d "
                    + "ON a.dentist_id = d.dentist_id "
                    + "INNER JOIN users u "
                    + "ON d.user_id = u.user_id "
                    + "INNER JOIN status s "
                    + "ON a.status_id = s.status_id "
                    + "ORDER BY a.appointment_date DESC, "
                    + "a.appointment_time ASC";

            java.sql.Connection con =
                    sunrise.dental.config.DBConnection.getConnection();

            java.sql.PreparedStatement pst =
                    con.prepareStatement(sql);

            java.sql.ResultSet rs =
                    pst.executeQuery();

            while (rs.next()) {

                int appointmentId =
                        rs.getInt("appointment_id");

                String appointmentNo =
                        String.format("APT-%05d", appointmentId);

                String patientName =
                        rs.getString("patient_first_name")
                        + " "
                        + rs.getString("patient_last_name");

                String dentistName =
                        "Dr. "
                        + rs.getString("dentist_first_name")
                        + " "
                        + rs.getString("dentist_last_name");

                java.sql.Date appointmentDate =
                        rs.getDate("appointment_date");

                java.sql.Time appointmentTime =
                        rs.getTime("appointment_time");

                String reason =
                        rs.getString("reason");

                String status =
                        rs.getString("status_name");

                model.addRow(new Object[]{
                    appointmentNo,
                    patientName,
                    dentistName,
                    appointmentDate,
                    appointmentTime,
                    reason,
                    status
                });
            }

            rs.close();
            pst.close();

        } catch (Exception e) {

            e.printStackTrace();

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Failed to load appointments:\n"
                    + e.getMessage(),
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void clearAppointmentForm() {

        // Reset Patient
        if (jComboBox1.getItemCount() > 0) {
            jComboBox1.setSelectedIndex(0);
        }

        // Reset Dentist
        if (jComboBox2.getItemCount() > 0) {
            jComboBox2.setSelectedIndex(0);
        }

        // Clear Reason
        jTextField3.setText("");

        // Reset selected IDs
        selectedPatientId = -1;
        selectedDentistId = -1;

        // Reset selected time
        selectedTime = "";

        // Update time buttons
        updateTimeButtons();
    }
    
    private void searchAppointments() {

        String searchText = searchField1.getText().trim();

        DefaultTableModel model =
                (DefaultTableModel) jTable1.getModel();

        model.setRowCount(0);

        String sql =
                "SELECT a.appointment_id, " +
                "p.first_name AS patient_first_name, " +
                "p.last_name AS patient_last_name, " +
                "p.phone, " +
                "d.first_name AS dentist_first_name, " +
                "d.last_name AS dentist_last_name, " +
                "a.appointment_date, " +
                "a.appointment_time, " +
                "a.reason, " +
                "s.status_name " +
                "FROM appointments a " +
                "INNER JOIN patients p ON a.patient_id = p.patient_id " +
                "INNER JOIN dentists d ON a.dentist_id = d.dentist_id " +
                "INNER JOIN status s ON a.status_id = s.status_id " +
                "WHERE p.first_name LIKE ? " +
                "OR p.last_name LIKE ? " +
                "OR p.phone LIKE ? " +
                "OR d.first_name LIKE ? " +
                "OR d.last_name LIKE ? " +
                "ORDER BY a.appointment_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            String search = "%" + searchText + "%";

            pst.setString(1, search);
            pst.setString(2, search);
            pst.setString(3, search);
            pst.setString(4, search);
            pst.setString(5, search);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                String appointmentNo =
                        String.format("APT-%05d",
                                rs.getInt("appointment_id"));

                String patientName =
                        rs.getString("patient_first_name") + " " +
                        rs.getString("patient_last_name");

                String dentistName =
                        "Dr. " +
                        rs.getString("dentist_first_name") + " " +
                        rs.getString("dentist_last_name");

                model.addRow(new Object[]{
                    appointmentNo,
                    patientName,
                    dentistName,
                    rs.getDate("appointment_date"),
                    rs.getTime("appointment_time"),
                    rs.getString("reason"),
                    rs.getString("status_name")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error while searching appointments: "
                            + e.getMessage(),
                    "Search Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        searchField1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();

        jLabel1.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        jLabel1.setText("Appointment Management");

        jLabel2.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel2.setText("Schedule and manage patient Appointment");

        jLabel3.setText("Patient");

        jTextField1.setText("Search by name");

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jTextField2.setText("Search by name");

        jLabel4.setText("Dentist");

        jLabel5.setText("Reason");

        jLabel7.setText("Time");

        jButton1.setText("09.00 - 12.00");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("2.00 - 5.00");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("6.00 - 9.00");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Save Appointment");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Clear");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(234, 234, 234))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextField1)
                                    .addComponent(jTextField3)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                        .addGap(221, 221, 221)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(243, 243, 243))
                            .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(68, 68, 68))
                                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(10, 10, 10))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Appointment No", "Patient", "Dentist", "Date", "Time", "Reason", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        searchField1.setText("Search by name or phone...");
        searchField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchField1KeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jLabel6.setText("Appointment List");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(299, 299, 299)
                        .addComponent(searchField1, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(searchField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
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
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed

        if (jComboBox1.getSelectedItem() != null) {

            String selected = jComboBox1.getSelectedItem().toString();

            if (selected.contains(" - ")) {

                String id = selected.substring(0, selected.indexOf(" - "));

                selectedPatientId = Integer.parseInt(id);
            }
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed

        if (jComboBox2.getSelectedItem() != null) {

            String selected = jComboBox2.getSelectedItem().toString();

            if (selected.contains(" - ")) {

                String id = selected.substring(0, selected.indexOf(" - "));

                selectedDentistId = Integer.parseInt(id);
            }
        }
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        selectedTime = "09:00:00";
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        selectedTime = "14:00:00";
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        selectedTime = "18:00:00";
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        try {

            if (selectedPatientId == -1) {
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Please select a patient."
                );
                return;
            }

            if (selectedDentistId == -1) {
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Please select a dentist."
                );
                return;
            }

            String reason = jTextField3.getText().trim();

            if (reason.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Please enter the reason."
                );
                return;
            }

            if (selectedTime.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Please select an appointment time."
                );
                return;
            }

            java.time.LocalDate appointmentDate =
                    java.time.LocalDate.now();

            String sql = "INSERT INTO appointments "
                    + "(patient_id, dentist_id, appointment_date, "
                    + "appointment_time, reason, status_id) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

            Connection con =
                    sunrise.dental.config.DBConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(sql);

            pst.setInt(1, selectedPatientId);
            pst.setInt(2, selectedDentistId);
            pst.setDate(
                    3,
                    java.sql.Date.valueOf(appointmentDate)
            );
            pst.setTime(
                    4,
                    java.sql.Time.valueOf(selectedTime)
            );
            pst.setString(5, reason);

            // 2 = Confirmed
            pst.setInt(6, 2);

            pst.executeUpdate();

            pst.close();

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Appointment saved successfully!",
                    "Success",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

            loadAppointments();

            clearAppointmentForm();

            updateTimeButtons();

        } catch (Exception e) {

            e.printStackTrace();

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Failed to save appointment:\n" + e.getMessage(),
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        clearAppointmentForm();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void searchField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchField1KeyReleased
        searchAppointments();
    }//GEN-LAST:event_searchField1KeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField searchField1;
    // End of variables declaration//GEN-END:variables
}
