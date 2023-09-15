package com.example.productrepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product addProduct(NewProduct newProduct) {
        Product product = new Product(
                UUID.randomUUID().toString(),
                newProduct.title(),
                newProduct.price()
        );
        return productRepository.save(product);
    }

    public Optional<Product> getProduct(String id) {
        return productRepository.findById(id);
    }

    public void removeProduct(String id) {
        productRepository.deleteById(id);
    }

    public Optional<Product> updateProduct(String id, String title, Integer price) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            Product product_ = product.get();
            if (title!=null) product_ = product_.withTitle(title);
            if (price!=null) product_ = product_.withPrice(price);
            return Optional.of(productRepository.save(product_));
        }
        return Optional.empty();
    }
}
