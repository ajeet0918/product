package com.example.product.service;

import com.example.product.entitiy.Product;
import com.example.product.repository.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

public class ProductServeiceTests {

    @Mock
    private ProductRepo productRepo;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setId(7L);
        product.setProductName("banana");
    }

    @Test
    void getAllProductsTest() {
        // Given
        List<Product> products = Arrays.asList(product);
        given(productRepo.findAll()).willReturn(products);

        // When
        List<Product> result = productService.getAllProducts();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProductName()).isEqualTo("banana");
        then(productRepo).should(times(1)).findAll();
    }

    @Test
    void getProductByIdTest() {
        // Given
        given(productRepo.findById(7L)).willReturn(Optional.of(product));

        // When
        Optional<Product> result = Optional.ofNullable(productService.getProductById(7L));

        // Then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getProductName()).isEqualTo("banana");
        then(productRepo).should().findById(7L);
    }

    @Test
    void createProductTest() {
        // Given
        given(productRepo.save(product)).willReturn(product);

        Product result = productService.createProduct(product);

        // Then
        assertThat(result.getProductName()).isEqualTo("banana");
        then(productRepo).should().save(product);
    }

    @Test
    void updateProductTest() {
        // Arrange
        product.setId(7L); // Ensure product has the correct ID for the test
        product.setProductName("Test Product"); // Set the product name for the test

        // Mock the behavior of productRepo
        given(productRepo.existsById(7L)).willReturn(true); // Mock existence
        given(productRepo.save(product)).willReturn(product); // Mock save

        // Act
        Product updatedProduct = productService.updateProduct(7L, product);

        // Assert
        assertThat(updatedProduct.getProductName()).isEqualTo("Test Product");
        assertThat(updatedProduct.getId()).isEqualTo(7L);

        // Verify that save was called
        then(productRepo).should().save(product);
    }


    @Test
    void deleteProductTest() {
        // Given
        given(productRepo.existsById(7L)).willReturn(true);

        // When
        productService.deleteProduct(7L);

        // Then
        then(productRepo).should(times(1)).deleteById(7L);
    }

    @Test
    void deleteProductNotFoundTest() {
        // Given
        given(productRepo.existsById(1L)).willReturn(false);

        // When / Then
        RuntimeException thrown = org.assertj.core.api.Assertions.catchThrowableOfType(() -> {
            productService.deleteProduct(1L);
        }, RuntimeException.class);

        assertThat(thrown).hasMessageContaining("CONTENT NOT FOUND");
        then(productRepo).should(never()).deleteById(1L);
    }

    @Test
    void findByNameTest() {
        // Given
        given(productRepo.findByproductName("banana")).willReturn(Optional.of(product));

        // When
        Optional<Product> result = productService.findByName("banana");

        // Then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getProductName()).isEqualTo("banana");
        then(productRepo).should().findByproductName("banana");
    }
}