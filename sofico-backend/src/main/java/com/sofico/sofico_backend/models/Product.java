package com.sofico.sofico_backend.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double price;

    @Column(nullable = false)
    private Boolean highlighted = false;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "main_category_id")
    private Category mainCategory; // Categoria principal

    @ManyToMany
    @JoinTable(name = "product_subcategories", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "subcategory_id"))
    private List<Category> subcategories = new ArrayList<>();

    public List<Category> getSubcategories() {
        return subcategories;
    }

    private String color; // Cor do produto

    @ElementCollection
    @CollectionTable(name = "product_sizes", joinColumns = @JoinColumn(name = "product_id"))
    @MapKeyColumn(name = "size")
    @Column(name = "quantity")
    private Map<String, Integer> sizes = new HashMap<>(); // Tamanhos e quantidades

    private Double discountValue;
    private Boolean discountPercentage;

    @Transient
    private Double finalPrice;

    public Double getFinalPrice() {
        if (discountPercentage != null && discountPercentage && discountValue != null) {
            finalPrice = price - (price * discountValue / 100);
        } else if (discountValue != null) {
            finalPrice = price - discountValue;
        } else {
            finalPrice = price;
        }
        return finalPrice;
    }
}