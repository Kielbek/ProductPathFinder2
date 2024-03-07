package com.example.listazakupow.Repository;

import com.example.listazakupow.Entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findByNameIn(List<String> names);
}
