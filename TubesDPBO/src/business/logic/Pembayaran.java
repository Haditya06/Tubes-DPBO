/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package business.logic;

import database.DatabaseConnection;
import enums.StatusReservasi;
import model.DetailTransaksi;
import model.Layanan;
import model.Tagihan;
import model.Obat; // <-- PENTING: Import class Obat

import java.sql.*;

public class Pembayaran {

    private double totalPemasukan;

    public Pembayaran() {
        this.totalPemasukan = 0;
    }

    /**
     * Memproses pembayaran dari customer.
     */
    public boolean prosesBayar(Tagihan tagihan, double uangDibayar) {
        double totalBiaya = tagihan.hitungTotalBiaya();

        if (uangDibayar >= totalBiaya) {
            tagihan.setLunas();
            totalPemasukan += totalBiaya;
            double kembalian = hitungKembalian(uangDibayar, totalBiaya);

            // Update status reservasi ke LUNAS (Hanya 1x)
            if (tagihan.getReservasi() != null) {
                tagihan.getReservasi().updateStatus("LUNAS");
            }

            // Simpan ke tabel pembayaran di DB
            simpanPembayaranKeDB(tagihan.getIdTagihan(), totalBiaya);

            System.out.println("======================================");
            System.out.println("[Pembayaran] Pembayaran Berhasil");
            System.out.println("  Tagihan ID   : " + tagihan.getIdTagihan());
            System.out.println("  Total Biaya  : " + formatRupiah(totalBiaya));
            System.out.println("  Uang Dibayar : " + formatRupiah(uangDibayar));
            System.out.println("  Kembalian    : " + formatRupiah(kembalian));
            System.out.println("======================================");
            return true;

        } else {
            double kurang = totalBiaya - uangDibayar;
            System.out.println("[Pembayaran] Gagal: Uang dibayar tidak mencukupi");
            System.out.println("  Kurang : " + formatRupiah(kurang));
            return false;
        }
    }

    /**
     * INSERT ke tabel pembayaran & UPDATE status_bayar di tagihan
     */
    public void simpanPembayaranKeDB(int idTagihan, double totalBayar) {
        String sqlInsert = "INSERT INTO pembayaran (id_tagihan, total_bayar, tanggal_bayar) VALUES (?, ?, NOW())";
        String sqlUpdate = "UPDATE tagihan SET status_bayar = 1 WHERE id_tagihan = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                psInsert.setInt(1, idTagihan);
                psInsert.setDouble(2, totalBayar);
                psInsert.executeUpdate();
            }

            try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
                psUpdate.setInt(1, idTagihan);
                psUpdate.executeUpdate();
            }

            conn.commit();
            System.out.println("[Pembayaran] Data pembayaran berhasil disimpan ke DB.");

        } catch (SQLException e) {
            System.err.println("[Pembayaran] ERROR simpan ke DB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public double hitungKembalian(double uangDibayar, double totalBiaya) {
        double kembalian = uangDibayar - totalBiaya;
        return kembalian < 0 ? 0 : kembalian;
    }

    /**
     * Cetak struk PERBAIKAN: Casting (model.Obat) dan ganti getJumlah() ->
     * getQty()
     */
    public String cetakStruk(Tagihan tagihan) {
        String namaPelanggan = "Unknown";
        if (tagihan.getReservasi() != null && tagihan.getReservasi().getPelanggan() != null) {
            namaPelanggan = tagihan.getReservasi().getPelanggan().getNamaLengkap();
        } else if (tagihan.getNamaPelanggan() != null) {
            namaPelanggan = tagihan.getNamaPelanggan();
        }

        StringBuilder hasil = new StringBuilder();
        hasil.append("============================================\n");
        hasil.append("            HI PAW VETCARE                 \n");
        hasil.append("============================================\n");
        hasil.append("ID Tagihan  : ").append(tagihan.getIdTagihan()).append("\n");
        hasil.append("Pelanggan   : ").append(namaPelanggan).append("\n");
        hasil.append("--------------------------------------------\n");

        hasil.append("RINCIAN OBAT:\n");
        for (DetailTransaksi detail : tagihan.getDaftarDetail()) {
            // ✅ PERBAIKAN 1: Casting (model.Obat) agar method getNamaObat() dikenali
            String namaObat = (detail.getObat() != null)
                    ? ((model.Obat) detail.getObat()).getNamaObat()
                    : "Obat Tidak Dikenal";

            // ✅ PERBAIKAN 2: Ganti getJumlah() menjadi getQty()
            hasil.append("  - ").append(namaObat)
                    .append(" x").append(detail.getQty())
                    .append(" = ").append(formatRupiah(detail.hitungSubtotal())).append("\n");
        }

        hasil.append("\nRINCIAN LAYANAN:\n");
        for (Layanan layanan : tagihan.getDaftarLayanan()) {
            hasil.append("  - ").append(layanan.getNamaLayanan())
                    .append(" = ").append(formatRupiah(layanan.getHarga())).append("\n");
        }

        hasil.append("--------------------------------------------\n");
        hasil.append("TOTAL  : ").append(formatRupiah(tagihan.hitungTotalBiaya())).append("\n");
        hasil.append("============================================\n");
        hasil.append("Status : ").append(tagihan.getStatusBayar() ? "LUNAS" : "BELUM LUNAS").append("\n");
        hasil.append("============================================\n");

        return hasil.toString();
    }

    // Helper format rupiah
    private String formatRupiah(double jumlah) {
        long jumlahLong = (long) jumlah;
        String angka = String.valueOf(jumlahLong);
        StringBuilder hasil = new StringBuilder();
        int counter = 0;
        for (int i = angka.length() - 1; i >= 0; i--) {
            if (counter > 0 && counter % 3 == 0) {
                hasil.insert(0, ".");
            }
            hasil.insert(0, angka.charAt(i));
            counter++;
        }
        return "Rp " + hasil.toString();
    }

    public double getTotalPemasukan() {
        return totalPemasukan;
    }
}
