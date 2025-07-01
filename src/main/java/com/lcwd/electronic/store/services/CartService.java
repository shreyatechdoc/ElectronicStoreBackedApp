package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.CartDto;

public interface CartService {

    // Add items to cart
    // case 1: cart for user is not available we will create cart and add cartitems into it.
    // case 2: cart available add the items to cart

    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    // removing item from cart
    void removeItemFromCart(String userId,int cartItem);

    // removing all items from cart
    void clearCart(String userId);

    // fetch particular user cart
    CartDto getCartByUser(String userId);
}
