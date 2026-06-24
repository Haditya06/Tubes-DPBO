/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import database.DatabaseConnection;
import database.ReservasiDAO;
import model.Customer;
import model.Reservasi;
import util.SessionManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FormReservasiFrame extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FormReservasiFrame.class.getName());
    private List<ServiceItem> selectedServices = new ArrayList<>();
    
    private class ServiceItem {

        int idLayanan;
        String namaLayanan;
        double harga;

        ServiceItem(int idLayanan, String namaLayanan, double harga) {
            this.idLayanan = idLayanan;
            this.namaLayanan = namaLayanan;
            this.harga = harga;
        }
    }
    
    public FormReservasiFrame() {
        initComponents();
        loadPetsToDropdown();
        loadServicesToDropdown();
        setLocationRelativeTo(null);
    }

    private void loadPetsToDropdown() {
        dropPets.removeAllItems();  // ✅ Hapus item default
        dropPets.addItem("-- Pilih Hewan --");

        try {
            Customer customer = (Customer) SessionManager.getInstance().getCurrentUser();

            if (customer == null) {
                System.out.println("[ERROR] Tidak ada user login!");
                JOptionPane.showMessageDialog(this, "Silakan login terlebih dahulu!");
                return;
            }

            System.out.println("[DEBUG] Username: " + customer.getUsername());

            Connection conn = DatabaseConnection.getConnection();

            // Query yang lebih sederhana
            String sql = "SELECT h.id_hewan, h.nama_hewan, h.jenis "
                    + "FROM hewan h "
                    + "JOIN customer c ON h.id_customer = c.id_customer "
                    + "WHERE c.id_user = (SELECT id_user FROM users WHERE username = ?)";

            System.out.println("[DEBUG] Query: " + sql);

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, customer.getUsername());
            ResultSet rs = ps.executeQuery();

            int count = 0;
            while (rs.next()) {
                count++;
                int idHewan = rs.getInt("id_hewan");
                String namaHewan = rs.getString("nama_hewan");
                String jenis = rs.getString("jenis");

                String itemText = idHewan + "|" + namaHewan + " - " + jenis;
                dropPets.addItem(itemText);

                System.out.println("[DEBUG] Pet loaded: " + itemText);
            }

            System.out.println("[DEBUG] Total pets loaded: " + count);

            if (count == 0) {
                System.out.println("[WARN] Tidak ada hewan untuk customer ini!");
                JOptionPane.showMessageDialog(this,
                        "Anda belum memiliki hewan.\nSilakan add pet terlebih dahulu!",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            System.err.println("[ERROR] loadPetsToDropdown: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    
    private void loadServicesToDropdown() {
        dropService.removeAllItems();
        dropService.addItem("-- Pilih Layanan --");

        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT id_layanan, nama_layanan, harga FROM layanan ORDER BY id_layanan";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_layanan");
                String nama = rs.getString("nama_layanan");
                double harga = rs.getDouble("harga");

                // Format: "ID|Nama - Rp Harga"
                dropService.addItem(id + "|" + nama + " - Rp " + String.format("%,.0f", harga).replace(",", "."));
            }

            System.out.println("[DEBUG] Layanan loaded from database");
        } catch (SQLException e) {
            e.printStackTrace();
            // Fallback kalau error atau tabel belum ada
            dropService.addItem("1|Pemeriksaan Umum - Rp 100000");
            dropService.addItem("2|Vaksinasi - Rp 150000");
            dropService.addItem("3|Sterilisasi - Rp 500000");
            dropService.addItem("4|Perawatan Gigi - Rp 200000");
            dropService.addItem("5|Suntik Antibiotik - Rp 75000");
            dropService.addItem("6|Obat Cacing - Rp 50000");
            System.out.println("[WARN] Using fallback service data");
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
        jPanel1 = new javax.swing.JPanel();
        dropPets = new javax.swing.JComboBox<>();
        dropService = new javax.swing.JComboBox<>();
        tfDate = new javax.swing.JTextField();
        btnSubmit = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnAddService = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTabeService = new javax.swing.JTable();
        lblTotal = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Create Reservation"));

        dropPets.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        dropPets.setBorder(javax.swing.BorderFactory.createTitledBorder("Select Pet:"));
        dropPets.addActionListener(this::dropPetsActionPerformed);

        dropService.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        dropService.setBorder(javax.swing.BorderFactory.createTitledBorder("Select Service:"));
        dropService.addActionListener(this::dropServiceActionPerformed);

        tfDate.setText("YYYY-MM-DD HH:MM:SS");
        tfDate.setBorder(javax.swing.BorderFactory.createTitledBorder("Date:"));
        tfDate.addActionListener(this::tfDateActionPerformed);

        btnSubmit.setText("Submit");
        btnSubmit.addActionListener(this::btnSubmitActionPerformed);

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(this::btnCancelActionPerformed);

        btnAddService.setText("Add Service");
        btnAddService.addActionListener(this::btnAddServiceActionPerformed);

        jTabeService.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Service", "Price"
            }
        ));
        jScrollPane2.setViewportView(jTabeService);

        lblTotal.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTotal.setText("Total: Rp.0");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dropService, javax.swing.GroupLayout.Alignment.TRAILING, 0, 487, Short.MAX_VALUE)
                    .addComponent(dropPets, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tfDate)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnAddService)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSubmit)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancel)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dropPets, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(dropService, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAddService)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(tfDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTotal)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnSubmit)
                        .addComponent(btnCancel)))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dropPetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dropPetsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dropPetsActionPerformed

    private void dropServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dropServiceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dropServiceActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        // TODO add your handling code here:
        if (dropPets.getSelectedItem() == null || dropPets.getSelectedItem().toString().equals("-- Pilih Hewan --")) {
            JOptionPane.showMessageDialog(this, "Pilih hewan terlebih dahulu!");
            return;
        }

        if (selectedServices.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tambahkan minimal 1 layanan!");
            return;
        }

        String dateText = tfDate.getText();
        if (dateText.isEmpty() || dateText.equals("YYYY-MM-DD HH:MM:SS")) {
            JOptionPane.showMessageDialog(this, "Masukkan tanggal dan waktu!");
            return;
        }

        try {
            String petData = dropPets.getSelectedItem().toString();
            int idHewan = Integer.parseInt(petData.split("\\|")[0]);

            Timestamp tanggal = Timestamp.valueOf(dateText);

            Customer customer = (Customer) SessionManager.getInstance().getCurrentUser();
            if (customer == null) {
                JOptionPane.showMessageDialog(this, "User tidak login!");
                return;
            }

            Connection conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            String sqlGetCustomerId = "SELECT c.id_customer FROM customer c "
                    + "JOIN users u ON c.id_user = u.id_user "
                    + "WHERE u.username = ?";
            PreparedStatement psGetId = conn.prepareStatement(sqlGetCustomerId);
            psGetId.setString(1, customer.getUsername());
            ResultSet rsGetId = psGetId.executeQuery();

            int idCustomer = 0;
            if (rsGetId.next()) {
                idCustomer = rsGetId.getInt("id_customer");
                System.out.println("[DEBUG] ID Customer dari database: " + idCustomer);
            } else {
                JOptionPane.showMessageDialog(this, "Data customer tidak ditemukan!");
                return;
            }

            String sqlReservasi = "INSERT INTO reservasi (id_customer, id_hewan, tanggal, status_reservasi) VALUES (?, ?, ?, 'PENDING')";
            PreparedStatement psReservasi = conn.prepareStatement(sqlReservasi, Statement.RETURN_GENERATED_KEYS);
            psReservasi.setInt(1, idCustomer); 
            psReservasi.setInt(2, idHewan);
            psReservasi.setTimestamp(3, tanggal);
            psReservasi.executeUpdate();

            int idReservasi = 0;
            ResultSet rs = psReservasi.getGeneratedKeys();
            if (rs.next()) {
                idReservasi = rs.getInt(1);
            }

            System.out.println("[DEBUG] Reservasi ID baru: " + idReservasi);

            String sqlLayanan = "INSERT INTO reservasi_layanan (id_reservasi, id_layanan, harga_satuan) VALUES (?, ?, ?)";
            PreparedStatement psLayanan = conn.prepareStatement(sqlLayanan);

            for (ServiceItem item : selectedServices) {
                psLayanan.setInt(1, idReservasi);
                psLayanan.setInt(2, item.idLayanan);
                psLayanan.setDouble(3, item.harga);
                psLayanan.addBatch();
                System.out.println("[DEBUG] Tambah layanan: " + item.namaLayanan + " - Rp " + item.harga);
            }

            psLayanan.executeBatch();

            conn.commit();
            conn.setAutoCommit(true);

            JOptionPane.showMessageDialog(this,
                    "Reservasi berhasil dibuat dengan " + selectedServices.size() + " layanan!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            this.dispose();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
   
    }//GEN-LAST:event_btnSubmitActionPerformed

    private void tfDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfDateActionPerformed

    private void btnAddServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddServiceActionPerformed
        // TODO add your handling code here:
        String selected = dropService.getSelectedItem().toString();

        if (selected.equals("-- Pilih Layanan --")) {
            JOptionPane.showMessageDialog(this, "Pilih layanan terlebih dahulu!");
            return;
        }

        try {
            String[] parts = selected.split("\\|", 2);
            int idLayanan = Integer.parseInt(parts[0]);

            String[] nameAndPrice = parts[1].split(" - Rp ");
            String namaLayanan = nameAndPrice[0];
            double harga = Double.parseDouble(nameAndPrice[1].replace(".", "").replace(",", ""));

            for (ServiceItem item : selectedServices) {
                if (item.idLayanan == idLayanan) {
                    JOptionPane.showMessageDialog(this, "Layanan ini sudah dipilih!");
                    return;
                }
            }

            selectedServices.add(new ServiceItem(idLayanan, namaLayanan, harga));

            DefaultTableModel model = (DefaultTableModel) jTabeService.getModel();
            model.addRow(new Object[]{
                namaLayanan,
                "Rp " + String.format("%,.0f", harga).replace(",", ".")
            });

            updateTotalLabel();

            dropService.setSelectedIndex(0);

            System.out.println("[DEBUG] Layanan ditambahkan: " + namaLayanan);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error parsing: " + e.getMessage());
        }
    }//GEN-LAST:event_btnAddServiceActionPerformed

    private void updateTotalLabel() {
        double total = 0;
        for (ServiceItem item : selectedServices) {
            total += item.harga;
        }
        lblTotal.setText("Total: Rp " + String.format("%,.0f", total).replace(",", "."));
    }
    
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
        java.awt.EventQueue.invokeLater(new Runnable(){
            public void run() {
                new view.FormReservasiFrame().setVisible(true);
            }
        });
    }

    


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddService;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JComboBox<String> dropPets;
    private javax.swing.JComboBox<String> dropService;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTabeService;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JTextField tfDate;
    // End of variables declaration//GEN-END:variables
}
