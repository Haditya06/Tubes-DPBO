/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;


/**
 *
 * @author Dit
 */
import model.User;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;
    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(User user) {
        currentUser = user;
        System.out.println("Login sebagai : " + user.getNamaLengkap());
    }

    public void logout() {
        currentUser = null;
        System.out.println("Logout berhasil");
    }
    public User getCurrentUser() {
        return currentUser;
    }
    public boolean isLoggedIn(){
        return currentUser != null;
    }
    
   
}
