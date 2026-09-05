
package sunrise.dental.ui.admin;

import javax.swing.plaf.basic.BasicInternalFrameUI;
import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import sunrise.dental.config.DBConnection;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class AdminHome extends javax.swing.JInternalFrame {
    
    public AdminHome() {
        initComponents();

        removeInternalFrameTitleBar();

        setBorder(null);

        setClosable(false);
        setIconifiable(false);
        setMaximizable(false);
        setResizable(false);

        try {
            setMaximum(true);
        } catch (java.beans.PropertyVetoException e) {
            e.printStackTrace();
        }

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                jPanel6.setSize(getContentPane().getSize());
            }
        });
        loadRevenueChart();
        loadTotalPatients();
        loadTodayRevenue();
        jButton4.setEnabled(false);
    }

    private void removeInternalFrameTitleBar() {

        BasicInternalFrameUI ui
                = (BasicInternalFrameUI) getUI();

        ui.setNorthPane(null);

        setBorder(null);

        revalidate();
        repaint();
    }
    
    private void loadRevenueChart() {

        try {

            String sql =
                    "SELECT DATE(payment_date) AS payment_day, "
                    + "SUM(amount) AS revenue "
                    + "FROM payments "
                    + "WHERE payment_status = 'Paid' "
                    + "AND payment_date >= DATE_FORMAT(CURDATE(), '%Y-%m-01') "
                    + "AND payment_date < DATE_ADD(DATE_FORMAT(CURDATE(), '%Y-%m-01'), INTERVAL 1 MONTH) "
                    + "GROUP BY DATE(payment_date) "
                    + "ORDER BY DATE(payment_date)";

            PreparedStatement pst = DBConnection.getConnection()
                    .prepareStatement(sql);

            ResultSet rs = pst.executeQuery();

            org.jfree.data.category.DefaultCategoryDataset dataset =
                    new org.jfree.data.category.DefaultCategoryDataset();

            while (rs.next()) {

                String date = rs.getString("payment_day");
                double revenue = rs.getDouble("revenue");

                dataset.addValue(
                        revenue,
                        "Revenue",
                        date
                );
            }

            rs.close();
            pst.close();

            org.jfree.chart.JFreeChart chart =
                    org.jfree.chart.ChartFactory.createLineChart(
                            "Monthly Revenue",
                            "Date",
                            "Revenue (LKR)",
                            dataset,
                            org.jfree.chart.plot.PlotOrientation.VERTICAL,
                            true,
                            true,
                            false
                    );

            org.jfree.chart.plot.CategoryPlot plot =
                    chart.getCategoryPlot();

            org.jfree.chart.renderer.category.LineAndShapeRenderer renderer =
                    new org.jfree.chart.renderer.category.LineAndShapeRenderer();

            renderer.setDefaultShapesVisible(true);
            renderer.setDefaultLinesVisible(true);

            plot.setRenderer(renderer);

            org.jfree.chart.ChartPanel chartPanel =
                    new org.jfree.chart.ChartPanel(chart);

            chartPanel.setMouseWheelEnabled(true);

            jPanel5.removeAll();

            jPanel5.setLayout(new java.awt.BorderLayout());

            jPanel5.add(
                    chartPanel,
                    java.awt.BorderLayout.CENTER
            );

            jPanel5.revalidate();
            jPanel5.repaint();

        } catch (Exception e) {

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Error loading monthly revenue chart:\n"
                    + e.getMessage(),
                    "Database Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }
    
    private void loadTotalPatients() {

        try {

            String sql = "SELECT COUNT(*) AS total FROM patients";

            PreparedStatement pst = DBConnection.getConnection()
                    .prepareStatement(sql);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int totalPatients = rs.getInt("total");

                jLabel3.setText(String.valueOf(totalPatients));
            }

            rs.close();
            pst.close();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading total patients:\n" + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }
    
    private void loadTodayRevenue() {

        try {

            String sql =
                    "SELECT COALESCE(SUM(amount), 0) AS today_revenue "
                    + "FROM payments "
                    + "WHERE payment_status = 'Paid' "
                    + "AND DATE(payment_date) = CURDATE()";

            PreparedStatement pst = DBConnection.getConnection()
                    .prepareStatement(sql);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                double todayRevenue = rs.getDouble("today_revenue");

                // Display today's revenue
                jLabel7.setText(
                        String.format("LKR %.2f", todayRevenue)
                );

                // Enable / Disable Report button
                if (todayRevenue > 0) {
                    jButton4.setEnabled(true);
                } else {
                    jButton4.setEnabled(false);
                }
            }

            rs.close();
            pst.close();

        } catch (Exception e) {

            jLabel7.setText("LKR 0.00");
            jButton4.setEnabled(false);

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading today's revenue:\n"
                    + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }
    
    private void generateTodayRevenueReport() {

        Document document = new Document();

        try {

            // Create reports folder
            File reportsFolder = new File("reports");

            if (!reportsFolder.exists()) {
                reportsFolder.mkdirs();
            }

            // File name
            String fileName =
                    "Today_Revenue_Report_"
                    + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")
                            .format(new Date())
                    + ".pdf";

            File pdfFile = new File(
                    reportsFolder,
                    fileName
            );

            // Create PDF
            PdfWriter.getInstance(
                    document,
                    new java.io.FileOutputStream(pdfFile)
            );

            document.open();

            // =========================
            // TITLE
            // =========================

            Paragraph title = new Paragraph(
                    "SUNRISE DENTAL CLINIC"
            );

            title.setAlignment(
                    com.itextpdf.text.Element.ALIGN_CENTER
            );

            document.add(title);

            Paragraph reportTitle = new Paragraph(
                    "Today's Revenue Report"
            );

            reportTitle.setAlignment(
                    com.itextpdf.text.Element.ALIGN_CENTER
            );

            document.add(reportTitle);

            document.add(
                    new Paragraph(
                            "Date: "
                            + new SimpleDateFormat("yyyy-MM-dd")
                                    .format(new Date())
                    )
            );

            document.add(
                    new Paragraph(" ")
            );

            // =========================
            // GET PAYMENTS
            // =========================

            String sql =
                    "SELECT payment_id, bill_id, amount, "
                    + "payment_method, payment_date "
                    + "FROM payments "
                    + "WHERE payment_status = 'Paid' "
                    + "AND DATE(payment_date) = CURDATE() "
                    + "ORDER BY payment_date";

            PreparedStatement pst =
                    DBConnection.getConnection()
                            .prepareStatement(sql);

            ResultSet rs = pst.executeQuery();

            // =========================
            // TABLE
            // =========================

            PdfPTable table =
                    new PdfPTable(5);

            table.setWidthPercentage(100);

            table.addCell(
                    new PdfPCell(
                            new Phrase("Payment ID")
                    )
            );

            table.addCell(
                    new PdfPCell(
                            new Phrase("Bill ID")
                    )
            );

            table.addCell(
                    new PdfPCell(
                            new Phrase("Amount (LKR)")
                    )
            );

            table.addCell(
                    new PdfPCell(
                            new Phrase("Payment Method")
                    )
            );

            table.addCell(
                    new PdfPCell(
                            new Phrase("Payment Date")
                    )
            );

            double totalRevenue = 0;

            while (rs.next()) {

                int paymentId =
                        rs.getInt("payment_id");

                int billId =
                        rs.getInt("bill_id");

                double amount =
                        rs.getDouble("amount");

                String paymentMethod =
                        rs.getString("payment_method");

                String paymentDate =
                        rs.getString("payment_date");

                totalRevenue += amount;

                table.addCell(
                        String.valueOf(paymentId)
                );

                table.addCell(
                        String.valueOf(billId)
                );

                table.addCell(
                        String.format("%.2f", amount)
                );

                table.addCell(
                        paymentMethod
                );

                table.addCell(
                        paymentDate
                );
            }

            document.add(table);

            document.add(
                    new Paragraph(" ")
            );

            // =========================
            // TOTAL REVENUE
            // =========================

            Paragraph total = new Paragraph(
                    String.format(
                            "TOTAL TODAY REVENUE: LKR %.2f",
                            totalRevenue
                    )
            );

            total.setAlignment(
                    com.itextpdf.text.Element.ALIGN_RIGHT
            );

            document.add(total);

            document.add(
                    new Paragraph(" ")
            );

            Paragraph footer = new Paragraph(
                    "Generated by Sunrise Dental Clinic Management System"
            );

            footer.setAlignment(
                    com.itextpdf.text.Element.ALIGN_CENTER
            );

            document.add(footer);

            rs.close();
            pst.close();

            document.close();

            // =========================
            // OPEN PDF IN CHROME
            // =========================

            openPDFInChrome(pdfFile);

        } catch (Exception e) {

            if (document.isOpen()) {
                document.close();
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Error generating revenue report:\n"
                    + e.getMessage(),
                    "PDF Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }
    
    private void openPDFInChrome(File pdfFile) {

        try {

            String[] chromePaths = {

                "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe",

                "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe",

                System.getProperty("user.home")
                        + "\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe"
            };

            String chromePath = null;

            for (String path : chromePaths) {

                File chromeFile = new File(path);

                if (chromeFile.exists()) {

                    chromePath = path;
                    break;
                }
            }

            // =========================
            // OPEN WITH CHROME
            // =========================

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

                // Fallback
                if (java.awt.Desktop.isDesktopSupported()) {

                    java.awt.Desktop.getDesktop()
                            .open(pdfFile);
                }
            }

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to open PDF:\n"
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

        jPanel6 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/patientbed.png"))); // NOI18N
        jButton1.setFocusable(false);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("Total Patient");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/revenue.png"))); // NOI18N
        jButton3.setFocusable(false);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setText("Today Revenue");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N

        jButton4.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jButton4.setText("Report");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(14, 14, 14))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jButton4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12))
        );

        jPanel5.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(307, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 171, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        generateTodayRevenueReport();
    }//GEN-LAST:event_jButton4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    // End of variables declaration//GEN-END:variables
}
