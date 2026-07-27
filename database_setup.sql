-- Create database
CREATE DATABASE IF NOT EXISTS nwu_cafeteria;
USE nwu_cafeteria;

-- Customer table
CREATE TABLE Customer (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    allergies TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Employee table
CREATE TABLE Employee (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Menu Item table
CREATE TABLE Menu_Item (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Order table
CREATE TABLE `Order` (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2),
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id)
);

-- Order Detail table
CREATE TABLE Order_Detail (
    order_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    item_id INT,
    quantity INT NOT NULL,
    price_at_time DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES `Order`(order_id),
    FOREIGN KEY (item_id) REFERENCES Menu_Item(item_id)
);

-- Review table
CREATE TABLE Review (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    item_id INT,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id),
    FOREIGN KEY (item_id) REFERENCES Menu_Item(item_id)
);

-- Loyalty Program table
CREATE TABLE Loyalty_Program (
    loyalty_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT UNIQUE,
    points_balance INT DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id)
);

-- Insert sample data
INSERT INTO Menu_Item (name, description, price) VALUES
('Cheese Burger', 'Juicy beef patty with cheese and fresh vegetables', 45.00),
('Chicken Wrap', 'Grilled chicken with fresh veggies in a tortilla', 35.00),
('Greek Salad', 'Fresh salad with feta cheese and olives', 40.00),
('Cappuccino', 'Freshly brewed coffee with steamed milk', 25.00),
('Chocolate Muffin', 'Freshly baked chocolate muffin', 15.00);

INSERT INTO Employee (name, email, password_hash, role) VALUES
('Admin User', 'admin@nwu.ac.za', 'admin123', 'admin');

-- Create dedicated user for the application (optional but recommended)
CREATE USER IF NOT EXISTS 'cafeteria_user'@'localhost' IDENTIFIED BY 'cafeteria123';
GRANT ALL PRIVILEGES ON nwu_cafeteria.* TO 'cafeteria_user'@'localhost';
FLUSH PRIVILEGES;