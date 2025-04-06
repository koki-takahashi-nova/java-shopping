package com.example.shopping.controller;

import com.example.shopping.entity.Customer;
import com.example.shopping.entity.Product;
import com.example.shopping.model.Cart;
import com.example.shopping.service.OrderService;
import com.example.shopping.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ShoppingController {
    @Autowired
    private ProductService productService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private Cart cart;
    
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "index";
    }
    
    @GetMapping("/category/{category}")
    public String productsByCategory(@PathVariable String category, Model model) {
        model.addAttribute("products", productService.getProductsByCategory(category));
        return "index";
    }
    
    @PostMapping("/cart/add/{id}")
    public String addToCart(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            cart.addProduct(product);
        }
        return "redirect:/cart";
    }
    
    @GetMapping("/cart")
    public String viewCart(Model model) {
        model.addAttribute("cart", cart);
        return "cart";
    }
    
    @PostMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable Long id) {
        cart.removeProduct(id);
        return "redirect:/cart";
    }
    
    @GetMapping("/checkout")
    public String checkout(Model model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("cart", cart);
        return "checkout";
    }
    
    @PostMapping("/order")
    public String placeOrder(@ModelAttribute Customer customer, Model model) {
        orderService.createOrder(customer, cart);
        cart.clear();
        return "redirect:/order/confirmation";
    }
    
    @GetMapping("/order/confirmation")
    public String orderConfirmation() {
        return "orderConfirmation";
    }
} 