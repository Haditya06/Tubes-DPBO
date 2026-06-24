/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class DetailTransaksi {

    private String idDetail;
    private String namaItem;
    private int qty;
    private double hargaSatuan;
    private Obat obat;
    private Layanan layanan;

    // ✅ CONSTRUCTOR LAMA (untuk StaffDashboardFrame & FormPemeriksaanFrame)
    public DetailTransaksi(String idDetail, String namaItem, int qty, double hargaSatuan) {
        this.idDetail = idDetail;
        this.namaItem = namaItem;
        this.qty = qty;
        this.hargaSatuan = hargaSatuan;
    }

    // Constructor untuk OBAT
    public DetailTransaksi(Obat obat, int qty) {
        this.obat = obat;
        this.namaItem = obat.getNamaObat();
        this.qty = qty;
        this.hargaSatuan = obat.getHarga();
    }

    // Constructor untuk LAYANAN
    public DetailTransaksi(Layanan layanan, int qty) {
        this.layanan = layanan;
        this.namaItem = layanan.getNamaLayanan();
        this.qty = qty;
        this.hargaSatuan = layanan.getHarga();
    }

    // ✅ GETTERS & SETTERS
    public String getIdDetail() {
        return idDetail;
    }

    public String getNamaItem() {  // ✅ Method ini yang dipanggil FormPembayaranFrame
        return namaItem;
    }

    public int getQty() {
        return qty;
    }

    public double getHargaSatuan() {
        return hargaSatuan;
    }

    public Obat getObat() {
        return obat;
    }

    public Layanan getLayanan() {
        return layanan;
    }

    public double hitungSubtotal() {
        return hargaSatuan * qty;
    }
}
