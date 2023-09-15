package com.example.productrepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts(){
      return productService.getAllProducts();
    }

    @PostMapping
    public Product addProduct(@RequestBody NewProduct newProduct){
        return productService.addProduct(newProduct);
    }

    @GetMapping("/{id}")
    public Optional<Product> getProducts(@PathVariable String id) {
        return productService.getProduct(id);
    }

}
