package com.sofico.sofico_backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sofico.sofico_backend.models.Brand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    Logger logger = LoggerFactory.getLogger(BrandRepository.class);

    Optional<Brand> findByName(String name);

    List<Brand> findByNameIn(List<String> names);

    default void logBrandSearchByName(String name) {
        logger.info("Searching for brand with name: {}", name);
    }

    default void logBrandsSearchByNames(List<String> names) {
        logger.info("Searching for brands with names: {}", names);
    }
}
