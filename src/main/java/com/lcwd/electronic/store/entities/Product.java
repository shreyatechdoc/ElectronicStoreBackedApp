package com.lcwd.electronic.store.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "products")
public class Product {

    @Id
    private String productId;
    private String title;
    @Column(length = 10000)
    private String description;
    private  int price;
    private int quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private  int discountedPrice;
    private String productImageName;

    @ManyToOne(fetch = FetchType.EAGER)
    //create column and join to product table
    @JoinColumn(name = "category_id")
    private Category category;
}
