package com.nwu.cafeteria.service;

import com.nwu.cafeteria.dao.CustomerDAO;
import com.nwu.cafeteria.dao.EmployeeDAO;
import com.nwu.cafeteria.model.Customer;
import com.nwu.cafeteria.model.Employee;
import com.nwu.cafeteria.util.PasswordHasher;

public class AuthenticationService {
    private CustomerDAO customerDAO;
    private EmployeeDAO employeeDAO;
    
    public AuthenticationService() {
        this.customerDAO = new CustomerDAO();
        this.employeeDAO = new EmployeeDAO();
    }
    
    public Customer loginCustomer(String email, String password) {
        Customer customer = customerDAO.getCustomerByEmail(email);
        if (customer != null && PasswordHasher.verifyPassword(password, customer.getPasswordHash())) {
            return customer;
        }
        return null;
    }
    
    public Employee loginEmployee(String email, String password) {
        Employee employee = employeeDAO.getEmployeeByEmail(email);
        if (employee != null && PasswordHasher.verifyPassword(password, employee.getPasswordHash())) {
            return employee;
        }
        return null;
    }
    
    public boolean registerCustomer(String name, String email, String password, String allergies) {
        // Check if email already exists
        if (customerDAO.getCustomerByEmail(email) != null) {
            return false;
        }
        
        String hashedPassword = PasswordHasher.hashPassword(password);
        Customer customer = new Customer(name, email, hashedPassword, allergies);
        return customerDAO.createCustomer(customer);
    }
    
    public boolean registerEmployee(String name, String email, String password, String role) {
        // Check if email already exists
        if (employeeDAO.getEmployeeByEmail(email) != null) {
            return false;
        }
        
        String hashedPassword = PasswordHasher.hashPassword(password);
        Employee employee = new Employee(name, email, hashedPassword, role);
        return employeeDAO.createEmployee(employee);
    }
}
