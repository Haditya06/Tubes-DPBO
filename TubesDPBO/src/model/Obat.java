/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Dit
 */
public class Obat {
    private String idObat;
    private String namaObat;
    private int stok;
    private double harga;
    private String deskripsi;

    public Obat() {
    }
    public Obat(String idObat,String namaObat,int stok,double harga) {
        this.idObat = idObat;
        this.namaObat = namaObat;
        this.stok = stok;
        this.harga = harga;
    }
    
    public Obat(int idObat, String namaObat, String deskripsi, double harga, int stok) {
        this.idObat = String.valueOf(idObat); // Convert int ke String
        this.namaObat = namaObat;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.stok = stok;
    }
    public void kurangiStok(int jumlah) {

        if (stok >= jumlah) {
            stok -= jumlah;
        } else {
            System.out.println("Stok tidak cukup");
        }
    }
    public String getNamaObat() {
        return namaObat;
    }
    public double getHarga() {
        return harga;
    }
    
    public String getIdObat() {
        return idObat; 
    }
}
