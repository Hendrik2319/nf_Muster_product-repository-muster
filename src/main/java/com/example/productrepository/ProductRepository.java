package com.example.productrepository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {

    @Query("{ 'price': { '$lte': ?0 } }")
    List<Product> findAllLowerOrEqualThan(int price);

}
