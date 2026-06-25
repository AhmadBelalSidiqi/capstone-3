package org.yearup.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.CartItem;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.repository.ShoppingCartRepository;

import java.util.List;

@Service
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
    }

    public ShoppingCart getByUserId(int userId) {
        // load the user's cart rows, look up each product, and build the ShoppingCart
        List<CartItem> cartItems = shoppingCartRepository.findByUserId(userId);
        ShoppingCart shoppingCart = new ShoppingCart();
        for (CartItem item : cartItems) {
            ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
            Product product = productService.getById(item.getProductId());
            shoppingCartItem.setProduct(product);
            shoppingCartItem.setQuantity(item.getQuantity());
            shoppingCart.add(shoppingCartItem);
        }
        return shoppingCart;
    }

    public ShoppingCart addProduct(int userId, int productId) {
        CartItem cartItem = new CartItem();
        cartItem.setUserId(userId);
        cartItem.setProductId(productId);
        CartItem existingItem = shoppingCartRepository.findByUserIdAndProductId(userId, productId);

        if (existingItem == null) shoppingCartRepository.save(cartItem);
        else {
            // increase the quantity if the user already have the product
            int quantity = existingItem.getQuantity();
            existingItem.setQuantity(++quantity);
            shoppingCartRepository.save(existingItem);
        }
        return getByUserId(userId);
    }

    @Transactional
    public void deleteAllProducts(int userId) {
        shoppingCartRepository.deleteByUserId(userId);
    }

    public ShoppingCart updateProduct(int userId, ShoppingCartItem item, int productId) {
        CartItem existingItem = shoppingCartRepository.findByUserIdAndProductId(userId, productId);
        if (existingItem == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not found");
        existingItem.setQuantity(item.getQuantity());
        shoppingCartRepository.save(existingItem);
        return getByUserId(userId);
    }
}
