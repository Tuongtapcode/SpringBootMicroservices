package com.nnt.microservices.product.service;

import com.nnt.microservices.product.dto.ProductReponse;
import com.nnt.microservices.product.dto.ProductRequest;
import com.nnt.microservices.product.model.Product;
import com.nnt.microservices.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public ProductReponse createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .build();
        productRepository.save(product);
        log.info("Product {} has been created", product);
        return new ProductReponse(product.getId(), product.getName(), product.getDescription(), product.getPrice());
    }

    public List<ProductReponse> getAllProducts() {
        return productRepository.findAll()
                .stream().map(product -> new ProductReponse(product.getId(), product.getName(), product.getDescription(), product.getPrice())).toList();
    }
}
