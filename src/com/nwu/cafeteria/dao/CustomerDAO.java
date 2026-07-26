package com.nwu.cafeteria.dao;

import com.nwu.cafeteria.model.Customer;
import com.nwu.cafeteria.util.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    
    public boolean createCustomer(Customer customer) {
        String sql = "INSERT INTO Customer (name, email, password_hash, allergies) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getEmail());
            stmt.setString(3, customer.getPasswordHash());
            stmt.setString(4, customer.getAllergies());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Get generated customer ID
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    customer.setCustomerId(rs.getInt(1));
                }
                
                // Initialize loyalty program
                initializeLoyaltyProgram(customer.getCustomerId());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating customer: " + e.getMessage());
        }
        return false;
    }
    
    private void initializeLoyaltyProgram(int customerId) {
        String sql = "INSERT INTO Loyalty_Program (customer_id, points_balance) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, customerId);
            stmt.setInt(2, 0);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error initializing loyalty program: " + e.getMessage());
        }
    }
    
    public Customer getCustomerByEmail(String email) {
        String sql = "SELECT c.*, lp.points_balance FROM Customer c " +
                    "LEFT JOIN Loyalty_Program lp ON c.customer_id = lp.customer_id " +
                    "WHERE c.email = ?";
        Customer customer = null;
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setName(rs.getString("name"));
                customer.setEmail(rs.getString("email"));
                customer.setPasswordHash(rs.getString("password_hash"));
                customer.setAllergies(rs.getString("allergies"));
                customer.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                customer.setLoyaltyPoints(rs.getInt("points_balance"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting customer by email: " + e.getMessage());
        }
        
        return customer;
    }
    
    public Customer getCustomerById(int customerId) {
        String sql = "SELECT c.*, lp.points_balance FROM Customer c " +
                    "LEFT JOIN Loyalty_Program lp ON c.customer_id = lp.customer_id " +
                    "WHERE c.customer_id = ?";
        Customer customer = null;
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setName(rs.getString("name"));
                customer.setEmail(rs.getString("email"));
                customer.setPasswordHash(rs.getString("password_hash"));
                customer.setAllergies(rs.getString("allergies"));
                customer.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                customer.setLoyaltyPoints(rs.getInt("points_balance"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting customer by ID: " + e.getMessage());
        }
        
        return customer;
    }
    
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT c.*, lp.points_balance FROM Customer c " +
                    "LEFT JOIN Loyalty_Program lp ON c.customer_id = lp.customer_id " +
                    "ORDER BY c.name";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setName(rs.getString("name"));
                customer.setEmail(rs.getString("email"));
                customer.setPasswordHash(rs.getString("password_hash"));
                customer.setAllergies(rs.getString("allergies"));
                customer.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                customer.setLoyaltyPoints(rs.getInt("points_balance"));
                
                customers.add(customer);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all customers: " + e.getMessage());
        }
        
        return customers;
    }
    
    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE Customer SET name = ?, allergies = ? WHERE customer_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getAllergies());
            stmt.setInt(3, customer.getCustomerId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating customer: " + e.getMessage());
            return false;
        }
    }
    
    public int getCustomerCount() {
        String sql = "SELECT COUNT(*) as count FROM Customer";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting customer count: " + e.getMessage());
        }
        
        return 0;
    }
}
