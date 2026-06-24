/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package business.logic;
import database.DatabaseConnection; 
import model.Tagihan;
import util.KuitansiGenerator;
 
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author syahrilfarhan
 */
public class RiwayatTransaksi {
    private List<Tagihan> listTransaksi;
    
    public RiwayatTransaksi(List<Tagihan> listTransaksi) {
        this.listTransaksi = (listTransaksi != null) ? listTransaksi : new ArrayList<>();
    }
    
    public RiwayatTransaksi() {
        this.listTransaksi = new ArrayList<>();
    }
    
    //tambah Manual
    public void tambahTagihan(Tagihan tagihan) {
        if (tagihan.getStatusBayar()) {
            listTransaksi.add(tagihan);
            System.out.println("[RiwayatTransaksi] Tagihan " + tagihan.getIdTagihan() + " berhasil ditambahkan ke riwayat.");
        } else {
            System.out.println("[RiwayatTransaksi] Gagal: Tagihan " + tagihan.getIdTagihan() + " belum lunas.");
        }
    }
    
    //ambil dari database
    
    public List<Tagihan> ambilDataLunasDariDB() {
        System.out.println("[RiwayatTransaksi] Mengambil data transaksi lunas dari Database...");
        listTransaksi.clear();

        String sql
                = "SELECT t.id_tagihan, t.total, t.status_bayar, "
                + "       u.nama_lengkap, r.tanggal "
                + "FROM tagihan t "
                + "JOIN pemeriksaan p ON t.id_pemeriksaan = p.id_pemeriksaan "
                + "JOIN reservasi r   ON p.id_reservasi   = r.id_reservasi "
                + "JOIN customer c    ON r.id_customer     = c.id_customer "
                + "JOIN users u ON c.id_user = u.id_user "
                + "WHERE t.status_bayar = 1 "
                + "ORDER BY r.tanggal DESC";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idTagihan = rs.getInt("id_tagihan");
                double total = rs.getDouble("total");
                String namaPelanggan = rs.getString("nama_lengkap");
                java.sql.Date tanggal = rs.getDate("tanggal");

                Tagihan tagihan = new Tagihan(idTagihan, null);
                tagihan.setTotal(total);
                tagihan.setLunas();
                tagihan.setNamaPelanggan(namaPelanggan);
                tagihan.setTanggalTransaksi(tanggal);

                listTransaksi.add(tagihan);
            }

            System.out.println("[RiwayatTransaksi] Berhasil mengambil "
                    + listTransaksi.size() + " data transaksi.");

        } catch (SQLException e) {
            System.err.println("[RiwayatTransaksi] ERROR: " + e.getMessage());
            e.printStackTrace();
        }

        return listTransaksi;
    }
    
    
    //tambilkan ke console
    
    public void tampilkanRiwayat() {
        if (listTransaksi.isEmpty()) {
            System.out.println("[RiwayatTransaksi] Belum ada riwayat transaksi.");
            return;
        }

        System.out.println("====================================================");
        System.out.println("           RIWAYAT TRANSAKSI KLINIK                 ");
        System.out.println("====================================================");
        System.out.printf("%-5s | %-10s | %-20s | %15s%n",
                "No", "ID", "Pelanggan", "Total Biaya");
        System.out.println("----------------------------------------------------");

        int nomor = 1;
        double grandTotal = 0;

        for (Tagihan tagihan : listTransaksi) {
            // PERBAIKAN: pakai getNamaPelanggan(), bukan hardcode "Lihat di Database"
            String nama = tagihan.getNamaPelanggan() != null
                    ? tagihan.getNamaPelanggan() : "Unknown";
            double total = tagihan.getTotal() > 0
                    ? tagihan.getTotal() : tagihan.hitungTotalBiaya();

            System.out.printf("%-5d | %-10d | %-20s | %15s%n",
                    nomor++,
                    tagihan.getIdTagihan(),
                    nama,
                    KuitansiGenerator.formatRupiah(total));

            grandTotal += total;
        }

        System.out.println("----------------------------------------------------");
        System.out.println("Total Transaksi : " + getTotalTransaksi());
        System.out.println("Grand Total     : " + KuitansiGenerator.formatRupiah(grandTotal));
        System.out.println("====================================================");
    }
    
    // cari transaksi
    
    public Tagihan cariTransaksi(int idTagihan) {
        for (Tagihan tagihan : listTransaksi) {
            if (tagihan.getIdTagihan() == idTagihan) {
                return tagihan;
            }
        }
        return null;
    }
    
    //Getter
    public int getTotalTransaksi() {
        return listTransaksi.size();
    }
    
    public List<Tagihan> getListTransaksi() {
        return listTransaksi;
    }

    @Override
    public String toString() {
        return "[RiwayatTransaksi] Jumlah total transaksi: " + getTotalTransaksi();
    }
}
