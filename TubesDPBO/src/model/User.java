/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Dit
 */
public abstract class User {
    
    protected String username;
    protected String password;
    protected String namaLengkap;
    public User(){
        
    }

    public User(String username,String password,String namaLengkap) {
        this.username = username;
        this.password = password;
        this.namaLengkap = namaLengkap;
    }
    
    public abstract String getRole();
    
    public boolean login() {
        System.out.println(namaLengkap + " berhasil login");
        return true;
    }

    public void logout() {
        System.out.println(namaLengkap + " logout");
    }

    public String getUsername() {
        return username;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }
    public String getPassword(){
        return password;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setNamaLengkap(String namaLengkap){
        this.namaLengkap = namaLengkap;
    }
    public void setPassword(String password){
        this.password = password;
    }
}
