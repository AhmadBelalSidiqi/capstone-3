# 🛒 E-Commerce Backend API

A Spring Boot REST API for an e-commerce application that enables users to browse products, manage a shopping cart, and complete checkout. This project follows clean architecture principles and demonstrates real-world backend development practices.

---

## 🚀 Features

- ✅ Product search with filtering (category, price range, featured items)
- ✅ Category management (Admin only)
- ✅ Product management (Admin only)
- ✅ Shopping cart functionality for users
- ✅ Checkout system that converts cart into an order
- ✅ Order and order line item creation
- ✅ User profile management
- ✅ Role-based authorization (User / Admin)
- ✅ Proper validation and HTTP response handling

---

## 🧱 Tech Stack

- **Java 17+**
- **Spring Boot**
- **Spring Security**
- **Spring Data JPA (Hibernate)**
- **MySQL**
- **Maven**
---

## 📂 Project Structure

src/main/java/org/yearup
│
├── controllers     # REST API endpoints
├── service         # Business logic
├── repository      # Database access layer (JPA)
├── models          # Entity classes

---

## 🔐 Security

This application uses **Spring Security** with role-based authorization:

- `ROLE_USER`
    - View products
    - Manage shopping cart
    - Checkout
    - Manage profile

- `ROLE_ADMIN`
    - Create, update, delete products
    - Manage categories

---

## 📦 API Endpoints

### 🔹 Products

| Method | Endpoint         | Description            |
|--------|------------------|------------------------|
| GET    | `/products`      | Search/filter products |
| GET    | `/products/{id}` | Get product by ID      |
| POST   | `/products`      | Create product (Admin) |
| PUT    | `/products/{id}` | Update product (Admin) |
| DELETE | `/products/{id}` | Delete product (Admin) |

---

### 🔹 Categories

| Method | Endpoint                    | Description              |
|--------|-----------------------------|--------------------------|
| GET    | `/categories`               | Get all categories       |
| GET    | `/categories/{id}`          | Get category by ID       |
| GET    | `/categories/{id}/products` | Get products by category |
| POST   | `/categories`               | Create category (Admin)  |
| PUT    | `/categories/{id}`          | Update category (Admin)  |
| DELETE | `/categories/{id}`          | Delete category (Admin)  |

---

### 🔹 Shopping Cart

| Method | Endpoint                     | Description             |
|--------|------------------------------|-------------------------|
| GET    | `/cart`                      | Retrieve user's cart    |
| POST   | `/cart/products/{productId}` | Add product to cart     |
| PUT    | `/cart/products/{productId}` | Update product quantity |
| DELETE | `/cart`                      | Clear cart              |

---

### 🔹 Orders / Checkout

| Method | Endpoint  | Description               |
|--------|-----------|---------------------------|
| POST   | `/orders` | Checkout and create order |

---

### 🔹 Profile

| Method | Endpoint   | Description      |
|--------|------------|------------------|
| GET    | `/profile` | Get user profile |
| PUT    | `/profile` | Update profile   |

---

## 🧠 Key Concepts Implemented

### ✅ Proper Update Handling
- Ensures entities exist before updating
- Returns `404 Not Found` if resource does not exist
- Avoids unsafe direct saves

### ✅ Transaction Management
- Uses `@Transactional` for checkout
- Ensures all database operations succeed or rollback together

### ✅ Validation

- Prevents checkout with an empty cart
- Uses appropriate HTTP status codes:
    - `200 OK`
    - `201 Created`
    - `400 Bad Request`
    - `404 Not Found`


---

## 🛒 Checkout Flow

1. User sends `POST /orders`
2. System retrieves user's shopping cart
3. Validates cart is not empty
4. Creates a new order
5. Creates order line items for each cart item
6. Clears the shopping cart
7. All steps run inside a **transaction**

---

## ⚠️ Error Handling

- `404 Not Found` → resource does not exist
- `400 Bad Request` → invalid request (e.g., empty cart)
- Uses `ResponseStatusException` for clear API responses


---

## ⭐ Favorite Piece of Code

One of the most important parts of this project is the **checkout logic in the service layer**.


```java  
  @Transactional
    public void checkout(int userId) {
        Profile userProfile = profileService.getProfileByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));
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

        for (CartItem item : userCartItem) {
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
```

### 📌 Why this code matters

- ✅ Uses **`@Transactional`** to ensure all operations succeed or fail together
- ✅ Validates that the **cart is not empty before processing**
- ✅ Ensures **data consistency** in database operations


---
## 👨‍💻 Author

**Ahmad Belal Sidiqi**

---

## ✅ Summary

This project demonstrates:

- RESTful API design
- Secure backend development
- Transactional business logic
- Clean and maintainable architecture

---
