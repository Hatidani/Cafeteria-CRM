package com.nwu.cafeteria.service;

import com.nwu.cafeteria.dao.CustomerDAO;
import com.nwu.cafeteria.dao.OrderDAO;
import com.nwu.cafeteria.model.Customer;
import com.nwu.cafeteria.model.Order;

import java.math.BigDecimal;
import java.util.List;

public class OrderService {
    private OrderDAO orderDAO;
    private CustomerDAO customerDAO;
    
    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.customerDAO = new CustomerDAO();
    }
    
    public boolean createOrder(int customerId, List<Order.OrderItem> items) {
        BigDecimal totalAmount = calculateTotalAmount(items);
        Order order = new Order(customerId, totalAmount);
        order.getItems().addAll(items);
        
        return orderDAO.createOrder(order);
    }
    
    private BigDecimal calculateTotalAmount(List<Order.OrderItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (Order.OrderItem item : items) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }
    
    public List<Order> getCustomerOrders(int customerId) {
        return orderDAO.getOrdersByCustomerId(customerId);
    }
    
    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }
    
    public BigDecimal getTotalRevenue() {
        return orderDAO.getTotalRevenue();
    }
    
    public int getTotalOrders() {
        return orderDAO.getTotalOrders();
    }
    
    public int getOrdersCountByCustomer(int customerId) {
        return orderDAO.getOrdersCountByCustomer(customerId);
    }
    
    public Customer getCustomerById(int customerId) {
        return customerDAO.getCustomerById(customerId);
    }
    
    public int getTotalCustomers() {
        return customerDAO.getCustomerCount();
    }
}
