package com.ecommerce.admin.controller;

import com.ecommerce.admin.Service.SuccessExcelExporter;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.service.OrderService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class SuccessController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/orders/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=successful_orders_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Order> listOrders = orderService.findAllOrdersSuccessful();

        SuccessExcelExporter excelExporter = new SuccessExcelExporter(listOrders);

        excelExporter.export(response);
    }
}
