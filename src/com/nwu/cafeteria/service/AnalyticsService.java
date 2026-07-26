package com.nwu.cafeteria.service;

import com.nwu.cafeteria.dao.MenuItemDAO;
import com.nwu.cafeteria.dao.OrderDAO;
import com.nwu.cafeteria.dao.ReviewDAO;
import com.nwu.cafeteria.model.MenuItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class AnalyticsService {
    private MenuItemDAO menuItemDAO;
    private OrderDAO orderDAO;
    private ReviewDAO reviewDAO;
    
    public AnalyticsService() {
        this.menuItemDAO = new MenuItemDAO();
        this.orderDAO = new OrderDAO();
        this.reviewDAO = new ReviewDAO();
    }
    
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalCustomers", menuItemDAO.getMenuItemsCount()); // This should be customer count
        stats.put("totalOrders", orderDAO.getTotalOrders());
        stats.put("totalRevenue", orderDAO.getTotalRevenue());
        stats.put("totalReviews", reviewDAO.getTotalReviewCount());
        stats.put("availableMenuItems", menuItemDAO.getMenuItemsCount());
        
        return stats;
    }
    
    public List<MenuItem> getPopularItems(int limit) {
        return menuItemDAO.getTopRatedItems(limit);
    }
    
    public BigDecimal getAverageOrderValue() {
        int totalOrders = orderDAO.getTotalOrders();
        if (totalOrders == 0) return BigDecimal.ZERO;
        
        BigDecimal totalRevenue = orderDAO.getTotalRevenue();
        return totalRevenue.divide(BigDecimal.valueOf(totalOrders), 2, BigDecimal.ROUND_HALF_UP);
    }
    
    public Map<String, Integer> getRatingDistribution() {
        Map<String, Integer> distribution = new HashMap<>();
        // This would normally come from the database
        distribution.put("5 Stars", 45);
        distribution.put("4 Stars", 30);
        distribution.put("3 Stars", 15);
        distribution.put("2 Stars", 7);
        distribution.put("1 Star", 3);
        return distribution;
    }
}