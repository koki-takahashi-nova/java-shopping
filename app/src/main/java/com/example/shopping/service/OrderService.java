package com.example.shopping.service;

import com.example.shopping.entity.Customer;
import com.example.shopping.entity.Order;
import com.example.shopping.entity.OrderDetail;
import com.example.shopping.model.Cart;
import com.example.shopping.repository.CustomerRepository;
import com.example.shopping.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Transactional
    public Order createOrder(Customer customer, Cart cart) {
        customer = customerRepository.save(customer);
        
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(cart.getTotal());
        order.setOrderDetails(new ArrayList<>());
        
        cart.getItems().forEach(cartItem -> {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(cartItem.getProduct());
            detail.setQuantity(cartItem.getQuantity());
            detail.setPrice(cartItem.getProduct().getPrice());
            order.getOrderDetails().add(detail);
        });
        
        return orderRepository.save(order);
    }
} 