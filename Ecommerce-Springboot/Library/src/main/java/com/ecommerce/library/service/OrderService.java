package com.ecommerce.library.service;

import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.ShoppingCart;

import java.util.List;


public interface OrderService {
    Order save(ShoppingCart shoppingCart);

    List<Order> findAll(String username);

    List<Order> findALlOrders();

    List<Order> findAllOrdersSuccessful();

    List<Order> findAllOrdersFailed();

    Order acceptOrder(Long id);

    Order cancelOrder(Long id);
    Order successfulOrder(Long id);
    Order failedOrder(Long id);
}
