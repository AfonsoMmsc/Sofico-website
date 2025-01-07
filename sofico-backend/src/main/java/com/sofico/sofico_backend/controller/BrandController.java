package com.sofico.sofico_backend.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sofico.sofico_backend.models.Brand;
import com.sofico.sofico_backend.service.BrandService;

@RestController
@RequestMapping("/brands")
public class BrandController {

    private static final Logger logger = LoggerFactory.getLogger(BrandController.class);

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    // List all brands
    @GetMapping
    public ResponseEntity<List<Brand>> listAllBrands() {
        logger.info("Fetching all brands");
        List<Brand> brands = brandService.listAll();
        logger.info("Found {} brands", brands.size());
        return ResponseEntity.ok(brands);
    }

    // Get brand by ID
    @GetMapping("/{id}")
    public ResponseEntity<Brand> getBrandById(@PathVariable Long id) {
        logger.info("Fetching brand with ID: {}", id);
        Brand brand = brandService.getById(id);
        return brand != null ? ResponseEntity.ok(brand) : ResponseEntity.notFound().build();
    }

    // Get brand by name
    @GetMapping("/name/{name}")
    public ResponseEntity<Brand> getBrandByName(@PathVariable String name) {
        logger.info("Fetching brand with name: {}", name);
        Brand brand = brandService.getBrandByName(name);
        return brand != null ? ResponseEntity.ok(brand) : ResponseEntity.notFound().build();
    }

    // Create a new brand
    @PostMapping
    public ResponseEntity<Brand> createBrand(@RequestBody Brand newBrand) {
        logger.info("Creating new brand: {}", newBrand.getName());
        try {
            Brand createdBrand = brandService.save(newBrand);
            logger.info("Brand '{}' created successfully", createdBrand.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBrand);
        } catch (Exception e) {
            logger.error("Error creating brand: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Update an existing brand
    @PutMapping("/{id}")
    public ResponseEntity<Brand> updateBrand(@PathVariable Long id, @RequestBody Brand updatedBrand) {
        logger.info("Updating brand with ID: {}", id);
        Brand existingBrand = brandService.getById(id);
        if (existingBrand != null) {
            existingBrand.setName(updatedBrand.getName());
            // Add other attributes to update as necessary
            Brand savedBrand = brandService.save(existingBrand);
            logger.info("Brand updated successfully: {}", savedBrand.getName());
            return ResponseEntity.ok(savedBrand);
        }
        logger.warn("Brand with ID {} not found", id);
        return ResponseEntity.notFound().build();
    }

    // Remove a brand by ID with logging
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        logger.info("Deleting brand with ID: {}", id);
        if (brandService.deleteBrand(id)) {
            logger.info("Brand with ID {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        }
        logger.warn("Brand with ID {} not found", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Get brands by a list of names
    @GetMapping("/names")
    public ResponseEntity<List<Brand>> getBrandsByNames(@RequestParam List<String> names) {
        logger.info("Fetching brands with names: {}", names);
        List<Brand> brands = brandService.findBrandsByNames(names);
        logger.info("Found {} brands with specified names", brands.size());
        return ResponseEntity.ok(brands);
    }

    @PostMapping("/addMany")
    public ResponseEntity<List<Brand>> addMultipleBrands(@RequestBody List<Brand> brands) {
        List<Brand> savedBrands = brandService.addMultipleBrands(brands);
        return ResponseEntity.ok(savedBrands);
    }
}
