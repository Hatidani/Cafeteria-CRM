# 🍽️ NWU Cafeteria Satisfaction Monitor

[![Java](https://img.shields.io/badge/Java-8+-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![Swing](https://img.shields.io/badge/GUI-Swing-green.svg)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![Status](https://img.shields.io/badge/Status-Development-brightgreen.svg)]()

## 📋 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Demo Accounts](#demo-accounts)
- [Screenshots](#sample-screenshots)

## Overview

The **NWU Cafeteria Satisfaction Monitor** is a Customer Relationship Management (CRM) system designed to enhance the dining experience at North-West University's Mahikeng Campus. The application creates a direct communication channel between customers and cafeteria staff, enabling:

- 📝 **Customer Feedback**: Submit ratings and reviews for menu items
- 📊 **Analytics Dashboard**: View customer behavior insights and sales reports
- 🏆 **Loyalty Program**: Track and manage customer loyalty points
- 👨‍🍳 **Menu Management**: Add, update, or remove menu items
- 🔒 **Secure Authentication**: Role-based access for customers and employees

## Features

### For Customers
- 🔐 **Secure Registration & Login**
- 📋 **Browse Menu Items**
- ⭐ **Submit Ratings & Reviews**
- 👤 **Manage Profile Information**
- 🏅 **Track Loyalty Points**
- 🛒 **View Order History**
- 🚫 **Save Allergies & Preferences**

### For Employees
- 📊 **Analytics Dashboard**
- 📈 **View Sales Reports**
- 💬 **Monitor Customer Feedback**
- 📝 **Manage Menu Items** (Add/Edit/Delete)
- 👥 **Manage Loyalty Program**
- 📅 **Track Popular Items & Trends**

## Technologies Used

### Frontend
- **Java Swing** - GUI Framework
- **Custom Purple Theme** - Consistent UI Design

### Backend
- **Java 8+** - Core Programming Language
- **JDBC** - Database Connectivity

### Database
- **MySQL** - Relational Database Management

### Tools
- **Git** - Version Control
- **GitHub** - Code Repository
- **VS Code / Eclipse / IntelliJ** - IDE Support

## Installation

### Prerequisites

Before running this application, ensure you have:

1. **Java Development Kit (JDK) 8 or higher**
   ```bash
   java -version

2. **MySQL Server 8.0 or higher**

   ```bash
   mysql -version

3. **MySQL Connector/J (Download from MySQL Connector/J Download)**

### Steps: 
1: Clone the Repository

2. Add MySQL Connector
Download mysql-connector-j-8.0.33.jar and place it in the lib/ folder

3. Using VS Code
Install the MySQL extension by cweijan
Connect to your MySQL server
Open scripts/database-setup.sql
Execute the SQL script

## Demo Accounts
Email: admin@nwu.ac.za
Password: admin123
Role: Administrator

Email: employee@nwu.ac.za
Password: employee123
Role: Cafeteria Employee

## Sample Screenshots
![Login page](https://github.com/Hatidani/Cafeteria-CRM/blob/64606cdc427594eec5ac079cb7d6f5e658fe7bdf/Screenshots/login.png)

![Registration](https://github.com/Hatidani/Cafeteria-CRM/blob/64606cdc427594eec5ac079cb7d6f5e658fe7bdf/Screenshots/Registration.png)
)

![Customer Home](https://github.com/Hatidani/Cafeteria-CRM/blob/64606cdc427594eec5ac079cb7d6f5e658fe7bdf/Screenshots/customerdashboard_home.png)

![Employee Home](https://github.com/Hatidani/Cafeteria-CRM/blob/64606cdc427594eec5ac079cb7d6f5e658fe7bdf/Screenshots/employeedashboard_home.png)
