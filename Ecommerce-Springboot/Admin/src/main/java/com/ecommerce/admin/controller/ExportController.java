package com.ecommerce.admin.controller;

import com.ecommerce.admin.Service.WordExportService;
import com.ecommerce.library.model.OrderDetail;
import com.ecommerce.library.service.OrderDetailService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/export")
public class ExportController {

    @Autowired
    private WordExportService wordExportService;

    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping("/toWord")
    public void exportToWord(@RequestParam Long orderDetailId, HttpServletResponse response) {
        // Retrieve order details by orderDetailId
        List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderDetailId(orderDetailId);
        if (orderDetails.isEmpty()) {
            // Handle the case when orderDetails is empty (e.g., show an error message)
            return;
        }

        // Use the WordExportService to generate the Word document
        byte[] documentBytes = wordExportService.exportToWord(orderDetailId, orderDetails);

        // Set response headers for file download
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=order-details-" + orderDetailId + ".docx");

        try {
            // Write the document bytes to the response stream
            response.getOutputStream().write(documentBytes);
            response.getOutputStream().flush();
        } catch (IOException e) {
            // Handle IOException (e.g., log the exception)
            e.printStackTrace();
        }
    }
}
