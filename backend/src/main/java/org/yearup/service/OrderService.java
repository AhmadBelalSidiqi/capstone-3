package org.yearup.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.*;
import org.yearup.repository.OrderLineItemsRepository;
import org.yearup.repository.OrderRepository;
import org.yearup.repository.ShoppingCartRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderLineItemsRepository orderLineItemsRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProfileService profileService;
    private final ProductService productService;
    @Autowired
    public OrderService(OrderRepository orderRepository,
                        OrderLineItemsRepository orderLineItemsRepository,
                        ShoppingCartRepository shoppingCartRepository,
                        ProfileService profileService,
                        ProductService productService) {
        this.orderRepository = orderRepository;
        this.orderLineItemsRepository = orderLineItemsRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.profileService = profileService;
        this.productService = productService;
    }

    /**
     * Converts a user's shopping cart into an order.
     * Steps:
     * 1. Validate cart is not empty
     * 2. Create order
     * 3. Create order line items
     * 4. Clear the shopping cart
     */
    @Transactional
    public void checkout( int userId ){
        Profile userProfile = profileService.getProfileByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Profile not found"));
        List<CartItem> userCartItem = shoppingCartRepository.findByUserId(userId);
        if (userCartItem.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is Empty ");
        Order order = new Order();
        order.setUserID(userProfile.getUserId());
        order.setDateTime(LocalDateTime.now());
        order.setAddress(userProfile.getAddress());
        order.setCity(userProfile.getCity());
        order.setState(userProfile.getState());
        order.setZip(userProfile.getZip());
        order.setShippingAmount(0);

        Order savedOrder = orderRepository.save(order);

        for(CartItem item : userCartItem){
            Product product = productService.getById(item.getProductId());
            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setOrderId(savedOrder.getOrderID());
            orderLineItem.setProductId(item.getProductId());
            orderLineItem.setSalesPrice(product.getPrice());
            orderLineItem.setQuantity(item.getQuantity());
            orderLineItemsRepository.save(orderLineItem);

        }

        shoppingCartRepository.deleteByUserId(userId);

    }
}
