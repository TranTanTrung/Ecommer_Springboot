package com.ecommerce.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "image", "image1", "image2", "image3", "image4"}))
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;
    private String name;
    @Column(columnDefinition = "LONGTEXT")
    @Lob
    private String description;
    private int currentQuantity;
    private double costPrice;
    private double salePrice;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String image;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String image1;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String image2;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String image3;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String image4;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;
    private boolean is_activated;
    private boolean is_deleted;

}
