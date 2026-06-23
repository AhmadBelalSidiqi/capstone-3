package org.yearup.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    @Transactional
    public void checkout(User user){
        Profile userProfile = profileService.getProfileByUserId(user.getId()).orElseThrow();
        Order order = new Order();
        order.setUserID(userProfile.getUserId());
        order.setDateTime(LocalDateTime.now());
        order.setAddress(userProfile.getAddress());
        order.setCity(userProfile.getCity());
        order.setState(userProfile.getState());
        order.setZip(userProfile.getZip());
        order.setShippingAmount(0);

       Order savedOrder = orderRepository.save(order);

        List<CartItem> userCartItem = shoppingCartRepository.findByUserId(user.getId());
        for(CartItem item : userCartItem){
            Product product = productService.getById(item.getProductId());
            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setOrderId(savedOrder.getOrderID());
            orderLineItem.setProductId(item.getProductId());
            orderLineItem.setSalesPrice(product.getPrice());
            orderLineItem.setQuantity(item.getQuantity());
            orderLineItemsRepository.save(orderLineItem);

        }

        shoppingCartRepository.deleteByUserId(user.getId());

    }
}
