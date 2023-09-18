package com.example.productrepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

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
    void addProduct() {
        // Given
        when(idService.generateId()).thenReturn("456");
        when(productRepository.save(new Product("456","dsdsf",45))).thenReturn(
                new Product("456","dsdsf",45)
        );

        // When
        Product actual = productService.addProduct(new NewProduct("dsdsf",45));

        // Then
        verify(idService).generateId();
        verify(productRepository).save(new Product("456","dsdsf",45));
        Product expected = new Product("456","dsdsf",45);
        assertEquals(expected, actual);
    }

    @Test
    void whenGetAllProducts_calledWithEmptyRepo_returnEmptyList() {
        // Given
        when(productRepository.findAll()).thenReturn(List.of());

        // When
        List<Product> actual = productService.getAllProducts();

        // Then
        verify(productRepository).findAll();
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
    void whenGetProduct_getsInvalidID_returnEmptyOptional() {
        // Given
        when(productRepository.findById("456")).thenReturn(
                Optional.empty()
        );

        // When
        Optional<Product> actual = productService.getProduct("456");

        // Then
        verify(productRepository).findById("456");
        Optional<Product> expected = Optional.empty();
        assertEquals(expected, actual);
    }

    @Test
    void whenGetProduct_getsValidID_returnCorrespondingProduct() {
        // Given
        when(productRepository.findById("456")).thenReturn(
                Optional.of(
                        new Product("456","Title1",12)
                )
        );

        // When
        Optional<Product> actual = productService.getProduct("456");

        // Then
        assertNotNull(actual);
        assertTrue(actual.isPresent());

        verify(productRepository).findById("456");
        Product expected = new Product("456","Title1",12);
        assertEquals(expected, actual.get());
    }

    @Test
    void removeProduct() {
        // Given

        // When
        productService.removeProduct("456");

        // Then
        verify(productRepository).deleteById("456");
    }

    @Test
    void whenUpdateProduct_getsInvalidID_returnEmptyOptional() {
        // Given
        when(productRepository.findById("456")).thenReturn(
                Optional.empty()
        );

        // When
        Optional<Product> actual = productService.updateProduct("456", "dsdsf", 12);

        // Then
        verify(productRepository).findById("456");
        Optional<Product> expected = Optional.empty();
        assertEquals(expected, actual);
    }

    @Test
    void whenUpdateProduct_getsValidID_returnUpdatedProduct() {
        // Given
        when(productRepository.findById("456")).thenReturn(
                Optional.of(
                        new Product("456","Title1",45)
                )
        );
        when(productRepository.save(new Product("456","dsdsf",12))).thenReturn(
                new Product("456","dsdsf",12)
        );

        // When
        Optional<Product> actual = productService.updateProduct("456", "dsdsf", 12);

        // Then
        assertNotNull(actual);
        assertTrue(actual.isPresent());

        verify(productRepository).findById("456");
        verify(productRepository).save(new Product("456","dsdsf",12));
        Product expected = new Product("456","dsdsf",12);
        assertEquals(expected, actual.get());
    }

    @Test
    void whenUpdateProduct_getsValidIDAndTitle_returnUpdatedProduct() {
        // Given
        when(productRepository.findById("456")).thenReturn(
                Optional.of(
                        new Product("456","Title1",45)
                )
        );
        when(productRepository.save(new Product("456","dsdsf",45))).thenReturn(
                new Product("456","dsdsf",45)
        );

        // When
        Optional<Product> actual = productService.updateProduct("456", "dsdsf", null);

        // Then
        assertNotNull(actual);
        assertTrue(actual.isPresent());

        verify(productRepository).findById("456");
        verify(productRepository).save(new Product("456","dsdsf",45));
        Product expected = new Product("456","dsdsf",45);
        assertEquals(expected, actual.get());
    }

    @Test
    void whenUpdateProduct_getsValidIDAndPrice_returnUpdatedProduct() {
        // Given
        when(productRepository.findById("456")).thenReturn(
                Optional.of(
                        new Product("456","Title1",45)
                )
        );
        when(productRepository.save(new Product("456","Title1",12))).thenReturn(
                new Product("456","Title1",12)
        );

        // When
        Optional<Product> actual = productService.updateProduct("456", null, 12);

        // Then
        assertNotNull(actual);
        assertTrue(actual.isPresent());

        verify(productRepository).findById("456");
        verify(productRepository).save(new Product("456","Title1",12));
        Product expected = new Product("456","Title1",12);
        assertEquals(expected, actual.get());
    }
}