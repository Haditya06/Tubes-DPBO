/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Dit
 */
public class Dokter extends User{
    private String idDokter;
    private String spesialisasi;

    public Dokter(String username,String password,String namaLengkap,String idDokter,String spesialisasi) {
        super(username, password, namaLengkap);
        this.idDokter = idDokter;
        this.spesialisasi = spesialisasi;
    }
    public String getIdDokter() {
        return idDokter;
    }
    public String getSpesialisasi() {
        return spesialisasi;
    }
    @Override
    public String getRole(){
        return "Dokter";
    }
}
