package com.example.product.service;


import com.example.product.entitiy.Product;
import com.example.product.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepo productRepo;

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepo.findById(id);
    }

    public Product createProduct(Product product) {
        return productRepo.save(product);
    }

    public Product updateProduct(Long id, Product product) {
        product.setId(id);
        return productRepo.save(product);
    }

    public void deleteProduct(Long id) {
        if (!productRepo.existsById(id)) {
            throw new RuntimeException("CONTENT NOT FOUND");
        }
        productRepo.deleteById(id);
    }

    public Optional<Product> findByName(String productName){
        return  productRepo.findByproductName(productName);
    }

}

