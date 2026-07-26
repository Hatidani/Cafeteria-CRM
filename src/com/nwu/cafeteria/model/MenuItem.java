package com.nwu.cafeteria.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MenuItem {
    private int itemId;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean isAvailable;
    private BigDecimal averageRating;
    private int reviewCount;
    private LocalDateTime createdAt;
    
    public MenuItem() {}
    
    public MenuItem(String name, String description, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.isAvailable = true;
        this.averageRating = BigDecimal.ZERO;
        this.reviewCount = 0;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    
    public BigDecimal getAverageRating() { return averageRating; }
    public void setAverageRating(BigDecimal averageRating) { this.averageRating = averageRating; }
    
    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public String getFormattedPrice() {
        return "R" + price.setScale(2);
    }
    
    public String getRatingStars() {
        if (reviewCount == 0) return "No ratings";
        int fullStars = averageRating.intValue();
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < fullStars; i++) {
            stars.append("★");
        }
        for (int i = fullStars; i < 5; i++) {
            stars.append("☆");
        }
        return stars.toString() + " (" + averageRating + ")";
    }
    
    @Override
    public String toString() {
        return name + " - " + getFormattedPrice() + " - " + getRatingStars();
    }
}