package com.ecommerce.library.service;

import com.ecommerce.library.model.ProductReview;

import java.util.List;

public interface ProductReviewService {
    ProductReview save(ProductReview productReview);
    List<ProductReview> findByProductId(Long productId);
    List<ProductReview> getAllProductReviews();
    void deleteProductReview(Long id);
}
