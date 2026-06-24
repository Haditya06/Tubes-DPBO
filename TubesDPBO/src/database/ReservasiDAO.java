/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.sql.*;
import model.Reservasi;
import model.Customer;
import util.SessionManager;

public class ReservasiDAO {

    // 🔹 METHOD BARU: Insert Reservasi ke Database
    public void insertReservation(Reservasi r) {
        try {
            Connection conn = DatabaseConnection.getConnection();

            Customer currentCustomer = (Customer) SessionManager.getInstance().getCurrentUser();

            if (currentCustomer == null) {
                System.err.println("[ERROR] Tidak ada user yang login!");
                return;
            }

            System.out.println("[DEBUG] Username customer: " + currentCustomer.getUsername());

            String sqlGetCustomerId = "SELECT c.id_customer FROM customer c "
                    + "JOIN users u ON c.id_user = u.id_user "
                    + "WHERE u.username = ?";
            PreparedStatement psGetId = conn.prepareStatement(sqlGetCustomerId);
            psGetId.setString(1, currentCustomer.getUsername());
            ResultSet rs = psGetId.executeQuery();

            int idCustomer = 0;
            if (rs.next()) {
                idCustomer = rs.getInt("id_customer");
                System.out.println("[DEBUG] ID Customer dari database: " + idCustomer);
            } else {
                System.err.println("[ERROR] Customer tidak ditemukan di database!");
                return;
            }

            String sql = "INSERT INTO reservasi (id_customer, id_hewan, tanggal, status_reservasi) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, idCustomer);  
            ps.setInt(2, r.getIdHewan());
            ps.setTimestamp(3, r.getTanggal());
            ps.setString(4, r.getStatus());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("[ReservasiDAO] Reservasi berhasil disimpan!");
                System.out.println("  - ID Customer: " + idCustomer);
                System.out.println("  - ID Hewan: " + r.getIdHewan());
                System.out.println("  - Tanggal: " + r.getTanggal());
                System.out.println("  - Status: " + r.getStatus());
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[ReservasiDAO] Gagal simpan reservasi: " + e.getMessage());
        }
    }
}
