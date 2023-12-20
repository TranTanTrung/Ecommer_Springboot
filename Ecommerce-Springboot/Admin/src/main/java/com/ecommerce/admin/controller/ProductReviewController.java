package com.ecommerce.library.controller;

import com.ecommerce.library.model.ProductReview;
import com.ecommerce.library.service.ProductReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductReviewController {

    @Autowired
    private ProductReviewService productReviewService;

    @GetMapping("/productreview")
    public String getAllProductReviews(Model model) {
        List<ProductReview> productReviews = productReviewService.getAllProductReviews();
        model.addAttribute("productReview", productReviews);
        model.addAttribute("size", productReviews.size()); // for your size condition in Thymeleaf
        return "productreview"; // replace with your actual template name
    }

    @PostMapping("/delete-product-review")
    public String deleteProductReview(@RequestParam Long id) {
        productReviewService.deleteProductReview(id);
        return "redirect:/productreview";
    }
}
