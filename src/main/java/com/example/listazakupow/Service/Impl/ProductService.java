package com.example.listazakupow.Service.Impl;

import com.example.listazakupow.Entity.Position;

import java.util.List;
import java.util.Map;

public interface ProductService {
    List<Position> findShortestPath(List<String> productList, String storeName);
}
