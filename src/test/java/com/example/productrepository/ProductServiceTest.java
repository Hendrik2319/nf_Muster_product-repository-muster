package com.example.productrepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductRepository productRepository;
    private ProductService productService;
    private IdService idService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        idService = mock(IdService.class);
        productService = new ProductService(productRepository,idService);
    }

    @Test
    void whenGetAllProducts_calledWithEmptyRepo_returnEmptyList() {
        // Given
        when(productRepository.findAll()).thenReturn(List.of());

        // When
        List<Product> actual = productService.getAllProducts();

        // Then
        List<Object> expected = List.of();
        assertEquals(expected, actual);
    }

    @Test
    void whenGetAllProducts_calledWithNonEmptyRepo_returnListOfRepoContent() {
        // Given
        when(productRepository.findAll()).thenReturn(List.of(
                new Product("123","Title1",12),
                new Product("124","Title2",15),
                new Product("125","Title3",134),
                new Product("126","Title4",142),
                new Product("127","Title5",152)
        ));

        // When
        List<Product> actual = productService.getAllProducts();

        // Then

        verify(productRepository).findAll();
        List<Product> expected = List.of(
                new Product("123","Title1",12),
                new Product("124","Title2",15),
                new Product("125","Title3",134),
                new Product("126","Title4",142),
                new Product("127","Title5",152)
        );
        assertEquals(expected, actual);
    }

    @Test
    void getProduct() {
    }

    @Test
    void removeProduct() {
    }

    @Test
    void updateProduct() {
    }
}