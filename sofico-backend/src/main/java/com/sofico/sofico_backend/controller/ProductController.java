package com.sofico.sofico_backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sofico.sofico_backend.models.Product;
import com.sofico.sofico_backend.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Get all products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        logger.info("Fetching all products");
        List<Product> products = productService.getAll();
        logger.info("Found {} products", products.size());
        return ResponseEntity.ok(products);
    }

    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        logger.info("Fetching product with ID: {}", id);
        Product product = productService.getById(id);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    // Get product by name
    @GetMapping("/name/{name}")
    public ResponseEntity<Product> getProductByName(@PathVariable String name) {
        logger.info("Fetching product with name: {}", name);
        Product product = productService.getByName(name);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    // Filter products by categories and/or brands
    @GetMapping("/filter")
    public ResponseEntity<List<Product>> getFilterProductsByBrandAndCategories(
            @RequestParam(value = "categories", required = false) List<String> categories,
            @RequestParam(value = "brands", required = false) List<String> brands) {

        logger.info("Filtering products by categories: {} and brands: {}", categories, brands);
        List<Product> filteredProducts = productService.findByCategoriesAndBrands(categories, brands);
        logger.info("Found {} products by filters", filteredProducts.size());
        return ResponseEntity.ok(filteredProducts);
    }

    // Add a single product
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        logger.info("Adding product: {}", product.getName());
        Product savedProduct = productService.addProductWithCategoryCheck(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    // Add multiple products
    @PostMapping("/bulk")
    public ResponseEntity<List<Product>> addProducts(@RequestBody List<Product> products) {
        logger.info("Adding multiple products");
        List<Product> savedProducts = products.stream()
                .map(productService::addProductWithCategoryCheck)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProducts);
    }

    // Delete a product by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        logger.info("Deleting product with ID: {}", id);
        boolean deleted = productService.removeProduct(id);
        if (deleted) {
            logger.info("Product with ID {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Product with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    // Update a product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        logger.info("Updating product with ID: {}", id);
        Product product = productService.updateProduct(id, updatedProduct);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }
}
