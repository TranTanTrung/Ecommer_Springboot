package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.OrderDetail;
import com.ecommerce.library.repository.OrderDetailRepository;
import com.ecommerce.library.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    @Autowired
    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailRepository.findAll();
    }

    @Override
    public List<OrderDetail> getOrderDetailsByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }

    @Override
    public List<OrderDetail> getOrderDetailsByOrderDetailId(Long orderDetailId) {
        // Assuming you have a method in your repository to find OrderDetail by ID
        Optional<OrderDetail> orderDetailOptional = orderDetailRepository.findById(orderDetailId);

        // Check if the OrderDetail is found
        if (orderDetailOptional.isPresent()) {
            List<OrderDetail> orderDetails = new ArrayList<>();
            orderDetails.add(orderDetailOptional.get());
            return orderDetails;
        } else {
            // Handle the case when OrderDetail is not found (return an empty list or throw an exception)
            return Collections.emptyList();
        }
    }
}
