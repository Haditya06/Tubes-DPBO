/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.sql.*;
import java.util.ArrayList;
import model.Hewan;

public class HewanDAO {

    private Connection conn;

    public HewanDAO() {
        conn = DatabaseConnection.getConnection();
    }

    // 🔹 1. INSERT HEWAN (Pakai id_customer, bukan username)
    public void insertHewan(Hewan h) {
        try {
            String sql = "INSERT INTO hewan (id_customer, nama_hewan, jenis, umur) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, h.getIdCustomer());  // ← Pakai ID Customer (int)
            ps.setString(2, h.getNamaHewan());
            ps.setString(3, h.getJenis());
            ps.setInt(4, h.getUmur());

            ps.executeUpdate();
            System.out.println("[HewanDAO] Hewan berhasil ditambahkan!");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[HewanDAO] Gagal tambah hewan: " + e.getMessage());
        }
    }

    // 🔹 2. GET HEWAN BY ID CUSTOMER
    public ArrayList<Hewan> getHewanByCustomerId(int idCustomer) {
        ArrayList<Hewan> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM hewan WHERE id_customer = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idCustomer);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Hewan h = new Hewan(
                        rs.getInt("id_hewan"),
                        rs.getString("nama_hewan"),
                        rs.getString("jenis"),
                        rs.getInt("umur"),
                        rs.getInt("id_customer")
                );
                list.add(h);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 3. GET HEWAN BY ID (Single Hewan)
    public Hewan getHewanById(int idHewan) {
        Hewan h = null;
        try {
            String sql = "SELECT * FROM hewan WHERE id_hewan = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idHewan);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                h = new Hewan(
                        rs.getInt("id_hewan"),
                        rs.getString("nama_hewan"),
                        rs.getString("jenis"),
                        rs.getInt("umur"),
                        rs.getInt("id_customer")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return h;
    }

    // 🔹 4. UPDATE HEWAN
    public void updateHewan(Hewan h) {
        try {
            String sql = "UPDATE hewan SET nama_hewan=?, jenis=?, umur=? WHERE id_hewan=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, h.getNamaHewan());
            ps.setString(2, h.getJenis());
            ps.setInt(3, h.getUmur());
            ps.setInt(4, h.getIdHewan());

            ps.executeUpdate();
            System.out.println("[HewanDAO] Hewan berhasil diupdate!");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[HewanDAO] Gagal update hewan: " + e.getMessage());
        }
    }

    // 🔹 5. DELETE HEWAN
    public void deleteHewan(int idHewan) {
        try {
            String sql = "DELETE FROM hewan WHERE id_hewan=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idHewan);
            ps.executeUpdate();
            System.out.println("[HewanDAO] Hewan berhasil dihapus!");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[HewanDAO] Gagal hapus hewan: " + e.getMessage());
        }
    }

    // 🔹 6. GET ALL HEWAN
    public ArrayList<Hewan> getAllHewan() {
        ArrayList<Hewan> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM hewan";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Hewan h = new Hewan(
                        rs.getInt("id_hewan"),
                        rs.getString("nama_hewan"),
                        rs.getString("jenis"),
                        rs.getInt("umur"),
                        rs.getInt("id_customer")
                );
                list.add(h);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
