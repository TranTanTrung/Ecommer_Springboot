package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.ProductReview;
import com.ecommerce.library.repository.ProductReviewRepository;
import com.ecommerce.library.service.ProductReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductReviewServiceImpl implements ProductReviewService {

    private final ProductReviewRepository productReviewRepository;

    @Autowired
    public ProductReviewServiceImpl(ProductReviewRepository productReviewRepository) {
        this.productReviewRepository = productReviewRepository;
    }

    @Override
    public ProductReview save(ProductReview productReview) {
        // Thực hiện lưu đánh giá sản phẩm vào cơ sở dữ liệu
        return productReviewRepository.save(productReview);
    }

    @Override
    public List<ProductReview> findByProductId(Long productId) {
        return productReviewRepository.findByProduct_Id(productId);
    }

    @Override
    public List<ProductReview> getAllProductReviews() {
        return productReviewRepository.findAll();
    }

    @Override
    public void deleteProductReview(Long id) {
        productReviewRepository.deleteById(id);
    }

}
