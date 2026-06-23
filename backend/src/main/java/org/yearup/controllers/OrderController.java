package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yearup.models.User;
import org.yearup.service.OrderService;
import org.yearup.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ROLE_USER')")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    /**
     * Converts the current user's shopping cart into an order.
     * Returns 201 if successful.
     * Returns 400 if the cart is empty.
     */
    @PostMapping
    public ResponseEntity<Void> checkout(Principal principal){
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        orderService.checkout(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
