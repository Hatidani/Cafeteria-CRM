package com.nwu.cafeteria.model;

import java.time.LocalDateTime;

public class Customer {
    private int customerId;
    private String name;
    private String email;
    private String passwordHash;
    private String allergies;
    private LocalDateTime createdAt;
    private int loyaltyPoints;
    
    public Customer() {}
    
    public Customer(String name, String email, String passwordHash, String allergies) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.allergies = allergies;
        this.createdAt = LocalDateTime.now();
        this.loyaltyPoints = 0;
    }
    
    // Getters and Setters
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    
    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public int getLoyaltyPoints() { return loyaltyPoints; }
    public void setLoyaltyPoints(int loyaltyPoints) { this.loyaltyPoints = loyaltyPoints; }
    
    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", allergies='" + allergies + '\'' +
                ", loyaltyPoints=" + loyaltyPoints +
                '}';
    }
}