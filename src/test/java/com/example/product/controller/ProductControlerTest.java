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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControlerTest {
    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product mockProduct;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        mockProduct = new Product(2L, "Test Product"); // Common product for multiple tests
    }

    private String productToJson(Product product) {
        return String.format("{\"productName\":\"%s\"}", product.getProductName());
    }

    @Test
    public void testGetAllProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(Collections.singletonList(mockProduct));

        mockMvc.perform(get("/product").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(mockProduct.getId()))
                .andExpect(jsonPath("$[0].productName").value(mockProduct.getProductName()));
    }

    @Test
    public void testGetProductById() throws Exception {
        when(productService.getProductById(2L)).thenReturn(Optional.of(mockProduct));

        mockMvc.perform(get("/product/2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockProduct.getId()))
                .andExpect(jsonPath("$.productName").value(mockProduct.getProductName()));
    }

    @Test
    public void testGetProductById_NotFound() throws Exception {
        when(productService.getProductById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateProduct() throws Exception {
        when(productService.createProduct(any(Product.class))).thenReturn(mockProduct);

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productToJson(mockProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(mockProduct.getId()))
                .andExpect(jsonPath("$.productName").value(mockProduct.getProductName()));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        when(productService.updateProduct(anyLong(), any(Product.class))).thenReturn(mockProduct);

        mockMvc.perform(put("/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productToJson(mockProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockProduct.getId()));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/product/2"))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(2L);
    }

}
