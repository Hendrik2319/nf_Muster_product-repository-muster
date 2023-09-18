package com.example.productrepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DirtiesContext
    void whenGetAllProducts_performsOnEmptyRepo() throws Exception {
        // Given

        // When
        mockMvc
                .perform(MockMvcRequestBuilders.get("/api/products"))

        // Then
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DirtiesContext
    void whenGetAllProducts_performsOnNonEmptyRepo() throws Exception {
        // Given
        productRepository.save(new Product("id1", "Title 1", 101));
        productRepository.save(new Product("id2", "Title 2", 102));
        productRepository.save(new Product("id3", "Title 3", 103));

        // When
        mockMvc
                .perform(MockMvcRequestBuilders.get("/api/products"))

                // Then
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    [
                        { "id": "id1", "title":"Title 1", "price":101 },
                        { "id": "id2", "title":"Title 2", "price":102 },
                        { "id": "id3", "title":"Title 3", "price":103 }
                    ]
                """))
        ;
    }

}