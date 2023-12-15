package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.repository.ProductRepository;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.utils.ImageUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;


    private final ImageUpload imageUpload;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<ProductDto> products() {
        List<ProductDto> allProducts = transferData(productRepository.getAllProduct());

// Tạo một Map để lưu trữ danh sách sản phẩm theo từng category
        Map<String, List<ProductDto>> productsByCategory = new HashMap<>();

// Phân loại sản phẩm vào từng category
        for (ProductDto product : allProducts) {
            Category category = product.getCategory();
            productsByCategory.computeIfAbsent(String.valueOf(category), k -> new ArrayList<>()).add(product);
        }

// Giới hạn số lượng sản phẩm trong mỗi category là 4
        int maxProductsPerCategory = 4;
        List<ProductDto> limitedProducts = new ArrayList<>();

        for (List<ProductDto> categoryProducts : productsByCategory.values()) {
            int count = Math.min(maxProductsPerCategory, categoryProducts.size());
            limitedProducts.addAll(categoryProducts.subList(0, count));
        }

        return limitedProducts;
    }

    @Override
    public List<ProductDto> allProduct() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = transferData(products);
        return productDtos;
    }

    @Override
    public Product save(MultipartFile imageProduct, MultipartFile imageProduct1,
                        MultipartFile imageProduct2, MultipartFile imageProduct3,
                        MultipartFile imageProduct4, ProductDto productDto) {
        Product product = new Product();
        try {
            if (imageProduct == null) {
                product.setImage(null);
            } else {
                imageUpload.uploadFile(imageProduct);
                product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            }
            if (imageProduct1 == null) {
                product.setImage1(null);
            } else {
                imageUpload.uploadFile(imageProduct1);
                product.setImage1(Base64.getEncoder().encodeToString(imageProduct1.getBytes()));
            }
            if (imageProduct2 == null) {
                product.setImage2(null);
            } else {
                imageUpload.uploadFile(imageProduct2);
                product.setImage2(Base64.getEncoder().encodeToString(imageProduct2.getBytes()));
            }
            if (imageProduct3 == null) {
                product.setImage3(null);
            } else {
                imageUpload.uploadFile(imageProduct3);
                product.setImage3(Base64.getEncoder().encodeToString(imageProduct3.getBytes()));
            }
            if (imageProduct4 == null) {
                product.setImage4(null);
            } else {
                imageUpload.uploadFile(imageProduct4);
                product.setImage4(Base64.getEncoder().encodeToString(imageProduct4.getBytes()));
            }
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.setCostPrice(productDto.getCostPrice());
            product.setCategory(productDto.getCategory());
            product.set_deleted(false);
            product.set_activated(true);
            return productRepository.save(product);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Product update(MultipartFile imageProduct, MultipartFile imageProduct1,
                          MultipartFile imageProduct2, MultipartFile imageProduct3,
                          MultipartFile imageProduct4, ProductDto productDto) {
        try {
            Product productUpdate = productRepository.getReferenceById(productDto.getId());
            if (imageProduct.getBytes().length > 0) {
                if (imageUpload.checkExist(imageProduct)) {
                    productUpdate.setImage(productUpdate.getImage());
                } else {
                    imageUpload.uploadFile(imageProduct);
                    productUpdate.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
                }
            }
            if (imageProduct1.getBytes().length > 0) {
                if (imageUpload.checkExist(imageProduct1)) {
                    productUpdate.setImage1(productUpdate.getImage1());
                } else {
                    imageUpload.uploadFile(imageProduct1);
                    productUpdate.setImage1(Base64.getEncoder().encodeToString(imageProduct1.getBytes()));
                }
            }
            if (imageProduct2.getBytes().length > 0) {
                if (imageUpload.checkExist(imageProduct2)) {
                    productUpdate.setImage2(productUpdate.getImage2());
                } else {
                    imageUpload.uploadFile(imageProduct2);
                    productUpdate.setImage2(Base64.getEncoder().encodeToString(imageProduct2.getBytes()));
                }
            }
            if (imageProduct3.getBytes().length > 0) {
                if (imageUpload.checkExist(imageProduct3)) {
                    productUpdate.setImage3(productUpdate.getImage3());
                } else {
                    imageUpload.uploadFile(imageProduct3);
                    productUpdate.setImage3(Base64.getEncoder().encodeToString(imageProduct3.getBytes()));
                }
            }
            if (imageProduct4.getBytes().length > 0) {
                if (imageUpload.checkExist(imageProduct4)) {
                    productUpdate.setImage4(productUpdate.getImage4());
                } else {
                    imageUpload.uploadFile(imageProduct4);
                    productUpdate.setImage4(Base64.getEncoder().encodeToString(imageProduct4.getBytes()));
                }
            }
            productUpdate.setCategory(productDto.getCategory());
            productUpdate.setId(productUpdate.getId());
            productUpdate.setName(productDto.getName());
            productUpdate.setDescription(productDto.getDescription());
            productUpdate.setCostPrice(productDto.getCostPrice());
            productUpdate.setSalePrice(productDto.getSalePrice());
            productUpdate.setCurrentQuantity(productDto.getCurrentQuantity());
            return productRepository.save(productUpdate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void enableById(Long id) {
        Product product = productRepository.getById(id);
        product.set_activated(true);
        product.set_deleted(false);
        productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        Product product = productRepository.getById(id);
        product.set_deleted(true);
        product.set_activated(false);
        productRepository.save(product);
    }

    @Override
    public ProductDto getById(Long id) {
        ProductDto productDto = new ProductDto();
        Product product = productRepository.getById(id);
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setCostPrice(product.getCostPrice());
        productDto.setSalePrice(product.getSalePrice());
        productDto.setCurrentQuantity(product.getCurrentQuantity());
        productDto.setCategory(product.getCategory());
        productDto.setImage(product.getImage());
        productDto.setImage1(product.getImage1());
        productDto.setImage2(product.getImage2());
        productDto.setImage3(product.getImage3());
        productDto.setImage4(product.getImage4());
        return productDto;
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).get();
    }

    @Override
    public List<ProductDto> randomProduct() {
        return transferData(productRepository.randomProduct());
    }

    @Override
    public Page<ProductDto> searchProducts(int pageNo, String keyword) {
        List<Product> products = productRepository.findAllByNameOrDescription(keyword);
        List<ProductDto> productDtoList = transferData(products);
        Pageable pageable = PageRequest.of(pageNo, 5);
        Page<ProductDto> dtoPage = toPage(productDtoList, pageable);
        return dtoPage;
    }

    @Override
    public Page<ProductDto> getAllProducts(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 6);
        List<ProductDto> productDtoLists = this.allProduct();
        Page<ProductDto> productDtoPage = toPage(productDtoLists, pageable);
        return productDtoPage;
    }

    @Override
    public Page<ProductDto> getAllProductsForCustomer(int pageNo) {
        return null;
    }

    @Override
    public List<ProductDto> findAllByCategory(String category) {
        return transferData(productRepository.findAllByCategory(category));
    }

    @Override
    public List<ProductDto> filterHighProducts() {
        return transferData(productRepository.filterHighProducts());
    }

    @Override
    public List<ProductDto> filterLowerProducts() {
        return transferData(productRepository.filterLowerProducts());
    }

    @Override
    public List<ProductDto> listViewProducts() {
        return transferData(productRepository.listViewProduct());
    }

    @Override
    public List<ProductDto> findByCategoryId(Long id) {
        return transferData(productRepository.getProductByCategoryId(id));
    }

    @Override
    public List<ProductDto> searchProducts(String keyword) {
        return transferData(productRepository.searchProducts(keyword));
    }

    private Page toPage(List list, Pageable pageable) {
        if (pageable.getOffset() >= list.size()) {
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size())
                ? list.size()
                : (int) (pageable.getOffset() + pageable.getPageSize());
        List subList = list.subList(startIndex, endIndex);
        return new PageImpl(subList, pageable, list.size());
    }

    private List<ProductDto> transferData(List<Product> products) {
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setCurrentQuantity(product.getCurrentQuantity());
            productDto.setCostPrice(product.getCostPrice());
            productDto.setSalePrice(product.getSalePrice());
            productDto.setDescription(product.getDescription());
            productDto.setImage(product.getImage());
            productDto.setImage1(product.getImage1());
            productDto.setImage2(product.getImage2());
            productDto.setImage3(product.getImage3());
            productDto.setImage4(product.getImage4());
            productDto.setCategory(product.getCategory());
            productDto.setActivated(product.is_activated());
            productDto.setDeleted(product.is_deleted());
            productDtos.add(productDto);
        }
        return productDtos;
    }
}
