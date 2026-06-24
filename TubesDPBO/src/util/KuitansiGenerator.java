/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import model.Tagihan;
import model.DetailTransaksi;
import model.Layanan;

public class KuitansiGenerator {

    private String formatTanggal;

    public KuitansiGenerator(String formatTanggal) {
        this.formatTanggal = formatTanggal;
    }

    // ✅ TAMBAHKAN METHOD STATIC INI (Bisa dipanggil dari mana saja)
    public static String formatRupiah(double jumlah) {
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

    // Method cetakKuitansi yang sudah ada
    public String cetakKuitansi(Tagihan tagihan) {
        StringBuilder sb = new StringBuilder();
        sb.append("============================================\n");
        sb.append("            HI PAW VETCARE                 \n");
        sb.append("============================================\n");
        sb.append("ID Tagihan  : ").append(tagihan.getIdTagihan()).append("\n");

        String namaPelanggan = (tagihan.getReservasi() != null
                && tagihan.getReservasi().getPelanggan() != null)
                ? tagihan.getReservasi().getPelanggan().getNamaLengkap()
                : (tagihan.getNamaPelanggan() != null ? tagihan.getNamaPelanggan() : "Unknown");

        sb.append("Pelanggan   : ").append(namaPelanggan).append("\n");
        sb.append("--------------------------------------------\n");

        // Rincian Obat
        sb.append("RINCIAN OBAT:\n");
        for (DetailTransaksi detail : tagihan.getDaftarDetail()) {
            String namaObat = (detail.getObat() != null)
                    ? ((model.Obat) detail.getObat()).getNamaObat()
                    : "Obat Tidak Dikenal";
            sb.append("  - ").append(namaObat)
                    .append(" x").append(detail.getQty())
                    .append(" = ").append(formatRupiah(detail.hitungSubtotal())).append("\n");
        }

        // Rincian Layanan
        if (tagihan.getDaftarLayanan() != null && !tagihan.getDaftarLayanan().isEmpty()) {
            sb.append("\nRINCIAN LAYANAN:\n");
            for (Layanan layanan : tagihan.getDaftarLayanan()) {
                sb.append("  - ").append(layanan.getNamaLayanan())
                        .append(" = ").append(formatRupiah(layanan.getHarga())).append("\n");
            }
        }

        sb.append("--------------------------------------------\n");
        sb.append("TOTAL  : ").append(formatRupiah(tagihan.hitungTotalBiaya())).append("\n");
        sb.append("============================================\n");
        sb.append("Status : ").append(tagihan.getStatusBayar() ? "LUNAS" : "BELUM LUNAS").append("\n");
        sb.append("============================================\n");

        return sb.toString();
    }

    // Getter & Setter
    public String getFormatTanggal() {
        return formatTanggal;
    }

    public void setFormatTanggal(String formatTanggal) {
        this.formatTanggal = formatTanggal;
    }
}
