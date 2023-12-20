package com.ecommerce.library.repository;

import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    // Tìm đánh giá theo sản phẩm
    List<ProductReview> findByProduct(Product product);

    List<ProductReview> findByProduct_Id(Long productId);
}
