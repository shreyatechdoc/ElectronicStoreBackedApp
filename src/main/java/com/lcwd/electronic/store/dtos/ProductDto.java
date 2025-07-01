package com.lcwd.electronic.store.dtos;


import com.lcwd.electronic.store.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private String productId;
    private String title;
    private String description;
    private  int price;
    private int quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private  int discountedPrice;
    private String productImageName;
    private CategoryDto category;

}
