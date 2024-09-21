package com.example.product.controller;

import com.example.product.entitiy.Product;
import com.example.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProductcontrollerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build(); // Set up MockMvc
    }

    @Test
    public void testGetAllProducts() throws Exception {
        Product product = new Product();
        product.setId(2L);
        product.setProductName("Test Product");
        when(productService.getAllProducts()).thenReturn(Collections.singletonList(product));

        // Act & Assert: Perform GET request and check response
        mockMvc.perform(get("/product")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].productName").value("Test Product"));
    }

    @Test
    public void testGetProductById() throws Exception {
        // Arrange: Prepare a sample product
        Product product = new Product();
        product.setId(2L);
        product.setProductName("Test Product");

        // Mock the service for existing product
        when(productService.getProductById(2L)).thenReturn(Optional.of(product));
        mockMvc.perform(get("/product/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.productName").value("Test Product"));
    }

    @Test
    public void testGetProductById_NotFound() throws Exception {
        when(productService.getProductById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateProduct() throws Exception {
        Product product = new Product();
        product.setId(2L);
        product.setProductName("Test Product");
        when(productService.createProduct(any(Product.class))).thenReturn(product);
        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productName\":\"Test Product\"}")) // Sample JSON input
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.productName").value("Test Product"));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        // Arrange: Prepare an updated product
        Product product = new Product();
        product.setId(2L);
        product.setProductName("Updated Product");

        // Mock the service to return the updated product
        when(productService.updateProduct(anyLong(), any(Product.class))).thenReturn(product);

        // Act & Assert: Perform PUT request and check response
        mockMvc.perform(put("/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productName\":\"Updated Product\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.productName").value("Updated Product"));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/product/2"))
                .andExpect(status().isNoContent());
        verify(productService, times(1)).deleteProduct(2L);
    }
}
