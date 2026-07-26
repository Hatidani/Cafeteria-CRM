package com.nwu.cafeteria.dao;

import com.nwu.cafeteria.model.Employee;
import com.nwu.cafeteria.util.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    
    public Employee getEmployeeByEmail(String email) {
        String sql = "SELECT * FROM Employee WHERE email = ?";
        Employee employee = null;
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                employee = new Employee();
                employee.setEmployeeId(rs.getInt("employee_id"));
                employee.setName(rs.getString("name"));
                employee.setEmail(rs.getString("email"));
                employee.setPasswordHash(rs.getString("password_hash"));
                employee.setRole(rs.getString("role"));
                employee.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting employee by email: " + e.getMessage());
        }
        
        return employee;
    }
    
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employee ORDER BY name";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeId(rs.getInt("employee_id"));
                employee.setName(rs.getString("name"));
                employee.setEmail(rs.getString("email"));
                employee.setPasswordHash(rs.getString("password_hash"));
                employee.setRole(rs.getString("role"));
                employee.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                
                employees.add(employee);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all employees: " + e.getMessage());
        }
        
        return employees;
    }
    
    public boolean createEmployee(Employee employee) {
        String sql = "INSERT INTO Employee (name, email, password_hash, role) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getEmail());
            stmt.setString(3, employee.getPasswordHash());
            stmt.setString(4, employee.getRole());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating employee: " + e.getMessage());
            return false;
        }
    }
}
