package com.example.product.repository;

import com.example.product.entitiy.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Long> {
 Optional<Product> findByproductName(String productname);
}
