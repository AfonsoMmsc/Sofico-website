package com.sofico.sofico_backend.repository;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sofico.sofico_backend.models.Category;
import com.sofico.sofico_backend.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Logger logger = LoggerFactory.getLogger(ProductRepository.class);

    // Method to find product by name
    Product findByName(String name);

    List<Product> findByCategoriesContaining(Category category);

    @SuppressWarnings("null")
    List<Product> findAll();

    // Find products by brand names
    List<Product> findByBrandNameIn(List<String> brands);

    default void logProductSearchByName(String name) {
        logger.info("Searching for product with name: {}", name);
    }

    default void logProductsByCategory(Category category) {
        logger.info("Searching for products in category: {}", category);
    }

    default void logProductsByBrandNames(List<String> brands) {
        logger.info("Searching for products with brand names: {}", brands);
    }

    default void logProductsByCategoryNames(List<String> categories) {
        logger.info("Searching for products with category names: {}", categories);
    }

    default void logProductsByCategoriesAndBrands(List<String> categories, List<String> brands) {
        logger.info("Searching for products with category names: {} and brand names: {}", categories, brands);
    }

    // Find products by category names
    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.name IN :categories")
    List<Product> findByCategories(@Param("categories") List<String> categories);

    // Find products by brand names
    @Query("SELECT p FROM Product p WHERE p.brand.name IN :brands")
    List<Product> findByBrands(@Param("brands") List<String> brands);

    // Find products by category and brand names
    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.name IN :categories AND p.brand.name IN :brands")
    List<Product> findByCategoriesAndBrands(@Param("categories") List<String> categories,
            @Param("brands") List<String> brands);

    @Query("SELECT p FROM Product p WHERE p.brand.id = :brandId")
    List<Product> findByBrandId(@Param("brandId") Long brandId);
}
