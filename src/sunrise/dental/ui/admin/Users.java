/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package sunrise.dental.ui.admin;

import javax.swing.plaf.basic.BasicInternalFrameUI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;
import sunrise.dental.config.DBConnection;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Users extends javax.swing.JInternalFrame {

    
    public Users() {
        initComponents();

        removeInternalFrameTitleBar();

        setBorder(null);

        setClosable(false);
        setIconifiable(false);
        setMaximizable(false);
        setResizable(false);
        loadDentists();
        loadReceptionists();
        
        setupSearch();
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
    
    public void loadDentists() {

        DefaultTableModel model =
                (DefaultTableModel) dentistTable.getModel();

        model.setRowCount(0);

        String sql = """
                     SELECT u.user_id,
                            u.first_name,
                            u.last_name,
                            u.email,
                            u.phone,
                            u.status
                     FROM users u
                     INNER JOIN dentists d
                     ON u.user_id = d.user_id
                     WHERE u.role = 'Dentist'
                     """;

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(sql);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                String name =
                        rs.getString("first_name") + " "
                        + rs.getString("last_name");

                model.addRow(new Object[]{
                    rs.getString("user_id"),
                    name,
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("status")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadReceptionists() {

        DefaultTableModel model =
                (DefaultTableModel) receptionistTable.getModel();

        model.setRowCount(0);

        String sql = """
                     SELECT user_id,
                            first_name,
                            last_name,
                            email,
                            phone,
                            status
                     FROM users
                     WHERE role = 'Receptionist'
                     """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()
        ) {

            while (rs.next()) {

                String name =
                        rs.getString("first_name") + " "
                        + rs.getString("last_name");

                model.addRow(new Object[]{
                    rs.getInt("user_id"),
                    name,
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("status")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setupSearch() {

        searchField.getDocument().addDocumentListener(
            new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    searchUsers();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    searchUsers();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    searchUsers();
                }
            }
        );
    }
    
    private void searchUsers() {

        String searchText = searchField.getText().trim();

        if (searchText.isEmpty()) {

            loadDentists();
            loadReceptionists();

            return;
        }

        searchDentists(searchText);
        searchReceptionists(searchText);
    }
    
    private void searchDentists(String searchText) {

        DefaultTableModel model =
                (DefaultTableModel) dentistTable.getModel();

        model.setRowCount(0);

        String sql = """
            SELECT user_id,
                   first_name,
                   last_name,
                   email,
                   phone,
                   status
            FROM users
            WHERE role = 'Dentist'
            AND (
                first_name LIKE ?
                OR last_name LIKE ?
                OR email LIKE ?
                OR phone LIKE ?
            )
            """;

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(sql)
        ) {

            String search = "%" + searchText + "%";

            pst.setString(1, search);
            pst.setString(2, search);
            pst.setString(3, search);
            pst.setString(4, search);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                model.addRow(new Object[]{
                    rs.getInt("user_id"),
                    rs.getString("first_name") + " "
                            + rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("status")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void searchReceptionists(String searchText) {

        DefaultTableModel model =
                (DefaultTableModel) receptionistTable.getModel();

        model.setRowCount(0);

        String sql = "SELECT user_id, first_name, last_name, email, phone, status "
                + "FROM users "
                + "WHERE role = 'Receptionist' "
                + "AND (first_name LIKE ? "
                + "OR last_name LIKE ? "
                + "OR phone LIKE ?)";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(sql);

            String search = "%" + searchText + "%";

            pst.setString(1, search);
            pst.setString(2, search);
            pst.setString(3, search);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                String name =
                        rs.getString("first_name") + " "
                        + rs.getString("last_name");

                model.addRow(new Object[]{
                    rs.getString("user_id"),
                    name,
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("status")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
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
        searchField = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        addReceptionistButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        receptionistTable = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        addDentistButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        dentistTable = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        jLabel1.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        jLabel1.setText("User Management");

        searchField.setText("Search by name or phone...");

        addReceptionistButton.setText("Add Receptionist");
        addReceptionistButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addReceptionistButtonActionPerformed(evt);
            }
        });

        receptionistTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Email", "Phone", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(receptionistTable);

        jButton6.setText("jButton6");
        jButton6.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addReceptionistButton)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addReceptionistButton)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        addDentistButton.setText("Add Dentist");
        addDentistButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDentistButtonActionPerformed(evt);
            }
        });

        dentistTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Email", "Phone", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(dentistTable);

        jButton5.setText("jButton5");
        jButton5.setEnabled(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addDentistButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addDentistButton)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel2.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel2.setText("Add, view and manage Dentists and receptionists");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void addDentistButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDentistButtonActionPerformed
        AddDentist dialog = new AddDentist(null, true, this);
        dialog.setVisible(true);
    }//GEN-LAST:event_addDentistButtonActionPerformed

    private void addReceptionistButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addReceptionistButtonActionPerformed
        AddReceptionist dialog = new AddReceptionist(null,true,this);
        dialog.setVisible(true);
    }//GEN-LAST:event_addReceptionistButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addDentistButton;
    private javax.swing.JButton addReceptionistButton;
    private javax.swing.JTable dentistTable;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable receptionistTable;
    private javax.swing.JTextField searchField;
    // End of variables declaration//GEN-END:variables
}
