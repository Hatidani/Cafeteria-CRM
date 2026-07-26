package com.nwu.cafeteria.service;

import com.nwu.cafeteria.dao.MenuItemDAO;
import com.nwu.cafeteria.model.MenuItem;

import java.util.List;

public class MenuService {
    private MenuItemDAO menuItemDAO;
    
    public MenuService() {
        this.menuItemDAO = new MenuItemDAO();
    }
    
    public List<MenuItem> getAvailableMenuItems() {
        return menuItemDAO.getAvailableMenuItems();
    }
    
    public List<MenuItem> getAllMenuItems() {
        return menuItemDAO.getAllMenuItems();
    }
    
    public List<MenuItem> getTopRatedItems(int limit) {
        return menuItemDAO.getTopRatedItems(limit);
    }
    
    public boolean addMenuItem(String name, String description, double price) {
        MenuItem item = new MenuItem(name, description, java.math.BigDecimal.valueOf(price));
        return menuItemDAO.createMenuItem(item);
    }
    
    public boolean updateMenuItem(int itemId, String name, String description, double price, boolean available) {
        MenuItem item = menuItemDAO.getMenuItemById(itemId);
        if (item != null) {
            item.setName(name);
            item.setDescription(description);
            item.setPrice(java.math.BigDecimal.valueOf(price));
            item.setAvailable(available);
            return menuItemDAO.updateMenuItem(item);
        }
        return false;
    }
    
    public boolean deleteMenuItem(int itemId) {
        return menuItemDAO.deleteMenuItem(itemId);
    }
    
    public int getMenuItemsCount() {
        return menuItemDAO.getMenuItemsCount();
    }
}