package com.sofico.sofico_backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sofico.sofico_backend.models.Brand;
import com.sofico.sofico_backend.repository.BrandRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class BrandService {

    private static final Logger logger = LoggerFactory.getLogger(BrandService.class);

    private final BrandRepository brandRepository;
    private final ProductService productService;

    // Constructor Injection
    public BrandService(BrandRepository brandRepository, ProductService productService) {
        this.brandRepository = brandRepository;
        this.productService = productService;
    }

    // Method to list all brands
    public List<Brand> listAll() {
        logger.info("Fetching all brands");
        return brandRepository.findAll();
    }

    // Method to get brand by ID
    public Brand getById(Long id) {
        logger.info("Fetching brand by ID: {}", id);
        return brandRepository.findById(id).orElse(null);
    }

    // Method to get brand by name, throws exception if not found
    public Brand getBrandByName(String name) {
        logger.info("Fetching brand by name: {}", name);
        return brandRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Brand not found"));
    }

    // Method to save a brand
    public Brand save(Brand brand) {
        logger.info("Saving brand: {}", brand.getName());
        return brandRepository.save(brand);
    }

    // Method to delete a brand by ID
    public boolean deleteBrand(Long id) {
        logger.info("Attempting to delete brand with ID: {}", id);
        if (brandRepository.existsById(id)) {
            productService.unlinkProductsFromBrand(id);
            brandRepository.deleteById(id);
            logger.info("Deleted brand with ID: {}", id);
            return true;
        }
        logger.warn("Brand with ID: {} not found", id);
        return false;
    }

    // Method to get or add a brand
    public Brand getOrAddBrand(Brand brand) {
        logger.info("Fetching or adding brand with name: {}", brand.getName());
        return brandRepository.findByName(brand.getName())
                .orElseGet(() -> brandRepository.save(brand));
    }

    // Method to find brands by names
    public List<Brand> findBrandsByNames(List<String> names) {
        logger.info("Fetching brands by names: {}", names);
        return brandRepository.findByNameIn(names);
    }

    public List<Brand> addMultipleBrands(List<Brand> brands) {
        return brands.stream()
                .map(brand -> {
                    if (brandRepository.findByName(brand.getName()).isEmpty()) {
                        return brandRepository.save(brand);
                    } else {
                        return brandRepository.findByName(brand.getName()).get();
                    }
                })
                .collect(Collectors.toList());
    }
}
