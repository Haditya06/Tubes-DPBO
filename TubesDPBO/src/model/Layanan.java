/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author syahrilfarhan
 */
public class Layanan {
    private String idLayanan;
    private String namaLayanan;
    private double harga;

    public Layanan(String idLayanan,
                   String namaLayanan,
                   double harga) {

        this.idLayanan = idLayanan;
        this.namaLayanan = namaLayanan;
        this.harga = harga;
    }

    public String getIdLayanan() {
        return idLayanan;
    }

    public String getNamaLayanan() {
        return namaLayanan;
    }

    public double getHarga() {
        return harga;
    }

    @Override
    public String toString() {
        return namaLayanan + " (Rp " + harga + ")";
    }
}
