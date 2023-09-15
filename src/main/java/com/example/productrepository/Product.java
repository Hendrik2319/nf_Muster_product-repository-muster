package com.example.productrepository;

import lombok.With;

@With
public record Product(String id, String title, int price) {
}
