package com.lcwd.electronic.store.dtos;


import com.lcwd.electronic.store.entities.OrderItem;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CreateOrderRequest {

    @NotBlank(message = "cart id is requried !!")
    private String cartId;
    @NotBlank(message = "user id is requried !!")
    private String userId;
    private String orderStatus="PENDING";
    private String paymentStatus="NOTPAID";
    @NotBlank(message = "billingAddress is requried !!")
    private String billingAddress;
    @NotBlank(message = "billingPhone is requried !!")
    private String billingPhone;
    @NotBlank(message = "billingName is requried !!")
    private String billingName;




}
