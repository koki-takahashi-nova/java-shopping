package com.example.shopping.service;

import com.example.shopping.entity.Product;
import com.example.shopping.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }
    
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> searchProducts(String keyword, Double minPrice, Double maxPrice) {
        if (minPrice != null && minPrice < 0) {
            throw new IllegalArgumentException("minPrice must be >= 0");
        }
        if (maxPrice != null && maxPrice < 0) {
            throw new IllegalArgumentException("maxPrice must be >= 0");
        }
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new IllegalArgumentException("minPrice must be <= maxPrice");
        }

        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasMin = minPrice != null;
        boolean hasMax = maxPrice != null;
        String trimmedKeyword = hasKeyword ? keyword.trim() : null;

        if (hasKeyword && hasMin && hasMax) {
            return productRepository.findByNameContainingIgnoreCaseAndPriceBetween(trimmedKeyword, minPrice, maxPrice);
        }
        if (hasKeyword && hasMin) {
            return productRepository.findByNameContainingIgnoreCaseAndPriceGreaterThanEqual(trimmedKeyword, minPrice);
        }
        if (hasKeyword && hasMax) {
            return productRepository.findByNameContainingIgnoreCaseAndPriceLessThanEqual(trimmedKeyword, maxPrice);
        }
        if (hasKeyword) {
            return productRepository.findByNameContainingIgnoreCase(trimmedKeyword);
        }
        if (hasMin && hasMax) {
            return productRepository.findByPriceBetween(minPrice, maxPrice);
        }
        if (hasMin) {
            return productRepository.findByPriceGreaterThanEqual(minPrice);
        }
        if (hasMax) {
            return productRepository.findByPriceLessThanEqual(maxPrice);
        }
        return productRepository.findAll();
    }
} 