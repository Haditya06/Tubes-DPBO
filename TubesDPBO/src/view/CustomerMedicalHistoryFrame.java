/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;
import database.DatabaseConnection;
import model.Customer;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author syahrilfarhan
 */
public class CustomerMedicalHistoryFrame extends javax.swing.JFrame {
    
    private Customer currentCustomer;
    
    public CustomerMedicalHistoryFrame(Customer customer) {
        this.currentCustomer = customer;
        initComponents();
        setLocationRelativeTo(null);
        loadMedicalHistory();
    }
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CustomerMedicalHistoryFrame.class.getName());

    /**
     * Creates new form CustomerMedicalHistoryFrame
     */
    
    private void loadMedicalHistory() {
        if (currentCustomer == null) {
            JOptionPane.showMessageDialog(this, "Data customer tidak ditemukan!");
            return;
        }

        System.out.println("=== DEBUG MEDICAL HISTORY ===");
        System.out.println("Username: " + currentCustomer.getUsername());

        try {
            Connection conn = DatabaseConnection.getConnection();

            String sqlCheckId = "SELECT c.id_customer FROM customer c "
                    + "JOIN users u ON c.id_user = u.id_user "
                    + "WHERE u.username = ?";
            PreparedStatement psCheck = conn.prepareStatement(sqlCheckId);
            psCheck.setString(1, currentCustomer.getUsername());
            ResultSet rsCheck = psCheck.executeQuery();

            int idCustomerDB = 0;
            if (rsCheck.next()) {
                idCustomerDB = rsCheck.getInt("id_customer");
                System.out.println("✓ Customer ID dari DB: " + idCustomerDB);
            } else {
                System.err.println("✗ Customer tidak ditemukan di tabel customer!");
                return;
            }

            String sqlCheckReservasi = "SELECT COUNT(*) as count FROM reservasi "
                    + "WHERE id_customer = ? AND status_reservasi IN ('SELESAI', 'LUNAS', 'PAID')";
            PreparedStatement psCheckRes = conn.prepareStatement(sqlCheckReservasi);
            psCheckRes.setInt(1, idCustomerDB);
            ResultSet rsCheckRes = psCheckRes.executeQuery();

            int countReservasi = 0;
            if (rsCheckRes.next()) {
                countReservasi = rsCheckRes.getInt("count");
                System.out.println("✓ Jumlah reservasi LUNAS: " + countReservasi);
            }

            if (countReservasi == 0) {
                JOptionPane.showMessageDialog(this,
                        "Belum ada riwayat medis.\n\n"
                        + "Customer ID: " + idCustomerDB + "\n"
                        + "Tidak ada reservasi dengan status LUNAS/SELESAI.",
                        "Informasi",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String sql = "SELECT "
                    + "r.id_reservasi, "
                    + "h.nama_hewan, "
                    + "'General Checkup' as layanan, "
                    + "DATE(r.tanggal) as tanggal, "
                    + "COALESCE(p.diagnosis, 'Belum ada diagnosis') as diagnosis, "
                    + "COALESCE(p.tindakan, '-') as tindakan, "
                    + "COALESCE(p.biaya_layanan, 0) as biaya_layanan, "
                    + "r.status_reservasi "
                    + "FROM reservasi r "
                    + "INNER JOIN hewan h ON r.id_hewan = h.id_hewan "
                    + "LEFT JOIN pemeriksaan p ON r.id_reservasi = p.id_reservasi "
                    + "WHERE r.id_customer = ? "
                    + "AND r.status_reservasi IN ('SELESAI', 'LUNAS', 'PAID') "
                    + "ORDER BY r.tanggal DESC";

            System.out.println("Executing query...");
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idCustomerDB);
            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            int count = 0;
            while (rs.next()) {
                count++;
                model.addRow(new Object[]{
                    "R-" + rs.getInt("id_reservasi"),
                    rs.getString("nama_hewan"),
                    rs.getString("layanan"),
                    rs.getString("tanggal"),
                    rs.getString("diagnosis"),
                    "Rp " + String.format("%,.0f", rs.getDouble("biaya_layanan")).replace(",", ".")
                });
            }

            System.out.println("✓ Total records loaded: " + count);
            System.out.println("=== END DEBUG ===");

            if (count == 0) {
                JOptionPane.showMessageDialog(this,
                        "Belum ada riwayat medis.\n\n"
                        + "Data reservasi ada (" + countReservasi + "), tapi:\n"
                        + "1. Tidak ada data di tabel pemeriksaan, atau\n"
                        + "2. Ada masalah di JOIN tabel",
                        "Informasi",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Res. ID", "Pet", "Service", "Date", "Diagnosa", "Total Cost"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Medical History");

        btnClose.setText("Close");
        btnClose.addActionListener(this::btnCloseActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnClose)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 690, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(263, 263, 263))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClose)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new CustomerMedicalHistoryFrame(null).setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
