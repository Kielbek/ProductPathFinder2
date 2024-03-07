package com.example.listazakupow.Service;

import com.example.listazakupow.Entity.Position;
import com.example.listazakupow.Entity.Product;
import com.example.listazakupow.Repository.ProductRepository;
import com.example.listazakupow.Service.Impl.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    public List<Position> findShortestPath(List<String> productList, String storeName) {
        List<Product> products = productRepository.findByNameIn(productList);

        // Collect positions for each product
        Map<Product, List<Position>> productPositionsMap = new HashMap<>();
        for (Product product : products) {
            List<Position> positions = product.getPositions();
            productPositionsMap.put(product, positions);
        }

        // Build graph (positions as vertices, distances as edges)
        Map<Position, Map<Position, Double>> graph = buildGraph(productPositionsMap);

        // Find shortest path using Dijkstra's algorithm
        Position start = findStartPosition(productPositionsMap, storeName);
        Map<Position, Double> distances = dijkstra(graph, start);

        // Get positions in shortest path
        List<Position> shortestPath = new ArrayList<>();
        for (Position position : distances.keySet()) {
            shortestPath.add(position);
        }

        return shortestPath;
    }

    // Build graph with positions as vertices and distances as edges
    private Map<Position, Map<Position, Double>> buildGraph(Map<Product, List<Position>> productPositionsMap) {
        Map<Position, Map<Position, Double>> graph = new HashMap<>();
        for (Product product : productPositionsMap.keySet()) {
            List<Position> positions = productPositionsMap.get(product);
            for (Position p1 : positions) {
                Map<Position, Double> edges = new HashMap<>();
                for (Position p2 : positions) {
                    if (!p1.equals(p2)) {
                        double distance = calculateDistance(p1, p2);
                        edges.put(p2, distance);
                    }
                }
                graph.put(p1, edges);
            }
        }
        return graph;
    }

    // Calculate Euclidean distance between two positions
    private double calculateDistance(Position p1, Position p2) {
        int dx = p1.getX() - p2.getX();
        int dy = p1.getY() - p2.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    // Find starting position based on store name
    private Position findStartPosition(Map<Product, List<Position>> productPositionsMap, String storeName) {
        for (Product product : productPositionsMap.keySet()) {
            List<Position> positions = productPositionsMap.get(product);
            for (Position position : positions) {
                if (position.getStoreName().equals(storeName)) {
                    return position;
                }
            }
        }
        return null; // Store not found
    }

    // Dijkstra's algorithm to find shortest paths
    private Map<Position, Double> dijkstra(Map<Position, Map<Position, Double>> graph, Position start) {
        Map<Position, Double> distances = new HashMap<>();
        Set<Position> visited = new HashSet<>();
        PriorityQueue<Position> queue = new PriorityQueue<>((p1, p2) -> Double.compare(distances.getOrDefault(p1, Double.POSITIVE_INFINITY), distances.getOrDefault(p2, Double.POSITIVE_INFINITY)));


        // Initialization
        for (Position position : graph.keySet()) {
            distances.put(position, Double.POSITIVE_INFINITY);
        }
        distances.put(start, 0.0);
        queue.offer(start);

        // Dijkstra's algorithm
        while (!queue.isEmpty()) {
            Position current = queue.poll();
            visited.add(current);
            Map<Position, Double> neighbors = graph.get(current);
            for (Position neighbor : neighbors.keySet()) {
                if (!visited.contains(neighbor)) {
                    double newDistance = distances.get(current) + neighbors.get(neighbor);
                    if (newDistance < distances.get(neighbor)) {
                        distances.put(neighbor, newDistance);
                        queue.offer(neighbor);
                    }
                }
            }
        }

        return distances;
    }
}
