package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.*;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.repository.OrderDetailRepository;
import com.ecommerce.library.repository.OrderRepository;
import com.ecommerce.library.service.OrderService;
import com.ecommerce.library.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository detailRepository;
    private final CustomerRepository customerRepository;
    private final ShoppingCartService cartService;

    @Override
    @Transactional
    public Order save(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setCustomer(shoppingCart.getCustomer());
        order.setTax(2);
        order.setTotalPrice(shoppingCart.getTotalPrice());
        order.setAccept(false);
        order.setPaymentMethod("Cash");
        order.setOrderStatus("Pending");
        order.setQuantity(shoppingCart.getTotalItems());
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (CartItem item : shoppingCart.getCartItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(item.getProduct());
            detailRepository.save(orderDetail);
            orderDetailList.add(orderDetail);
        }
        order.setOrderDetailList(orderDetailList);
        cartService.deleteCartById(shoppingCart.getId());
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findAll(String username) {
        Customer customer = customerRepository.findByUsername(username);
        List<Order> orders = customer.getOrders();
        return orders;
    }

    @Override
    public List<Order> findALlOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> findAllOrdersSuccessful() {
        List<Order> allOrders = orderRepository.findAll(); // Thay đổi repository này nếu tên thực tế khác

        List<Order> successfulOrders = new ArrayList<>();

        for (Order order : allOrders) {
            // Giả sử bạn có một trường "success" trong đối tượng Order để xác định đơn hàng successful
            if (order.isSuccess()) {
                successfulOrders.add(order);
            }
        }

        return successfulOrders;
    }

    @Override
    public List<Order> findAllOrdersFailed() {
        List<Order> allOrders = orderRepository.findAll(); // Thay đổi repository này nếu tên thực tế khác

        List<Order> successfulOrders = new ArrayList<>();

        for (Order order : allOrders) {
            // Giả sử bạn có một trường "success" trong đối tượng Order để xác định đơn hàng successful
            if (!order.isSuccess()) {
                successfulOrders.add(order);
            }
        }
        return successfulOrders;
    }


    @Override
    public Order acceptOrder(Long id) {
        Order order = orderRepository.getById(id);
        order.setAccept(true);
        order.setDeliveryDate(new Date());
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long id) {
        Order order = orderRepository.getById(id);
        order.setAccept(false);
        order.setDeliveryDate(new Date());
        order.setOrderStatus("Pending");
        return orderRepository.save(order);
    }

    @Override
    public Order successfulOrder(Long id) {
        Order order = orderRepository.getById(id);
        order.setAccept(true);
        order.setDeliveryDate(new Date());
        order.setSuccess(true);
        order.setOrderStatus("Successful Order");
        return orderRepository.save(order);
    }

    @Override
    public Order failedOrder(Long id) {
        Order order = orderRepository.getById(id);
        order.setAccept(true);
        order.setDeliveryDate(new Date());
        order.setSuccess(false);
        order.setOrderStatus("Order Failed");
        return orderRepository.save(order);
    }
}
