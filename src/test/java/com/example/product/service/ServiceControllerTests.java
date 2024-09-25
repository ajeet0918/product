package com.example.product.service;

import com.example.product.entitiy.Product;
import com.example.product.repository.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepo productRepo;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setProductName("Test Product");
    }

    @Test
    void getAllProductsTest() {
        List<Product> products = Arrays.asList(product);
        when(productRepo.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getProductName());
        verify(productRepo, times(1)).findAll();
    }

    @Test
    void getProductByIdTest() {
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Product", result.get().getProductName());
        verify(productRepo, times(1)).findById(1L);
    }

    @Test
    void createProductTest() {
        when(productRepo.save(product)).thenReturn(product);

        Product result = productService.createProduct(product);

        assertEquals("Test Product", result.getProductName());
        verify(productRepo, times(1)).save(product);
    }

    @Test
    void updateProductTest() {
        when(productRepo.save(product)).thenReturn(product);

        Product updatedProduct = productService.updateProduct(1L, product);

        assertEquals("Test Product", updatedProduct.getProductName());
        assertEquals(1L, updatedProduct.getId());
        verify(productRepo, times(1)).save(product);
    }

    @Test
    void deleteProductTest() {
        when(productRepo.existsById(1L)).thenReturn(true);

        productService.deleteProduct(1L);

        verify(productRepo, times(1)).deleteById(1L);
    }

    @Test
    void deleteProductNotFoundTest() {
        when(productRepo.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            productService.deleteProduct(1L);
        });

        assertEquals("CONTENT NOT FOUND", exception.getMessage());
        verify(productRepo, times(0)).deleteById(1L);
    }

    @Test
    void findByNameTest() {
        when(productRepo.findByproductName("Test Product")).thenReturn(Optional.of(product));

        Optional<Product> result = productService.findByName("Test Product");

        assertTrue(result.isPresent());
        assertEquals("Test Product", result.get().getProductName());
        verify(productRepo, times(1)).findByproductName("Test Product");
    }
}
