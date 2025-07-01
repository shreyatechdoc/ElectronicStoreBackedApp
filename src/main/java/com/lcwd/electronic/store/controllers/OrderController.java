package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest)
    {
        OrderDto orderDto1=orderService.createOrder(createOrderRequest);
        return new ResponseEntity<>(orderDto1, HttpStatus.CREATED);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId)
    {
        orderService.removeOrder(orderId);
        ApiResponseMessage responseMessage= ApiResponseMessage.builder()
                .message("order is remove")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<OrderDto>> getAllOrderByUser(@PathVariable String userId)
    {
        List<OrderDto>getOrders=orderService.getOrdersByUser(userId);
        return new ResponseEntity<>(getOrders,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageableResponse<OrderDto>> getAllOrders(
            @RequestParam (value = "pageNumber",defaultValue ="0",required = false ) int pageNumber,
            @RequestParam (value = "pageSize", defaultValue = "10",required = false) int pageSize,
            @RequestParam (value = "sortDir",defaultValue = "asc",required = false) String sortDir,
            @RequestParam (value = "sortBy",defaultValue = "orderId",required = false) String sortBy
    )
    {
        PageableResponse<OrderDto> getallorders=orderService.getOrders(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(getallorders,HttpStatus.OK) ;
    }



}
