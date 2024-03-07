package com.example.listazakupow.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String link;
    private String description;
    private String dimensions;
    private String productCode;
    @OneToMany
    private List<Position> positions;
    @ManyToMany
    private List<Product> productList;

    public Product(String name, String description, String dimensions, String productCode, List<Product> productList) {
        this.name = name;
        this.description = description;
        this.dimensions = dimensions;
        this.productCode = productCode;
        this.productList = productList;
        this.positions = new ArrayList<>();
    }

    public Product(String name, String link, String description) {
        this.name = name;
        this.link = link;
        this.description = description;
    }
}

