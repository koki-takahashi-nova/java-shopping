package com.example.shopping.controller;

import com.example.shopping.entity.Customer;
import com.example.shopping.entity.Product;
import com.example.shopping.model.Cart;
import java.util.List;
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

    // クイックフィルター用の価格帯定数
    private static final double LOW_PRICE_MAX = 10000.0;
    private static final double MID_PRICE_MIN = 10001.0;
    private static final double MID_PRICE_MAX = 50000.0;
    private static final double HIGH_PRICE_MIN = 50001.0;    
    
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "index";
    }

    @GetMapping("/search")
    public String searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Model model) {
        List<Product> products = productService.searchProducts(keyword, minPrice, maxPrice);
        model.addAttribute("products", products);
        model.addAttribute("resultCount", products.size());
        model.addAttribute("keyword", keyword);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        return "index";
    }

    @GetMapping("/category/{category}")
    public String productsByCategory(@PathVariable String category, Model model) {
        model.addAttribute("products", productService.getProductsByCategory(category));
        model.addAttribute("category", category);
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

    // 価格帯クイックフィルター: 10000円以下
    @GetMapping("/filter/low")
    public String filterLowPrice(Model model) {
        List<Product> products = productService.searchProducts(null, 0.0, LOW_PRICE_MAX);
        model.addAttribute("products", products);
        model.addAttribute("resultCount", products.size());
        model.addAttribute("maxPrice", LOW_PRICE_MAX);
        return "index"; 
    }

    // 価格帯クイックフィルター: 10001円〜50000円
    @GetMapping("/filter/mid")
    public String filterMidPrice(Model model) {
        List<Product> products = productService.searchProducts(null, MID_PRICE_MIN, MID_PRICE_MAX);
        model.addAttribute("products", products);
        model.addAttribute("resultCount", products.size());
        model.addAttribute("minPrice", MID_PRICE_MIN);
        model.addAttribute("maxPrice", MID_PRICE_MAX);
        return "index"; 
    }

    // 価格帯クイックフィルター: 50001円以上
    @GetMapping("/filter/high")
    public String filterHighPrice(Model model) {
        List<Product> products = productService.searchProducts(null, HIGH_PRICE_MIN, null);
        model.addAttribute("products", products);
        model.addAttribute("resultCount", products.size());
        model.addAttribute("minPrice", HIGH_PRICE_MIN);
        return "index"; 
    }
} 