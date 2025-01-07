package com.sofico.sofico_backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sofico.sofico_backend.models.Category;
import com.sofico.sofico_backend.models.Product;
import com.sofico.sofico_backend.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product getByName(String name) {
        return productRepository.findByName(name);
    }

    public List<Product> findByCategoriesAndBrands(List<String> categories, List<String> brands) {
        // Lógica de filtragem combinada
        return productRepository.findByCategoriesAndBrands(categories, brands);
    }

    public Product addProductWithCategoryCheck(Product product) {
        // Verificar ou criar categoria principal
        if (product.getMainCategory() != null) {
            Category mainCategory = categoryService.findOrCreateCategory(product.getMainCategory().getName());
            product.setMainCategory(mainCategory);
        }

        // Verificar ou criar subcategorias
        if (product.getSubcategories() != null && !product.getSubcategories().isEmpty()) {
            List<Category> subcategories = product.getSubcategories().stream()
                    .map(subcategory -> categoryService.findOrCreateCategory(subcategory.getName()))
                    .collect(Collectors.toList());
            product.setSubcategories(subcategories);
        }

        return productRepository.save(product);
    }

    public boolean removeProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        if (productRepository.existsById(id)) {
            updatedProduct.setId(id);
            return productRepository.save(updatedProduct);
        }
        return null;
    }

    public void unlinkProductsFromBrand(Long brandId) {
        List<Product> products = productRepository.findByBrandId(brandId);
        for (Product product : products) {
            product.setBrand(null);
            productRepository.save(product);
        }
    }
}
