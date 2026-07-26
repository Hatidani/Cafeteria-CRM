package com.nwu.cafeteria.dao;

import com.nwu.cafeteria.model.Order;
import com.nwu.cafeteria.util.DatabaseConfig;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    
    public boolean createOrder(Order order) {
        String sql = "INSERT INTO `Order` (customer_id, total_amount) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, order.getCustomerId());
            stmt.setBigDecimal(2, order.getTotalAmount());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int orderId = rs.getInt(1);
                    order.setOrderId(orderId);
                    
                    // Add order items
                    for (Order.OrderItem item : order.getItems()) {
                        addOrderItem(orderId, item);
                    }
                    
                    // Update loyalty points
                    updateLoyaltyPoints(order.getCustomerId(), order.getTotalAmount());
                    
                    return true;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating order: " + e.getMessage());
        }
        return false;
    }
    
    private void addOrderItem(int orderId, Order.OrderItem item) {
        String sql = "INSERT INTO Order_Detail (order_id, item_id, quantity, price_at_time) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // We need to get the item ID from the name (this is simplified)
            // In a real application, you'd have the actual item ID
            int itemId = getItemIdByName(item.getItemName());
            
            stmt.setInt(1, orderId);
            stmt.setInt(2, itemId);
            stmt.setInt(3, item.getQuantity());
            stmt.setBigDecimal(4, item.getPrice());
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error adding order item: " + e.getMessage());
        }
    }
    
    private int getItemIdByName(String itemName) {
        String sql = "SELECT item_id FROM Menu_Item WHERE name = ? LIMIT 1";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, itemName);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("item_id");
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting item ID by name: " + e.getMessage());
        }
        
        return 1; // Default fallback
    }
    
    private void updateLoyaltyPoints(int customerId, BigDecimal totalAmount) {
        String sql = "UPDATE Loyalty_Program SET points_balance = points_balance + ?, last_updated = NOW() WHERE customer_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // 1 point per R1 spent
            int pointsEarned = totalAmount.intValue();
            stmt.setInt(1, pointsEarned);
            stmt.setInt(2, customerId);
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error updating loyalty points: " + e.getMessage());
        }
    }
    
    public List<Order> getOrdersByCustomerId(int customerId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, c.name as customer_name FROM `Order` o " +
                    "JOIN Customer c ON o.customer_id = c.customer_id " +
                    "WHERE o.customer_id = ? ORDER BY o.order_date DESC";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setCustomerId(rs.getInt("customer_id"));
                order.setCustomerName(rs.getString("customer_name"));
                order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
                order.setTotalAmount(rs.getBigDecimal("total_amount"));
                
                // Load order items
                order.setItems(getOrderItems(order.getOrderId()));
                
                orders.add(order);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting orders by customer ID: " + e.getMessage());
        }
        
        return orders;
    }
    
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, c.name as customer_name FROM `Order` o " +
                    "JOIN Customer c ON o.customer_id = c.customer_id " +
                    "ORDER BY o.order_date DESC";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setCustomerId(rs.getInt("customer_id"));
                order.setCustomerName(rs.getString("customer_name"));
                order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
                order.setTotalAmount(rs.getBigDecimal("total_amount"));
                
                // Load order items
                order.setItems(getOrderItems(order.getOrderId()));
                
                orders.add(order);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all orders: " + e.getMessage());
        }
        
        return orders;
    }
    
    private List<Order.OrderItem> getOrderItems(int orderId) {
        List<Order.OrderItem> items = new ArrayList<>();
        String sql = "SELECT od.*, m.name as item_name FROM Order_Detail od " +
                    "JOIN Menu_Item m ON od.item_id = m.item_id " +
                    "WHERE od.order_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Order.OrderItem item = new Order.OrderItem(
                    rs.getString("item_name"),
                    rs.getInt("quantity"),
                    rs.getBigDecimal("price_at_time")
                );
                items.add(item);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting order items: " + e.getMessage());
        }
        
        return items;
    }
    
    public BigDecimal getTotalRevenue() {
        String sql = "SELECT SUM(total_amount) as total FROM `Order`";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("total");
                return total != null ? total : BigDecimal.ZERO;
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting total revenue: " + e.getMessage());
        }
        
        return BigDecimal.ZERO;
    }
    
    public int getTotalOrders() {
        String sql = "SELECT COUNT(*) as count FROM `Order`";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting total orders: " + e.getMessage());
        }
        
        return 0;
    }
    
    public int getOrdersCountByCustomer(int customerId) {
        String sql = "SELECT COUNT(*) as count FROM `Order` WHERE customer_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting orders count by customer: " + e.getMessage());
        }
        
        return 0;
    }
}