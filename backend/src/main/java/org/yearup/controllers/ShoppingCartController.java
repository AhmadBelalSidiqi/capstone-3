package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;
import org.yearup.service.ShoppingCartService;
import org.yearup.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/cart")
@PreAuthorize("hasRole('ROLE_USER')")
@CrossOrigin(origins = "*")
public class ShoppingCartController
{
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService, UserService userService) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }

    /***
     * Only users can access this method
     * @return User's shopping cart
     */
    @GetMapping
    public ShoppingCart getCart(Principal principal)
    {
        // get the currently logged-in username
        int userId = getCurrentUserId(principal);
        return shoppingCartService.getByUserId(userId);
    }

    /***
     * Only users can access this method,it adds a product to the userCart
     * @return usersShoppingCart
     */
    @PostMapping("/products/{productId}")
    public ResponseEntity<ShoppingCart> addProduct(@PathVariable int productId, Principal principal){
        //Get users id to add product in their cart
        int userId = getCurrentUserId(principal);
        ShoppingCart userShoppingCart = shoppingCartService.addProduct(userId , productId);

        return ResponseEntity.status(HttpStatus.CREATED).body(userShoppingCart);

    }


    /**
     * Updates the quantity of a product in the user's cart.
     */
    @PutMapping("/products/{productId}")
    public ResponseEntity<ShoppingCart> updateProductQuantity (@PathVariable int productId, @RequestBody ShoppingCartItem item, Principal principal){
        //Get users id to update product quantity in their cart
        int userId = getCurrentUserId(principal);
        ShoppingCart userShoppingCart  = shoppingCartService.updateProduct(userId,item, productId);
        return ResponseEntity.ok(userShoppingCart);
    }


    /***
     * Only users can access this method
     * @return empty ShoppingCart
     */
    @DeleteMapping
    public ResponseEntity<ShoppingCart> clearCart(Principal principal){
        //Get users id to deleteAll product in their cart
        int userId = getCurrentUserId(principal);
        shoppingCartService.deleteAllProducts(userId);
        ShoppingCart userShoppingCart = shoppingCartService.getByUserId(userId);
        return ResponseEntity.ok(userShoppingCart);
    }

    private int getCurrentUserId(Principal principal) {
        String username = principal.getName();
        User user = userService.getByUserName(username);
        return user.getId();
    }

}
