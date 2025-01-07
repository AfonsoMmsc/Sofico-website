package com.sofico.sofico_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sofico.sofico_backend.models.Category;
import com.sofico.sofico_backend.service.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/main")
    public ResponseEntity<List<Category>> getMainCategories() {
        return ResponseEntity.ok(categoryService.getMainCategories());
    }

    @GetMapping("/main-subcategories")
    public List<Category> getMainSubcategories() {
        return categoryService.getMainSubcategories();
    }

    @GetMapping("/subcategories-of-subcategories")
    public ResponseEntity<List<Category>> getSubcategoriesOfSubcategories() {
        return ResponseEntity.ok(categoryService.getSubcategoriesOfSubcategories());
    }

    @PostMapping
    public ResponseEntity<Category> addCategory(
            @RequestBody Category category,
            @RequestParam(required = false) Long parentId) {
        return ResponseEntity.ok(categoryService.addCategory(category, parentId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCategory(@PathVariable Long id) {
        categoryService.removeCategory(id);
        return ResponseEntity.noContent().build();
    }
}
