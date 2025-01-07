package com.sofico.sofico_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sofico.sofico_backend.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentIsNull();

    List<Category> findByIsMainTrueAndParentIsNotNull();

    List<Category> findByParentIsNotNullAndIsMainFalse();

    Optional<Category> findByName(String name);
}
