package com.lcwd.electronic.store.dtos;


import com.lcwd.electronic.store.entities.Order;
import com.lcwd.electronic.store.entities.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {

    private int orderItemId;

    private Product product;

    private int totalPrice;

    private int Quantity;


    
}
