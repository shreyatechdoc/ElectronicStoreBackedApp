package com.lcwd.electronic.store.entities;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderItemId;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int totalPrice;

    private int Quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
