package com.nwu.cafeteria.service;

import com.nwu.cafeteria.dao.MenuItemDAO;
import com.nwu.cafeteria.dao.ReviewDAO;
import com.nwu.cafeteria.model.MenuItem;
import com.nwu.cafeteria.model.Review;

import java.util.List;

public class ReviewService {
    private ReviewDAO reviewDAO;
    private MenuItemDAO menuItemDAO;
    
    public ReviewService() {
        this.reviewDAO = new ReviewDAO();
        this.menuItemDAO = new MenuItemDAO();
    }
    
    public boolean submitReview(int customerId, int itemId, int rating, String comment) {
        Review review = new Review(customerId, itemId, rating, comment);
        boolean success = reviewDAO.createReview(review);
        
        if (success) {
            updateMenuItemRating(itemId);
        }
        
        return success;
    }
    
    private void updateMenuItemRating(int itemId) {
        double averageRating = reviewDAO.getAverageRating(itemId);
        int reviewCount = reviewDAO.getReviewCount(itemId);
        
        menuItemDAO.updateRating(itemId, averageRating, reviewCount);
    }
    
    public List<Review> getReviewsForItem(int itemId) {
        return reviewDAO.getReviewsByItemId(itemId);
    }
    
    public List<Review> getReviewsByCustomer(int customerId) {
        return reviewDAO.getReviewsByCustomerId(customerId);
    }
    
    public List<Review> getAllReviews() {
        return reviewDAO.getAllReviews();
    }
    
    public List<MenuItem> getTopRatedItems(int limit) {
        return menuItemDAO.getTopRatedItems(limit);
    }
    
    public int getTotalReviewCount() {
        return reviewDAO.getTotalReviewCount();
    }
    
    public int getReviewsCountByCustomer(int customerId) {
        return reviewDAO.getReviewsCountByCustomer(customerId);
    }
}