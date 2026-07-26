package com.nwu.cafeteria.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderId;
    private int customerId;
    private String customerName;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private List<OrderItem> items;
    
    public Order() {
        this.items = new ArrayList<>();
    }
    
    public Order(int customerId, BigDecimal totalAmount) {
        this();
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.orderDate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public void addItem(OrderItem item) { this.items.add(item); }
    
    public String getFormattedTotal() {
        return "R" + totalAmount.setScale(2);
    }
    
    public static class OrderItem {
        private String itemName;
        private int quantity;
        private BigDecimal price;
        
        public OrderItem(String itemName, int quantity, BigDecimal price) {
            this.itemName = itemName;
            this.quantity = quantity;
            this.price = price;
        }
        
        // Getters and Setters
        public String getItemName() { return itemName; }
        public void setItemName(String itemName) { this.itemName = itemName; }
        
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
        
        public BigDecimal getSubtotal() {
            return price.multiply(BigDecimal.valueOf(quantity));
        }
        
        public String getFormattedSubtotal() {
            return "R" + getSubtotal().setScale(2);
        }
    }
}