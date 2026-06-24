/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Dit
 */
public class Customer extends User{
    private String noHp;
    private String alamat;
    private int idCustomer;
    

    public Customer(String noHp, String alamat, int idCustomer, String username, String password, String namaLengkap) {
        super(username, password, namaLengkap);
        this.noHp = noHp;
        this.alamat= alamat;
        this.idCustomer = idCustomer;
    }
    
    public Customer(String noHp, String alamat,  String username, String password, String namaLengkap) {
        super(username, password, namaLengkap);
        this.noHp = noHp;
        this.alamat= alamat;
    }
    
      public int getIdCustomer() {
        return idCustomer;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }
    
    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    @Override
    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
    
    @Override
    public String getRole() {
        return "Customer";
    }
    
}
