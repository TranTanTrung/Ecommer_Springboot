package com.ecommerce.admin.controller;

import com.ecommerce.admin.Service.WordExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/export")
public class ExportController {

    @Autowired
    private WordExportService wordExportService;

    @GetMapping("/toWord")
    @ResponseBody
    public ResponseEntity<byte[]> exportToWord(@RequestParam Long orderId) {
        // Retrieve order information by orderId and format the data as needed
        String data = "Order ID: " + orderId + "\n"; // Customize this line based on your order data

        // Use the WordExportService to generate the Word document
        byte[] documentBytes = wordExportService.exportToWord(data);

        // Set the response headers for file download
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "failed-order-" + orderId + ".docx");

        // Return the byte array and headers in a ResponseEntity
        return new ResponseEntity<>(documentBytes, headers, HttpStatus.OK);
    }
}
