package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.entities.*;
import com.lcwd.electronic.store.exceptions.BadApiRequestException;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CartRepository;
import com.lcwd.electronic.store.repositories.OrderRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {

        // fetching userId & cartId from createorderRequest class
        String userId=orderDto.getUserId();
        String cartId=orderDto.getCartId();

        // fetch user
        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("given userId is not found !!"));

        //fetch cart
        Cart cart=cartRepository.findById(cartId).orElseThrow(()->new ResourceNotFoundException("Given cardId is not found !!"));
        List<CartItem> cartItems = cart.getItems();

        // check items
        if(cartItems.size()<=0)
        {
            throw new BadApiRequestException("Invalid number of items in cart !!");
        }

        // set cart  details into order
        Order order=Order.builder()
                .billingAddress(orderDto.getBillingAddress())
                .orderDate(new Date())
                .orderStatus(orderDto.getOrderStatus())
                .billingPhone(orderDto.getBillingPhone())
                .deliveredDate(null)
                .paymentStatus(orderDto.getPaymentStatus())
                .orderId(UUID.randomUUID().toString())
                .billingName(orderDto.getBillingName())
                .user(user)
                .build();

        // pass cartItmes into orderItems
        AtomicReference<Integer>orderamount=new AtomicReference<>(0);
        List<OrderItem> orderItems=cartItems.stream().map(cartItem -> {

            OrderItem orderItem=OrderItem.builder()
                    .Quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity()*cartItem.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();
            orderamount.set(orderamount.get()+orderItem.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        order.setOrderAmount(orderamount.get());

        // clear cart
        cart.getItems().clear();
        cartRepository.save(cart);

        Order savedOrder=orderRepository.save(order);

        return modelMapper.map(savedOrder,OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {

        Order order=orderRepository.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Given Id is not found !!"));
        orderRepository.delete(order);
        ApiResponseMessage apiResponseMessage=ApiResponseMessage.builder()
                .message("Order is remove successfully !!")
                .status(HttpStatus.OK)
                .success(true)
                .build();
    }

    @Override
    public List<OrderDto> getOrdersByUser(String userId) {

        User user=userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("Given user is not found !!"));
        List<Order> orders=orderRepository.findByUser(user);
        List<OrderDto>orderDtos=orders.stream().map(order -> modelMapper.map(order,OrderDto.class)).collect(Collectors.toList());

        return orderDtos;
    }

    @Override
    public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Order> page=orderRepository.findAll(pageable);

        return Helper.getPageableResponse(page,OrderDto.class);

    }


}
