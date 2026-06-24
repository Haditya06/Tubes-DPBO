/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package database;

/**
 *
 * @author Dit
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {
    

    private static final String URL = "jdbc:mysql://localhost:3306/hazzelnut";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Koneksi berhasil!");
            return conn;

        } catch (SQLException e) {
            System.out.println("Koneksi gagal!");
            e.printStackTrace();
            return null;
        }
    }
}
