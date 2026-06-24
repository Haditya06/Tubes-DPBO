/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Reservasi {

    private String idReservasi;
    private LocalDateTime tanggalWaktu;
    private Customer customer;
    private Hewan hewan;
    private String status;

    //FIELD BARU untuk menyimpan ID langsung
    private int idCustomer;
    private int idHewan;
    private Timestamp tanggal;

    public Reservasi() {
    }

    // Constructor lama (tetap dipertahankan)
    public Reservasi( String idReservasi, LocalDateTime tanggalWaktu, Customer customer, Hewan hewan) {
        this.idReservasi = idReservasi;
        this.tanggalWaktu = tanggalWaktu;
        this.customer = customer;
        this.hewan = hewan;
        status = "MENUNGGU";
    }

    // CONSTRUCTOR BARU untuk FormReservasi
    public Reservasi(int idCustomer, int idHewan, Timestamp tanggal, String status) {
        this.idCustomer = idCustomer;
        this.idHewan = idHewan;
        this.tanggal = tanggal;
        this.status = status;
    }
    
    public Reservasi(String idReservasi, Customer customer, Hewan hewan,
            java.sql.Timestamp tanggal, String status) {
        this.idReservasi = idReservasi;
        this.customer = customer;
        this.hewan = hewan;
        this.tanggal = tanggal;
        this.status = status;
    }
    
    public void buatReservasi() {
        System.out.println("Reservasi berhasil dibuat");
    }

    public void updateStatus(String statusBaru) {
        status = statusBaru;
        System.out.println("Status berubah menjadi " + statusBaru);
    }

    // Getters & Setters yang sudah ada
    public String getIdReservasi() {
        return idReservasi;
    }

    public LocalDateTime getTanggalWaktu() {
        return tanggalWaktu;
    }

    public String getStatus() {
        return status;
    }

    public Hewan getHewan() {
        return hewan;
    }

    public Customer getPelanggan() {
        return customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    // 🔥 GETTERS & SETTERS BARU
    public int getIdCustomer() {
        return idCustomer;
    }

    public int getIdHewan() {
        return idHewan;
    }

    public Timestamp getTanggal() {
        return tanggal;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    public void setIdHewan(int idHewan) {
        this.idHewan = idHewan;
    }

    public void setTanggal(Timestamp tanggal) {
        this.tanggal = tanggal;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
