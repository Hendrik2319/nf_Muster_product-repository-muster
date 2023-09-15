package com.example.productrepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final IdService idService;

    public ProductService(ProductRepository productRepository, IdService idService) {
        this.productRepository = productRepository;
        this.idService = idService;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> findAllProductsBelowOrEqualTo(int price) {
        ArrayList<Product> products = new ArrayList<>();

        List<Product> allProducts = getAllProducts();
        for (Product product : allProducts)
            if (product.price() <= price)
                products.add(product);

        return products;
    }

    public Product addProduct(NewProduct newProduct) {
        Product product = new Product(
                idService.generateId(),
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
