package com.ecommerce.customer.controller;

import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ProductReview;
import com.ecommerce.library.repository.ProductRepository;
import com.ecommerce.library.repository.ProductReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-reviews")
public class ProductReviewController {

    @Autowired
    private ProductReviewRepository productReviewRepository;

    @Autowired
    private ProductRepository productRepository;

    // Get all reviews for a product
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductReview>> getProductReviews(@PathVariable Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        List<ProductReview> reviews = productReviewRepository.findByProduct(product);
        return ResponseEntity.ok(reviews);
    }

    // Add a new review for a product
    @PostMapping("/product/{productId}")
    public ResponseEntity<ProductReview> addProductReview(@PathVariable Long productId, @RequestBody ProductReview review) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        // Validate the review (you can add more validation logic as needed)
        if (review.getRating() < 1 || review.getRating() > 5) {
            return ResponseEntity.badRequest().body(null);
        }

        review.setProduct(product);
        ProductReview savedReview = productReviewRepository.save(review);
        return ResponseEntity.ok(savedReview);
    }

    // Update a review
    @PutMapping("/{reviewId}")
    public ResponseEntity<ProductReview> updateProductReview(@PathVariable Long reviewId, @RequestBody ProductReview updatedReview) {
        ProductReview existingReview = productReviewRepository.findById(reviewId).orElse(null);
        if (existingReview == null) {
            return ResponseEntity.notFound().build();
        }

        // Update necessary review fields
        existingReview.setComment(updatedReview.getComment());
        // Add more fields to update as needed

        ProductReview savedReview = productReviewRepository.save(existingReview);
        return ResponseEntity.ok(savedReview);
    }

    // Delete a review
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteProductReview(@PathVariable Long reviewId) {
        ProductReview existingReview = productReviewRepository.findById(reviewId).orElse(null);
        if (existingReview == null) {
            return ResponseEntity.notFound().build();
        }

        productReviewRepository.delete(existingReview);
        return ResponseEntity.noContent().build();
    }
}
