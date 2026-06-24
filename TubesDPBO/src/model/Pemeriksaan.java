/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Dit
 */
public class Pemeriksaan {
    private String idPemeriksaan;
    private Reservasi reservasi;
    private Dokter dokter;
    private String diagnosis;
    private String tindakan;
    public Pemeriksaan() {
    }

    public Pemeriksaan(String idPemeriksaan,Reservasi reservasi,Dokter dokter) {
        this.idPemeriksaan = idPemeriksaan;
        this.reservasi = reservasi;
        this.dokter = dokter;
    }
    public void inputDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
    public void inputTindakan(String tindakan) {
        this.tindakan = tindakan;
    }
}
