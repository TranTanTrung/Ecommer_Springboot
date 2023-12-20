package com.ecommerce.library.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "productReview")
public class ProductReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private int rating;

    @Column(columnDefinition = "LONGTEXT")
    @Lob
    private String comment;
}
