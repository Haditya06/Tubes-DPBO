/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import database.DatabaseConnection;
import model.Customer;
import util.SessionManager;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Dit
 */
public class CustomerDashboardFrame extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CustomerDashboardFrame.class.getName());
    private Customer currentCustomer;

    public CustomerDashboardFrame() {
        initComponents();
        loadCustomerData();
        setLocationRelativeTo(null);
    }

    public void refreshData() {
        loadCustomerData();
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            loadCustomerData();
        }
    }

    private void loadCustomerData() {
        Customer customer = (Customer) SessionManager.getInstance().getCurrentUser();

        if (customer == null) {
            System.out.println("[CustomerDashboard] Tidak ada user yang login!");
            lblNama2.setText("Nama : Tidak ada user login");
            return;
        }

        this.currentCustomer = customer;

        try {
            Connection conn = DatabaseConnection.getConnection();

            int idCustomerDB = 0;
            String sqlCust = "SELECT c.id_customer, u.nama_lengkap, c.no_hp "
                    + "FROM customer c "
                    + "JOIN users u ON c.id_user = u.id_user "
                    + "WHERE u.username = ?";
            PreparedStatement psCust = conn.prepareStatement(sqlCust);
            psCust.setString(1, customer.getUsername());
            ResultSet rsCust = psCust.executeQuery();

            if (rsCust.next()) {
                idCustomerDB = rsCust.getInt("id_customer");  // ✅ Ambil ID
                lblNama2.setText("Nama : " + rsCust.getString("nama_lengkap"));
                jLabel5.setText("Phone Number : " + rsCust.getString("no_hp"));
            }

            System.out.println("[DEBUG] ID Customer: " + idCustomerDB);

            // 2. Ambil data hewan
            String sqlHewan = "SELECT h.nama_hewan, h.jenis, h.umur, "
                    + "(SELECT COUNT(*) FROM reservasi r2 WHERE r2.id_hewan = h.id_hewan) as jumlah_reservasi, "
                    + "(SELECT MAX(r2.tanggal) FROM reservasi r2 WHERE r2.id_hewan = h.id_hewan) as latest_date, "
                    + "(SELECT r2.status_reservasi FROM reservasi r2 WHERE r2.id_hewan = h.id_hewan ORDER BY r2.tanggal DESC LIMIT 1) as status_reservasi "
                    + "FROM hewan h "
                    + "WHERE h.id_customer = ?";

            PreparedStatement psHewan = conn.prepareStatement(sqlHewan);
            psHewan.setInt(1, idCustomerDB);
            ResultSet rsHewan = psHewan.executeQuery();

            DefaultTableModel modelPets = new DefaultTableModel(
                    new String[]{"Pet Name", "Type", "Age", "Reservations", "Latest Date", "Reservation Status"}, 0
            );

            int totalPets = 0;
            while (rsHewan.next()) {
                totalPets++;

                String latestDate = rsHewan.getTimestamp("latest_date") != null
                        ? rsHewan.getTimestamp("latest_date").toString()
                        : "-";
                String status = rsHewan.getString("status_reservasi") != null
                        ? rsHewan.getString("status_reservasi")
                        : "-";

                modelPets.addRow(new Object[]{
                    rsHewan.getString("nama_hewan"),
                    rsHewan.getString("jenis"),
                    rsHewan.getInt("umur"),
                    rsHewan.getInt("jumlah_reservasi"), // ✅ Ganti dari jumlah_layanan
                    latestDate,
                    status
                });
            }
            tblPets.setModel(modelPets);
            lblPets2.setText("Pets : " + totalPets);

            String sqlReservasi = "SELECT "
                    + "r.id_reservasi, "
                    + "h.nama_hewan, "
                    + "GROUP_CONCAT(l.nama_layanan SEPARATOR ', ') as layanan, "
                    + "DATE(r.tanggal) as tanggal, "
                    + "TIME(r.tanggal) as waktu, "
                    + "r.status_reservasi "
                    + "FROM reservasi r "
                    + "JOIN hewan h ON r.id_hewan = h.id_hewan "
                    + "LEFT JOIN reservasi_layanan rl ON r.id_reservasi = rl.id_reservasi "
                    + "LEFT JOIN layanan l ON rl.id_layanan = l.id_layanan "
                    + "WHERE r.id_customer = ? "
                    + "GROUP BY r.id_reservasi, h.nama_hewan, DATE(r.tanggal), TIME(r.tanggal), r.status_reservasi "
                    + "ORDER BY r.tanggal DESC";

            PreparedStatement psReservasi = conn.prepareStatement(sqlReservasi);
            psReservasi.setInt(1, idCustomerDB);  
            ResultSet rsReservasi = psReservasi.executeQuery();

            DefaultTableModel modelAll = new DefaultTableModel(
                    new String[]{"ID", "Pet", "Service", "Date", "Time", "Status"}, 0
            );

            int totalReservasi = 0;
            while (rsReservasi.next()) {
                String tanggal = rsReservasi.getString("tanggal");
                String waktu = rsReservasi.getString("waktu");
                String layanan = rsReservasi.getString("layanan");

                if (layanan == null || layanan.isEmpty()) {
                    layanan = "General Checkup";
                }

                modelAll.addRow(new Object[]{
                    "R-" + rsReservasi.getInt("id_reservasi"),
                    rsReservasi.getString("nama_hewan"),
                    layanan,
                    tanggal != null ? tanggal : "-",
                    waktu != null ? waktu : "-",
                    rsReservasi.getString("status_reservasi")
                });
                totalReservasi++;
            }
            tblAll.setModel(modelAll);
            jLabel6.setText("Total Reservations : " + totalReservasi);

        } catch (Exception e) {
            System.err.println("[CustomerDashboard] Error: " + e.getMessage());
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

        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        lblNama2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblPets2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        BtnLogout = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPets = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblAll = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        btnCreate = new javax.swing.JButton();
        btnMedicalHistory = new javax.swing.JButton();
        btnAddPet = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1024, 600));

        jPanel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblNama2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblNama2.setText("Nama :");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Phone Number :");

        lblPets2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblPets2.setText("Pets :");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Total Reservations :");

        BtnLogout.setText("Logout");
        BtnLogout.addActionListener(this::BtnLogoutActionPerformed);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(lblNama2)
                .addGap(50, 50, 50)
                .addComponent(jLabel5)
                .addGap(50, 50, 50)
                .addComponent(lblPets2)
                .addGap(50, 50, 50)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 417, Short.MAX_VALUE)
                .addComponent(BtnLogout)
                .addGap(17, 17, 17))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNama2)
                    .addComponent(jLabel5)
                    .addComponent(lblPets2)
                    .addComponent(jLabel6))
                .addGap(7, 7, 7))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BtnLogout)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("My Pets & Reservartion"));

        tblPets.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tblPets.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Pet Name", "Type", "Age", "Reservations", "Latest Date", "Reservation Status"
            }
        ));
        jScrollPane1.setViewportView(tblPets);

        tblAll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tblAll.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Pet", "Service", "Date", "Time", "Status"
            }
        ));
        jScrollPane3.setViewportView(tblAll);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("All Reservations :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        btnCreate.setText("Create New Reservation");
        btnCreate.addActionListener(this::btnCreateActionPerformed);

        btnMedicalHistory.setText("View Medical History");
        btnMedicalHistory.addActionListener(this::btnMedicalHistoryActionPerformed);

        btnAddPet.setText("Add Pet");
        btnAddPet.addActionListener(this::btnAddPetActionPerformed);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(btnCreate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnMedicalHistory)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddPet)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCreate)
                    .addComponent(btnMedicalHistory)
                    .addComponent(btnAddPet))
                .addContainerGap(109, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
        // TODO add your handling code here:
        FormReservasiFrame form = new FormReservasiFrame();
        form.setLocationRelativeTo(this);
        form.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                loadCustomerData(); 
            }
        });

        form.setVisible(true);

    }//GEN-LAST:event_btnCreateActionPerformed

    private void btnMedicalHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMedicalHistoryActionPerformed
        // TODO add your handling code here:
        Customer customer = (Customer) SessionManager.getInstance().getCurrentUser();

        if (customer == null) {
            JOptionPane.showMessageDialog(this, "Silakan login terlebih dahulu!");
            return;
        }

        CustomerMedicalHistoryFrame form = new CustomerMedicalHistoryFrame(customer);
        form.setLocationRelativeTo(this);
        form.setVisible(true);
    }//GEN-LAST:event_btnMedicalHistoryActionPerformed

    private void BtnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnLogoutActionPerformed
        // TODO add your handling code here:
        SessionManager.getInstance().logout();

        javax.swing.JOptionPane.showMessageDialog(this, "Anda telah logout!");

        this.dispose();

        new LoginFrame().setVisible(true);
    }//GEN-LAST:event_BtnLogoutActionPerformed

    private void btnAddPetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPetActionPerformed
        // TODO add your handling code here:
        FormTambahHewan form = new FormTambahHewan();

        form.setLocationRelativeTo(this);

        form.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                loadCustomerData(); 
            }
        });
        form.setVisible(true);
    }//GEN-LAST:event_btnAddPetActionPerformed

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
            } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
                logger.log(java.util.logging.Level.SEVERE, null, ex);
            }
            //</editor-fold>

            /* Create and display the form */
            java.awt.EventQueue.invokeLater(() -> new CustomerDashboardFrame().setVisible(true));
        }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnLogout;
    private javax.swing.JButton btnAddPet;
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnMedicalHistory;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblNama2;
    private javax.swing.JLabel lblPets2;
    private javax.swing.JTable tblAll;
    private javax.swing.JTable tblPets;
    // End of variables declaration//GEN-END:variables
}
