/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.util.ArrayList;
//import java.util.*;
/**
 *
 * @author Dit
 */
public class Tagihan {
    private int idTagihan;
    private Reservasi reservasi;
    private ArrayList<DetailTransaksi> daftarDetail;  // rincian obat
    private ArrayList<Layanan> daftarLayanan; // rincian layanan
    private double totalBiaya;   // dihitung saat runtime (tambahDetail/tambahLayanan)
    private boolean statusBayar; 
    
    private double total;             // dari kolom tagihan.total di DB
    private String namaPelanggan;     // dari join ke users.nama_lengkap
    private java.sql.Date tanggalTransaksi;  // dari join ke reservasi.tanggal
    
    public Tagihan(int idTagihan, Reservasi reservasi) {
        this.idTagihan = idTagihan;
        this.reservasi = reservasi;
        this.daftarDetail = new ArrayList<>();
        this.daftarLayanan = new ArrayList<>();
        this.totalBiaya = 0;
        this.statusBayar = false;
    }
    
    public Tagihan(int idTagihan, Reservasi reservasi, ArrayList<Layanan> daftarLayanan) {
        this.idTagihan = idTagihan;
        this.reservasi = reservasi;
        this.daftarDetail = new ArrayList<>();
        // PERBAIKAN: daftarLayanan != null ? daftarLayanan pakai parameter, bukan field
        this.daftarLayanan = (daftarLayanan != null) ? daftarLayanan : new ArrayList<>();
        this.totalBiaya = 0;
        this.statusBayar = false;

        // PERBAIKAN: loop syntax yang benar
        for (Layanan layanan : this.daftarLayanan) {
            this.totalBiaya += layanan.getHarga();
        }
    }

    
    public void tambahDetail(DetailTransaksi detail) { //methode detail transaksi
        daftarDetail.add(detail);
        totalBiaya += detail.hitungSubtotal();
    }
    
    public void tambahLayanan(Layanan layanan) { // untuk menambahkan layanan ke tagihan
        daftarLayanan.add(layanan);
        totalBiaya += layanan.getHarga();
    }
    
    public double hitungTotalBiaya() {
        return totalBiaya;
    }
    
    public void setLunas() {
        this.statusBayar = true;
    }
    
    
    //setter untuk data dari db
    public void setNamaPelanggan(String namaPelanggan) {
        this.namaPelanggan = namaPelanggan;
    }

    public void setTanggalTransaksi(java.sql.Date tanggalTransaksi) {
        this.tanggalTransaksi = tanggalTransaksi;
    }
    
    public void setTotal(double total) {
        this.total = total;
    }
    
    //getter ku
    public int getIdTagihan() {
        return idTagihan;
    }

    public Reservasi getReservasi() {
        return reservasi;
    }

    public ArrayList<DetailTransaksi> getDaftarDetail() {
        return daftarDetail;
    }

    public ArrayList<Layanan> getDaftarLayanan() {
        return daftarLayanan;
    }

    public double getTotalBiaya() {
        return totalBiaya;
    }

    public boolean getStatusBayar() {
        return statusBayar;
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public java.sql.Date getTanggalTransaksi() {
        return tanggalTransaksi;
    }
    
    // mengembalikan total dari db
   public double getTotal() {
        return total;
    }
   
    // getPelanggan dari class Reservasi
    @Override
    public String toString() {
        String nama = (reservasi != null && reservasi.getPelanggan() != null)
                ? reservasi.getPelanggan().getNamaLengkap()
                : (namaPelanggan != null ? namaPelanggan : "Unknown");

        return "[Tagihan] ID: " + idTagihan
                + " | Pelanggan: " + nama
                + " | Total: Rp " + totalBiaya
                + " | Lunas: " + (statusBayar ? "Ya" : "Tidak");
    }
}
