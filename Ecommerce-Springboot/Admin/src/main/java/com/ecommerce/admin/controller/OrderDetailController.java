package com.ecommerce.admin.controller;

import com.ecommerce.library.model.OrderDetail;
import com.ecommerce.library.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    @Autowired
    public OrderDetailController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    @GetMapping("/orderdetail")
    public String getAlldetail(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            List<OrderDetail> orderDetails = orderDetailService.getAllOrderDetails();
            model.addAttribute("orderDetails", orderDetails);
            return "orderdetail";
        }
    }
}
