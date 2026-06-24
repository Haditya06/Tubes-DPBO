/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;
import database.DatabaseConnection;
import model.DetailTransaksi;
import model.Tagihan;
import util.KuitansiGenerator;
import util.SessionManager;
import model.StaffAdministrasi;
import java.sql.*; 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author Dit
 */
public class FormPembayaranFrame extends javax.swing.JFrame {
    
    private Tagihan tagihan;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FormPembayaranFrame.class.getName());

    /**
     * Creates new form FormPembayaranFrame
     */
    public FormPembayaranFrame() {
        initComponents();
        setLocationRelativeTo(null);
        // PENTING: DISPOSE_ON_CLOSE biar nggak nutup semua aplikasi saat form ini di-close
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }
    
    public FormPembayaranFrame(Tagihan tagihan, int reservationId) {
        this(); // Panggil constructor default di atas
        this.tagihan = tagihan;
        tampilkanData();
        setupAutoCalculate();
        loadReservationData(reservationId); 
    }

    public FormPembayaranFrame(Tagihan tagihan) {
        this(); // Panggil constructor default
        this.tagihan = tagihan;

        if (tagihan != null) {
            tampilkanData();       // Ini sudah mengisi tabel & text field
            setupAutoCalculate();

            // HAPUS ATAU KOMENTARI BAGIAN loadReservationData INI
            // Karena menyebabkan error "Unknown column" dan data sudah diload oleh tampilkanData()
            /*
        try {
            String idTagihan = tagihan.getIdTagihan();
            int reservationId = Integer.parseInt(idTagihan.replace("R-", ""));
            loadReservationData(reservationId); 
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
             */
        }   
    }
    
    private void tampilkanData() {
        if (tagihan == null) {
            return;
        }

        // Set data reservasi
        tfReservationID.setText(tagihan.getReservasi().getIdReservasi());
        tfReservationID.setEditable(false);
        tfPatientPet.setText(tagihan.getReservasi().getHewan().getNamaHewan());
        tfPatientPet.setEditable(false);
        tfOwner.setText(tagihan.getReservasi().getPelanggan().getNamaLengkap());
        tfOwner.setEditable(false);

        double totalObat = tagihan.hitungTotalBiaya();
        double biayaLayanan = getBiayaLayananDariDB();  
        double totalKeseluruhan = totalObat + biayaLayanan;

        System.out.println("[DEBUG] Biaya layanan: Rp " + biayaLayanan);
        System.out.println("[DEBUG] Total obat: Rp " + totalObat);
        System.out.println("[DEBUG] TOTAL KESELURUHAN: Rp " + totalKeseluruhan);

        tfTotalCost.setText(KuitansiGenerator.formatRupiah(totalKeseluruhan));
        tfTotalCost.setEditable(false);
        tfTotalCost.setFont(new java.awt.Font("Segoe UI", 1, 18));

        // Isi tabel invoice detail
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        if (biayaLayanan > 0) {
            model.addRow(new Object[]{
                "Biaya Pemeriksaan",
                1,
                KuitansiGenerator.formatRupiah(biayaLayanan)
            });
        }

        // Tambahkan baris untuk obat
        for (DetailTransaksi detail : tagihan.getDaftarDetail()) {
            model.addRow(new Object[]{
                detail.getNamaItem(),
                detail.getQty(),
                KuitansiGenerator.formatRupiah(detail.hitungSubtotal())
            });
        }
    }

    private double getBiayaLayananDariDB() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String idText = tfReservationID.getText().replace("R-", "").trim();
            int idReservasi = Integer.parseInt(idText);

            String sql = "SELECT biaya_layanan FROM pemeriksaan WHERE id_reservasi = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idReservasi);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("biaya_layanan");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    private void setupAutoCalculate() {
        tfCashAmout.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                hitungKembalian();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                hitungKembalian();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                hitungKembalian();
            }
        });
    }
    
    private void hitungKembalian() {
        try {
            String cashText = tfCashAmout.getText().trim();
            if (cashText.isEmpty()) {
                tfChangeDue.setText("Rp 0");
                tfChangeDue.setForeground(java.awt.Color.BLACK);
                return;
            }

            double cash = Double.parseDouble(cashText);

            double biayaLayanan = getBiayaLayananDariDB();
            double totalObat = 0;
            for (DetailTransaksi detail : tagihan.getDaftarDetail()) {
                totalObat += detail.hitungSubtotal();
            }
            double totalKeseluruhan = biayaLayanan + totalObat;

            double kembalian = cash - totalKeseluruhan;

            System.out.println("[DEBUG] Cash: " + cash);
            System.out.println("[DEBUG] Biaya Layanan: " + biayaLayanan);
            System.out.println("[DEBUG] Total Obat: " + totalObat);
            System.out.println("[DEBUG] Total Keseluruhan: " + totalKeseluruhan);
            System.out.println("[DEBUG] Kembalian: " + kembalian);

            if (kembalian >= 0) {
                tfChangeDue.setText("Rp " + String.format("%,.0f", kembalian).replace(",", "."));
                tfChangeDue.setForeground(new java.awt.Color(0, 128, 0)); // Hijau
            } else {
                tfChangeDue.setText("Kurang: Rp " + String.format("%,.0f", Math.abs(kembalian)).replace(",", "."));
                tfChangeDue.setForeground(new java.awt.Color(255, 0, 0)); // Merah
            }
        } catch (NumberFormatException e) {
            tfChangeDue.setText("Input tidak valid!");
            tfChangeDue.setForeground(java.awt.Color.RED);
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
        jLabel3 = new javax.swing.JLabel();
        tfOwner = new javax.swing.JTextField();
        tfPatientPet = new javax.swing.JTextField();
        tfReservationID = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        JPPayment = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        tfTotalCost = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        tfCashAmout = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        tfChangeDue = new javax.swing.JTextField();
        btnProcess = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Reservation ID:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Patient Pet:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Owner:");

        tfOwner.setText("jTextField1");

        tfPatientPet.setText("jTextField1");

        tfReservationID.setText("jTextField1");
        tfReservationID.addActionListener(this::tfReservationIDActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfReservationID, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfPatientPet, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(29, 29, 29)
                .addComponent(tfOwner, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(tfOwner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfPatientPet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfReservationID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Invoice Detail");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Item/Service", "Qty", "Sub Total"
            }
        ));
        JPPayment.setViewportView(jTable1);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setText("Total Cost: Rp.");

        tfTotalCost.setText("jTextField1");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(194, 194, 194)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfTotalCost)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tfTotalCost, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(23, 23, 23))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Cash Amout (Rp):");

        tfCashAmout.setText("0");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Change Due (Rp):");

        tfChangeDue.setText("0");
        tfChangeDue.addActionListener(this::tfChangeDueActionPerformed);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfCashAmout, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(tfChangeDue, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(74, 74, 74))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfCashAmout, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(tfChangeDue, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        btnProcess.setText("Process Payment & Print Receipt");
        btnProcess.addActionListener(this::btnProcessActionPerformed);

        btnBack.setText("Back");
        btnBack.addActionListener(this::btnBackActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JPPayment, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnProcess, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBack)
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(btnBack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnProcess, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tfReservationIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfReservationIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfReservationIDActionPerformed

    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        // TODO add your handling code here:
        try {
            String cashText = tfCashAmout.getText().trim();
            if (cashText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Cash amount cannot be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double cash = Double.parseDouble(cashText);
            double total = tagihan.hitungTotalBiaya();

            double biayaLayanan = getBiayaLayananDariDB();
            double totalObat = 0;
            for (DetailTransaksi detail : tagihan.getDaftarDetail()) {
                totalObat += detail.hitungSubtotal();
            }
            double totalKeseluruhan = biayaLayanan + totalObat;

            System.out.println("[DEBUG] Biaya Layanan: Rp " + biayaLayanan);
            System.out.println("[DEBUG] Total Obat: Rp " + totalObat);
            System.out.println("[DEBUG] TOTAL KESELURUHAN: Rp " + totalKeseluruhan);

            if (cash < totalKeseluruhan) {
                JOptionPane.showMessageDialog(this,
                        "Cash amount is not enough!\n\n"
                        + "Total: Rp " + String.format("%,.0f", totalKeseluruhan).replace(",", ".") + "\n"
                        + "Cash: Rp " + String.format("%,.0f", cash).replace(",", ".") + "\n"
                        + "Short: Rp " + String.format("%,.0f", totalKeseluruhan - cash).replace(",", "."),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            double kembalian = cash - totalKeseluruhan;

            boolean sukses = updateStatusPembayaran();

            if (sukses) {
                String struk = generateStruk(cash, kembalian);
                JOptionPane.showMessageDialog(this, "✅ Payment Successful!\n\n" + struk, "Success", JOptionPane.INFORMATION_MESSAGE);

                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update database status.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for cash amount!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnProcessActionPerformed

    
    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void tfChangeDueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfChangeDueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfChangeDueActionPerformed

    private boolean updateStatusPembayaran() {
        try {
            Connection conn = DatabaseConnection.getConnection();

            // Parse ID dari "R-9" menjadi 9
            String idText = tfReservationID.getText().replace("R-", "").trim();
            int idReservasi = Integer.parseInt(idText);

            System.out.println("=== DEBUG PAYMENT ===");
            System.out.println("Reservation ID: " + idReservasi);

            // 1. Cari ID pemeriksaan untuk reservasi ini
            String sqlGetPemeriksaan = "SELECT id_pemeriksaan FROM pemeriksaan WHERE id_reservasi = ?";
            PreparedStatement psGet = conn.prepareStatement(sqlGetPemeriksaan);
            psGet.setInt(1, idReservasi);
            ResultSet rsGet = psGet.executeQuery();

            int idPemeriksaan = 0;
            if (rsGet.next()) {
                idPemeriksaan = rsGet.getInt("id_pemeriksaan");
                System.out.println("ID Pemeriksaan: " + idPemeriksaan);
            } else {
                System.err.println("[ERROR] Tidak ada pemeriksaan untuk reservasi ini!");
                return false;
            }

            // 2. Insert ke tabel tagihan
            double total = tagihan.hitungTotalBiaya();
            String sqlTagihan = "INSERT INTO tagihan (id_pemeriksaan, total, status_bayar) "
                    + "VALUES (?, ?, 'LUNAS')";
            PreparedStatement psTagihan = conn.prepareStatement(sqlTagihan, Statement.RETURN_GENERATED_KEYS);
            psTagihan.setInt(1, idPemeriksaan);
            psTagihan.setDouble(2, total);
            psTagihan.executeUpdate();

            int idTagihan = 0;
            ResultSet rsTagihan = psTagihan.getGeneratedKeys();
            if (rsTagihan.next()) {
                idTagihan = rsTagihan.getInt(1);
                System.out.println("ID Tagihan baru: " + idTagihan);
            }

            // 3. Insert ke tabel pembayaran
            String sqlPembayaran = "INSERT INTO pembayaran (id_tagihan, total_bayar, tanggal_bayar) "
                    + "VALUES (?, ?, NOW())";
            PreparedStatement psBayar = conn.prepareStatement(sqlPembayaran);
            psBayar.setInt(1, idTagihan);
            psBayar.setDouble(2, total);
            psBayar.executeUpdate();

            System.out.println("Pembayaran disimpan: Rp " + total);

            // 4. Update status reservasi
            String sqlUpdate = "UPDATE reservasi SET status_reservasi = 'LUNAS' WHERE id_reservasi = ?";
            PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
            psUpdate.setInt(1, idReservasi);
            int rows = psUpdate.executeUpdate();

            System.out.println("Rows affected: " + rows);
            System.out.println("=== END DEBUG ===");

            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (NumberFormatException e) {
            System.err.println("[ERROR] Invalid ID format: " + tfReservationID.getText());
            e.printStackTrace();
            return false;
        }
    }
    
    private String generateStruk(double cash, double kembalian) {
        double biayaLayanan = getBiayaLayananDariDB();
        double totalObat = 0;
        for (DetailTransaksi detail : tagihan.getDaftarDetail()) {
            totalObat += detail.hitungSubtotal();
        }
        double totalKeseluruhan = biayaLayanan + totalObat;

        StringBuilder sb = new StringBuilder();
        sb.append("================================\n");
        sb.append("       HI-PAW VET CLINIC\n");
        sb.append("================================\n");
        sb.append("ID  : ").append(tfReservationID.getText()).append("\n");
        sb.append("Pet : ").append(tfPatientPet.getText()).append("\n");
        sb.append("Owner: ").append(tfOwner.getText()).append("\n");
        sb.append("--------------------------------\n");

        if (biayaLayanan > 0) {
            sb.append("Biaya Pemeriksaan: Rp ").append(String.format("%,.0f", biayaLayanan).replace(",", ".")).append("\n");
        }

        for (DetailTransaksi detail : tagihan.getDaftarDetail()) {
            sb.append(detail.getNamaItem()).append(" x").append(detail.getQty())
                    .append(" = Rp ").append(String.format("%,.0f", detail.hitungSubtotal()).replace(",", ".")).append("\n");
        }

        sb.append("--------------------------------\n");
        sb.append("Total: Rp ").append(String.format("%,.0f", totalKeseluruhan).replace(",", ".")).append("\n");
        sb.append("Cash : Rp ").append(String.format("%,.0f", cash).replace(",", ".")).append("\n");
        sb.append("Change: Rp ").append(String.format("%,.0f", kembalian).replace(",", ".")).append("\n");
        sb.append("================================\n");
        sb.append("   Thank you for your visit!\n");
        return sb.toString();
    }
    
    // Method untuk load data reservasi
    private void loadReservationData(int reservationId) {
        try {
            Connection conn = DatabaseConnection.getConnection();

            String sql = "SELECT r.*, h.nama_hewan, u.nama_lengkap "
                    + "FROM reservasi r "
                    + "JOIN hewan h ON r.id_hewan = h.id_hewan "
                    + "JOIN customer c ON r.id_customer = c.id_customer "
                    + "JOIN users u ON c.id_user = u.id_user "
                    + "WHERE r.id_reservasi = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, reservationId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Set data ke field-field yang ada
                // Misalnya:
                // tfReservationId.setText(String.valueOf(rs.getInt("id_reservasi")));
                // tfPetName.setText(rs.getString("nama_hewan"));
                // tfOwnerName.setText(rs.getString("nama_lengkap"));

                // Load item layanan (sesuaikan dengan tabel layanan/pembayaran kamu)
                loadServiceItems(reservationId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

// Load item layanan untuk reservasi ini
    private void loadServiceItems(int reservationId) {
        try {
            Connection conn = DatabaseConnection.getConnection();

            // Ambil layanan dari reservasi (sesuaikan query dengan struktur database kamu)
            String sql = "SELECT * FROM pembayaran WHERE id_reservasi = ?";
            // Atau mungkin dari tabel layanan? Sesuaikan dengan struktur database kamu

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, reservationId);
            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = new DefaultTableModel(
                    new String[]{"Item/Service", "Qty", "Sub Total"}, 0
            );

            double totalCost = 0;

            while (rs.next()) {
                String serviceName = rs.getString("layanan"); // sesuaikan kolom
                int qty = rs.getInt("qty");
                double price = rs.getDouble("harga");
                double subTotal = qty * price;

                model.addRow(new Object[]{serviceName, qty, "Rp " + String.format("%,d", subTotal)});
                totalCost += subTotal;
            }

            // Set model ke tabel
            // tblInvoiceDetail.setModel(model);
            // Set total cost
            // tfTotalCost.setText("Rp " + String.format("%,d", (int)totalCost));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
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
        java.awt.EventQueue.invokeLater(() -> new FormPembayaranFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane JPPayment;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnProcess;
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
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField tfCashAmout;
    private javax.swing.JTextField tfChangeDue;
    private javax.swing.JTextField tfOwner;
    private javax.swing.JTextField tfPatientPet;
    private javax.swing.JTextField tfReservationID;
    private javax.swing.JTextField tfTotalCost;
    // End of variables declaration//GEN-END:variables
}
