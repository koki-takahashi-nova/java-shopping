package com.example.shopping.model;

import com.example.shopping.entity.Product;
import lombok.Data;

@Data
public class CartItem {
    private Product product;
    private int quantity;
    
    public double getSubtotal() {
        return product.getPrice() * quantity;
    }
}