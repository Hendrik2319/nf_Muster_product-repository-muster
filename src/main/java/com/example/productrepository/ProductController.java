package com.example.productrepository;

import com.mongodb.lang.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Product> getProduct(@PathVariable String id) {
        return ResponseEntity.of(productService.getProduct(id));
    }

    @DeleteMapping("/{id}")
    public String removeProduct(@PathVariable String id) {
        productService.removeProduct(id);
        return "OK";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id, @Nullable @RequestParam String title, @Nullable @RequestParam Integer price) {
        //System.out.printf("updateProduct( %s, %s, %d )%n", id, title, price);
        return ResponseEntity.of(productService.updateProduct(id,title,price));
    }

}
