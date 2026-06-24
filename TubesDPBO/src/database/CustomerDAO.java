package database;

import java.sql.*;
import java.util.ArrayList;
import model.Customer;

public class CustomerDAO {

    // 🔹 READ ALL
    public ArrayList<Customer> getAllCustomer() {
        ArrayList<Customer> list = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getConnection();
            
            String sql = "SELECT c.id_customer, c.alamat, c.no_hp, u.username, u.password, u.nama_lengkap " +
                         "FROM customer c JOIN users u ON c.id_user = u.id_user";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Customer c = new Customer(
                        rs.getString("no_hp"),
                        rs.getString("alamat"),
                        rs.getInt("id_customer"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("nama_lengkap")
                );
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 READ BY ID
    public Customer getCustomerById(int id) {
        Customer c = null;
        try {
            Connection conn = DatabaseConnection.getConnection();
            
            String sql = "SELECT c.id_customer, c.alamat, c.no_hp, u.username, u.password, u.nama_lengkap " +
                         "FROM customer c JOIN users u ON c.id_user = u.id_user WHERE c.id_customer = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                c = new Customer(
                        rs.getString("no_hp"),
                        rs.getString("alamat"),
                        rs.getInt("id_customer"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("nama_lengkap")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    // 🔹 INSERT (DIPERBAIKI: Insert ke 2 tabel)
    public int insertCustomer(Customer c) {
        int generatedIdCustomer = -1;
        try {
            Connection conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Mulai transaksi

            // 1. INSERT ke tabel USERS dulu
            String sqlUser = "INSERT INTO users (username, password, nama_lengkap, role) VALUES (?, ?, ?, 'CUSTOMER')";
            PreparedStatement psUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS);
            psUser.setString(1, c.getUsername());
            psUser.setString(2, c.getPassword());
            psUser.setString(3, c.getNamaLengkap());
            psUser.executeUpdate();

            // Ambil id_user yang baru dibuat
            ResultSet rsUser = psUser.getGeneratedKeys();
            int idUserBaru = -1;
            if (rsUser.next()) {
                idUserBaru = rsUser.getInt(1);
            }

            // 2. INSERT ke tabel CUSTOMER
            String sqlCust = "INSERT INTO customer (id_user, alamat, no_hp) VALUES (?, ?, ?)";
            PreparedStatement psCust = conn.prepareStatement(sqlCust, Statement.RETURN_GENERATED_KEYS);
            psCust.setInt(1, idUserBaru);
            psCust.setString(2, c.getAlamat());
            psCust.setString(3, c.getNoHp());
            psCust.executeUpdate();

            ResultSet rsCust = psCust.getGeneratedKeys();
            if (rsCust.next()) {
                generatedIdCustomer = rsCust.getInt(1);
            }

            conn.commit(); // Simpan semua
            System.out.println("Register berhasil! ID Customer: " + generatedIdCustomer);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Gagal register: " + e.getMessage());
        }
        return generatedIdCustomer;
    }

    // 🔹 UPDATE
    public void updateCustomer(Customer c) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            
            String sql = "UPDATE customer SET alamat=?, no_hp=? WHERE id_customer=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, c.getAlamat());
            ps.setString(2, c.getNoHp());
            ps.setInt(3, c.getIdCustomer());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔹 DELETE
    public void deleteCustomer(int id) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            
            String sql = "DELETE FROM customer WHERE id_customer=?";
            PreparedStatement ps 
                    
                    = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}