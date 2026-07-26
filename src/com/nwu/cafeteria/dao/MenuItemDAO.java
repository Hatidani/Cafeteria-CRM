package com.nwu.cafeteria.dao;

import com.nwu.cafeteria.model.MenuItem;
import com.nwu.cafeteria.util.DatabaseConfig;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDAO {
    
    public boolean createMenuItem(MenuItem menuItem) {
        String sql = "INSERT INTO Menu_Item (name, description, price) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, menuItem.getName());
            stmt.setString(2, menuItem.getDescription());
            stmt.setBigDecimal(3, menuItem.getPrice());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    menuItem.setItemId(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating menu item: " + e.getMessage());
        }
        return false;
    }
    
    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        String sql = "SELECT * FROM Menu_Item ORDER BY name";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                MenuItem item = new MenuItem();
                item.setItemId(rs.getInt("item_id"));
                item.setName(rs.getString("name"));
                item.setDescription(rs.getString("description"));
                item.setPrice(rs.getBigDecimal("price"));
                item.setAvailable(rs.getBoolean("is_available"));
                item.setAverageRating(rs.getBigDecimal("average_rating"));
                item.setReviewCount(rs.getInt("review_count"));
                item.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                
                menuItems.add(item);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all menu items: " + e.getMessage());
        }
        
        return menuItems;
    }
    
    public List<MenuItem> getAvailableMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        String sql = "SELECT * FROM Menu_Item WHERE is_available = TRUE ORDER BY name";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                MenuItem item = new MenuItem();
                item.setItemId(rs.getInt("item_id"));
                item.setName(rs.getString("name"));
                item.setDescription(rs.getString("description"));
                item.setPrice(rs.getBigDecimal("price"));
                item.setAvailable(rs.getBoolean("is_available"));
                item.setAverageRating(rs.getBigDecimal("average_rating"));
                item.setReviewCount(rs.getInt("review_count"));
                item.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                
                menuItems.add(item);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting available menu items: " + e.getMessage());
        }
        
        return menuItems;
    }
    
    public List<MenuItem> getTopRatedItems(int limit) {
        List<MenuItem> menuItems = new ArrayList<>();
        String sql = "SELECT * FROM Menu_Item WHERE is_available = TRUE AND review_count > 0 " +
                    "ORDER BY average_rating DESC LIMIT ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                MenuItem item = new MenuItem();
                item.setItemId(rs.getInt("item_id"));
                item.setName(rs.getString("name"));
                item.setDescription(rs.getString("description"));
                item.setPrice(rs.getBigDecimal("price"));
                item.setAvailable(rs.getBoolean("is_available"));
                item.setAverageRating(rs.getBigDecimal("average_rating"));
                item.setReviewCount(rs.getInt("review_count"));
                item.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                
                menuItems.add(item);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting top rated items: " + e.getMessage());
        }
        
        return menuItems;
    }
    
    public boolean updateMenuItem(MenuItem menuItem) {
        String sql = "UPDATE Menu_Item SET name = ?, description = ?, price = ?, is_available = ? WHERE item_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, menuItem.getName());
            stmt.setString(2, menuItem.getDescription());
            stmt.setBigDecimal(3, menuItem.getPrice());
            stmt.setBoolean(4, menuItem.isAvailable());
            stmt.setInt(5, menuItem.getItemId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating menu item: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateRating(int itemId, double averageRating, int reviewCount) {
        String sql = "UPDATE Menu_Item SET average_rating = ?, review_count = ? WHERE item_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBigDecimal(1, BigDecimal.valueOf(averageRating));
            stmt.setInt(2, reviewCount);
            stmt.setInt(3, itemId);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating rating: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteMenuItem(int itemId) {
        String sql = "UPDATE Menu_Item SET is_available = FALSE WHERE item_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, itemId);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting menu item: " + e.getMessage());
            return false;
        }
    }
    
    public MenuItem getMenuItemById(int itemId) {
        String sql = "SELECT * FROM Menu_Item WHERE item_id = ?";
        MenuItem item = null;
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, itemId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                item = new MenuItem();
                item.setItemId(rs.getInt("item_id"));
                item.setName(rs.getString("name"));
                item.setDescription(rs.getString("description"));
                item.setPrice(rs.getBigDecimal("price"));
                item.setAvailable(rs.getBoolean("is_available"));
                item.setAverageRating(rs.getBigDecimal("average_rating"));
                item.setReviewCount(rs.getInt("review_count"));
                item.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting menu item by ID: " + e.getMessage());
        }
        
        return item;
    }
    
    public int getMenuItemsCount() {
        String sql = "SELECT COUNT(*) as count FROM Menu_Item WHERE is_available = TRUE";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting menu items count: " + e.getMessage());
        }
        
        return 0;
    }
}
