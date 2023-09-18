package com.example.productrepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String addProduct(String title, int price) throws Exception {
        String body = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            { "title":"%s", "price":%d }
                        """.formatted(title, price))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Product product = objectMapper.readValue(body, Product.class);
        assertEquals(title, product.title());
        assertEquals(price, product.price());
        return product.id();
    }

    @Test
    @DirtiesContext
    void whenGetAllProducts_performsOnEmptyRepo_returnsEmptyJsonArray() throws Exception {
        // Given

        // When
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/products")
                )

        // Then
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DirtiesContext
    void whenGetAllProducts_performsOnNonEmptyRepo_returnsNonEmptyJsonArray() throws Exception {
        // Given
        String id1 = addProduct("Title 1", 101);
        String id2 = addProduct("Title 2", 102);
        String id3 = addProduct("Title 3", 103);

        // When
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/products")
                )

        // Then
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    [
                        { "id":"%s", "title":"Title 1", "price":101 },
                        { "id":"%s", "title":"Title 2", "price":102 },
                        { "id":"%s", "title":"Title 3", "price":103 }
                    ]
                """.formatted(id1, id2, id3)))
        ;
    }

    @Test
    @DirtiesContext
    void whenAddProduct_getsNewProduct_returnsProduct() throws Exception {
        // Given

        // When
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\":\"Title 1\", \"price\":101 }")
                )

        // Then
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"title\":\"Title 1\", \"price\":101 }"))
                .andExpect(jsonPath("$.id").isString())
        ;
    }

    @Test
    @DirtiesContext
    void whenRemoveProduct_getsProductId_returnsOk() throws Exception {
        // Given
        String id = addProduct("Title 1", 101);

        // When
        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/api/products/%s".formatted(id))
                )

        // Then
                .andExpect(status().isOk())
                .andExpect(content().string("OK"))
        ;
    }

    @Test
    @DirtiesContext
    void whenGetProduct_getsValidProductId_returnsProduct() throws Exception {
        // Given
        String id = addProduct("Title 1", 101);

        // When
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/products/%s".formatted(id))
                )

        // Then
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    { "id":"%s", "title":"Title 1", "price":101 }
                """.formatted(id)))
        ;
    }

    @Test
    @DirtiesContext
    void whenGetProduct_getsInvalidProductId_returns404() throws Exception {
        // Given
        String id = addProduct("Title 1", 101);

        // When
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/products/XX%sXX".formatted(id))
                )

        // Then
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @DirtiesContext
    void whenUpdateProduct_getsInvalidProductId_returns404() throws Exception {
        // Given
        String id = addProduct("Title 1", 101);

        // When
        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/products/XX%sXX?title=Test Text".formatted(id))
                )

        // Then
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @DirtiesContext
    void whenUpdateProduct_getsValidProductIdAndTitle_returnsChangedProduct() throws Exception {
        // Given
        String id = addProduct("Title 1", 101);

        // When
        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/products/%s?title=Test Text".formatted(id))
                )

        // Then
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        { "id":"%s", "title":"Test Text", "price":101 }
                """.formatted(id)))
        ;
    }

    @Test
    @DirtiesContext
    void whenUpdateProduct_getsValidProductIdAndPrice_returnsChangedProduct() throws Exception {
        // Given
        String id = addProduct("Title 1", 101);

        // When
        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/products/%s?price=25".formatted(id))
                )

        // Then
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        { "id":"%s", "title":"Title 1", "price":25 }
                """.formatted(id)))
        ;
    }

    @Test
    @DirtiesContext
    void whenUpdateProduct_getsValidProductIdTitleAndPrice_returnsChangedProduct() throws Exception {
        // Given
        String id = addProduct("Title 1", 101);

        // When
        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/products/%s?title=Test Text&price=25".formatted(id))
                )

        // Then
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        { "id":"%s", "title":"Test Text", "price":25 }
                """.formatted(id)))
        ;
    }

}