package com.example.product.controller;


import com.example.product.entitiy.Product;
import com.example.product.exception.ProductNotFoundException;
import com.example.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        List<Product> product = productService.getAllProducts();
        if (product==null || product.isEmpty()){
            throw new ProductNotFoundException("There is  no product here");
        }
        return product;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = Optional.ofNullable(productService.getProductById(id));
        return product.map(ResponseEntity::ok)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));
    }

    @GetMapping("/name/{productName}")
    public ResponseEntity<Product> getProductByName(@PathVariable String productName) {
        Optional<Product> product = productService.findByName(productName);
        return product.map(ResponseEntity::ok)
                .orElseThrow(() -> new ProductNotFoundException("Product with id" +productName+ "not found "));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Optional<Product> product1 = Optional.ofNullable(productService.createProduct(product));
        return  product1.map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Error"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody Product product) {
           productService.updateProduct(id,product);
        return new ResponseEntity<>("The item has updated sucessfuly", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>("You have successfully removed the content", HttpStatus.OK);
    }


}
