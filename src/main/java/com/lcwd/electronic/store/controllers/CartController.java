package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    CartService cartService;

// add items into cart
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemInToCart(@PathVariable String userId, @RequestBody AddItemToCartRequest addItemToCartRequest)
    {
        CartDto cartDto=cartService.addItemToCart(userId, addItemToCartRequest);
        return new ResponseEntity<>(cartDto, HttpStatus.OK) ;
    }

    // remove items from cart
    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ApiResponseMessage> removeItemInToCart(@PathVariable String userId,@PathVariable int itemId)
    {
        cartService.removeItemFromCart(userId,itemId);
        ApiResponseMessage responseMessage= ApiResponseMessage.builder()
                .message("Item is removed").status(HttpStatus.OK)
                .success(true).build();
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

    // clear cart
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId)
    {
        cartService.clearCart(userId);
        ApiResponseMessage responseMessage= ApiResponseMessage.builder()
                .message("cart is clear")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

    // get cart by particular User
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> GetCart(@PathVariable String userId)
    {
        CartDto cartDto=cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK) ;
    }


}
