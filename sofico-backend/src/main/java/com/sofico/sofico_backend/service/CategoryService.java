package com.sofico.sofico_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sofico.sofico_backend.models.Category;
import com.sofico.sofico_backend.repository.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getMainCategories() {
        return categoryRepository.findByParentIsNull();
    }

    public List<Category> getMainSubcategories() {
        return categoryRepository.findByIsMainTrueAndParentIsNotNull();
    }

    public List<Category> getSubcategoriesOfSubcategories() {
        return categoryRepository.findByParentIsNotNullAndIsMainFalse();
    }

    public Category addCategory(Category category, Long parentId) {
        if (parentId != null) {
            Category parentCategory = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new IllegalArgumentException("Parent category not found"));
            category.setParent(parentCategory);
            parentCategory.getChildren().add(category);
        }
        return categoryRepository.save(category);
    }

    public void removeCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Category not found");
        }
        categoryRepository.deleteById(id);
    }

    public Category findOrCreateCategory(String name) {
        return categoryRepository.findByName(name)
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(name);
                    return categoryRepository.save(newCategory);
                });
    }
}
