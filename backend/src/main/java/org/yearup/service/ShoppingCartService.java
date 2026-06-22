package org.yearup.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.yearup.models.CartItem;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.repository.ShoppingCartRepository;

import java.util.List;

@Service
public class ShoppingCartService
{
    // a shopping cart is built from cart rows plus a product lookup for each row
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService)
    {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
    }

    public ShoppingCart getByUserId(int userId)
    {
        // load the user's cart rows, look up each product, and build the ShoppingCart
       List<CartItem> userCartItem = shoppingCartRepository.findByUserId(userId);
        ShoppingCart userShoppingCart = new ShoppingCart();
        for (CartItem item: userCartItem){
            ShoppingCartItem usersShoppingCartItem = new ShoppingCartItem();
            Product product = productService.getById(item.getProductId());
            usersShoppingCartItem.setProduct(product);
            usersShoppingCartItem.setQuantity(item.getQuantity());
            userShoppingCart.add(usersShoppingCartItem);
        }

        return userShoppingCart;
    }

    public ShoppingCart addProduct(int userId, int productId) {
      CartItem product = new CartItem();
      product.setUserId(userId);
      product.setProductId(productId);
      shoppingCartRepository.save(product);
      return getByUserId(userId);
    }

    public void deleteAllProducts(int userId) {
        shoppingCartRepository.deleteByUserId(userId);
    }

    // add additional methods here
}
