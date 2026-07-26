package com.nwu.cafeteria.dao;

import com.nwu.cafeteria.model.Review;
import com.nwu.cafeteria.util.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {
    
    public boolean createReview(Review review) {
        String sql = "INSERT INTO Review (customer_id, item_id, rating, comment) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, review.getCustomerId());
            stmt.setInt(2, review.getItemId());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getComment());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    review.setReviewId(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating review: " + e.getMessage());
        }
        return false;
    }
    
    public List<Review> getReviewsByItemId(int itemId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.*, c.name as customer_name, m.name as item_name " +
                    "FROM Review r " +
                    "JOIN Customer c ON r.customer_id = c.customer_id " +
                    "JOIN Menu_Item m ON r.item_id = m.item_id " +
                    "WHERE r.item_id = ? ORDER BY r.created_at DESC";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, itemId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Review review = new Review();
                review.setReviewId(rs.getInt("review_id"));
                review.setCustomerId(rs.getInt("customer_id"));
                review.setItemId(rs.getInt("item_id"));
                review.setCustomerName(rs.getString("customer_name"));
                review.setItemName(rs.getString("item_name"));
                review.setRating(rs.getInt("rating"));
                review.setComment(rs.getString("comment"));
                review.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                
                reviews.add(review);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting reviews by item ID: " + e.getMessage());
        }
        
        return reviews;
    }
    
    public List<Review> getReviewsByCustomerId(int customerId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.*, c.name as customer_name, m.name as item_name " +
                    "FROM Review r " +
                    "JOIN Customer c ON r.customer_id = c.customer_id " +
                    "JOIN Menu_Item m ON r.item_id = m.item_id " +
                    "WHERE r.customer_id = ? ORDER BY r.created_at DESC";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Review review = new Review();
                review.setReviewId(rs.getInt("review_id"));
                review.setCustomerId(rs.getInt("customer_id"));
                review.setItemId(rs.getInt("item_id"));
                review.setCustomerName(rs.getString("customer_name"));
                review.setItemName(rs.getString("item_name"));
                review.setRating(rs.getInt("rating"));
                review.setComment(rs.getString("comment"));
                review.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                
                reviews.add(review);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting reviews by customer ID: " + e.getMessage());
        }
        
        return reviews;
    }
    
    public List<Review> getAllReviews() {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.*, c.name as customer_name, m.name as item_name " +
                    "FROM Review r " +
                    "JOIN Customer c ON r.customer_id = c.customer_id " +
                    "JOIN Menu_Item m ON r.item_id = m.item_id " +
                    "ORDER BY r.created_at DESC";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Review review = new Review();
                review.setReviewId(rs.getInt("review_id"));
                review.setCustomerId(rs.getInt("customer_id"));
                review.setItemId(rs.getInt("item_id"));
                review.setCustomerName(rs.getString("customer_name"));
                review.setItemName(rs.getString("item_name"));
                review.setRating(rs.getInt("rating"));
                review.setComment(rs.getString("comment"));
                review.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                
                reviews.add(review);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all reviews: " + e.getMessage());
        }
        
        return reviews;
    }
    
    public double getAverageRating(int itemId) {
        String sql = "SELECT AVG(rating) as avg_rating FROM Review WHERE item_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, itemId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("avg_rating");
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting average rating: " + e.getMessage());
        }
        
        return 0.0;
    }
    
    public int getReviewCount(int itemId) {
        String sql = "SELECT COUNT(*) as count FROM Review WHERE item_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, itemId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting review count: " + e.getMessage());
        }
        
        return 0;
    }
    
    public int getTotalReviewCount() {
        String sql = "SELECT COUNT(*) as count FROM Review";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting total review count: " + e.getMessage());
        }
        
        return 0;
    }
    
    public int getReviewsCountByCustomer(int customerId) {
        String sql = "SELECT COUNT(*) as count FROM Review WHERE customer_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting reviews count by customer: " + e.getMessage());
        }
        
        return 0;
    }
}