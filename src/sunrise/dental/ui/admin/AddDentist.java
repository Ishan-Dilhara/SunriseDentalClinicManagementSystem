/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package sunrise.dental.ui.admin;

import sunrise.dental.config.DBConnection;


public class AddDentist extends javax.swing.JDialog {

    private Users usersForm;
    
    public AddDentist(java.awt.Frame parent, boolean modal, Users usersForm) {
        super(parent, modal);
        initComponents();
        this.usersForm = usersForm;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel7 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTextField17 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel7.setLayout(new java.awt.GridBagLayout());

        jLabel13.setFont(new java.awt.Font("Tw Cen MT", 0, 24)); // NOI18N
        jLabel13.setText("First Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 65, 0, 0);
        jPanel7.add(jLabel13, gridBagConstraints);

        jTextField12.setFont(new java.awt.Font("Tw Cen MT", 0, 24)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 14;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 65, 0, 77);
        jPanel7.add(jTextField12, gridBagConstraints);

        jLabel14.setFont(new java.awt.Font("Tw Cen MT", 0, 24)); // NOI18N
        jLabel14.setText("Last Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 65, 0, 0);
        jPanel7.add(jLabel14, gridBagConstraints);

        jTextField13.setFont(new java.awt.Font("Tw Cen MT", 0, 24)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 14;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 65, 0, 77);
        jPanel7.add(jTextField13, gridBagConstraints);

        jLabel15.setFont(new java.awt.Font("Tw Cen MT", 0, 24)); // NOI18N
        jLabel15.setText("Specialization");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 65, 0, 0);
        jPanel7.add(jLabel15, gridBagConstraints);

        jTextField15.setFont(new java.awt.Font("Tw Cen MT", 0, 24)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 14;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 65, 0, 77);
        jPanel7.add(jTextField15, gridBagConstraints);

        jLabel16.setFont(new java.awt.Font("Tw Cen MT", 0, 24)); // NOI18N
        jLabel16.setText("Email");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 40;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 65, 0, 0);
        jPanel7.add(jLabel16, gridBagConstraints);

        jTextField16.setFont(new java.awt.Font("Tw Cen MT", 0, 24)); // NOI18N
        jTextField16.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField16KeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 14;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 65, 0, 77);
        jPanel7.add(jTextField16, gridBagConstraints);

        jLabel17.setFont(new java.awt.Font("Tw Cen MT", 0, 24)); // NOI18N
        jLabel17.setText("Phone");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 37;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 65, 0, 0);
        jPanel7.add(jLabel17, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 51, 153));
        jLabel2.setText("Add Dentists");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 134, 0, 0);
        jPanel7.add(jLabel2, gridBagConstraints);

        jButton1.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/save.png"))); // NOI18N
        jButton1.setText("Save Dentist");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 65, 29, 0);
        jPanel7.add(jButton1, gridBagConstraints);

        jButton2.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/cancel.png"))); // NOI18N
        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 6, 29, 77);
        jPanel7.add(jButton2, gridBagConstraints);

        jTextField17.setFont(new java.awt.Font("Tw Cen MT", 0, 24)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 14;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 65, 0, 77);
        jPanel7.add(jTextField17, gridBagConstraints);

        jLabel18.setFont(new java.awt.Font("Tw Cen MT", 0, 24)); // NOI18N
        jLabel18.setText("Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipadx = 43;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 65, 0, 0);
        jPanel7.add(jLabel18, gridBagConstraints);

        jPasswordField1.setFont(new java.awt.Font("Tw Cen MT", 0, 24)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 14;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 65, 0, 77);
        jPanel7.add(jPasswordField1, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String firstName = jTextField12.getText().trim();
        String lastName = jTextField13.getText().trim();
        String email = jTextField15.getText().trim();
        String phone = jTextField16.getText().trim();
        String specialization = jTextField17.getText().trim();
        String password = new String(jPasswordField1.getPassword());

        // Validation
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()
                || phone.isEmpty() || specialization.isEmpty() || password.isEmpty()) {

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Please fill all fields.",
                    "Validation Error",
                    javax.swing.JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        java.sql.Connection con = null;
        java.sql.PreparedStatement userPst = null;
        java.sql.PreparedStatement dentistPst = null;
        java.sql.ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            String userSql = "INSERT INTO users "
                    + "(first_name, last_name, email, password, phone, role, status) "
                    + "VALUES (?, ?, ?, ?, ?, 'Dentist', 'Active')";

            userPst = con.prepareStatement(
                    userSql,
                    java.sql.Statement.RETURN_GENERATED_KEYS
            );

            userPst.setString(1, firstName);
            userPst.setString(2, lastName);
            userPst.setString(3, email);
            userPst.setString(4, password);
            userPst.setString(5, phone);

            int userRows = userPst.executeUpdate();

            if (userRows == 0) {
                throw new Exception("Failed to add dentist user.");
            }

            int userId = 0;

            rs = userPst.getGeneratedKeys();

            if (rs.next()) {
                userId = rs.getInt(1);
            } else {
                throw new Exception("Failed to get generated User ID.");
            }

            String dentistSql = "INSERT INTO dentists "
                    + "(user_id, specialization) "
                    + "VALUES (?, ?)";

            dentistPst = con.prepareStatement(dentistSql);

            dentistPst.setInt(1, userId);
            dentistPst.setString(2, specialization);

            int dentistRows = dentistPst.executeUpdate();

            if (dentistRows == 0) {
                throw new Exception("Failed to add dentist details.");
            }
            
            con.commit();

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Dentist added successfully!",
                    "Success",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

            if (usersForm != null) {
                usersForm.loadDentists();
            }
            
            this.dispose();

        } catch (Exception e) {

            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (java.sql.SQLException rollbackException) {
                rollbackException.printStackTrace();
            }

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Error: " + e.getMessage(),
                    "Database Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();

        } finally {

            try {

                if (rs != null) {
                    rs.close();
                }

                if (userPst != null) {
                    userPst.close();
                }

                if (dentistPst != null) {
                    dentistPst.close();
                }

                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }

            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField16KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField16KeyTyped
        char c = evt.getKeyChar();

        if (!Character.isDigit(c)) {
            evt.consume();
            return;
        }

        if (jTextField16.getText().length() >= 10) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField16KeyTyped

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AddDentist.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddDentist.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddDentist.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddDentist.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                AddDentist dialog = new AddDentist(
                        new javax.swing.JFrame(),
                        true,
                        null
                );

                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });

                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    // End of variables declaration//GEN-END:variables
}
