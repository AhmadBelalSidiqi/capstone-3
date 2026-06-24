package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.yearup.service.CategoryService;
import org.yearup.service.ProductService;

import java.util.List;


@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "*")
public class CategoryController {
    private final CategoryService categoryService;
    private final ProductService productService;


    @Autowired
    public CategoryController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }


    /***
     * Retrieves all categories.
     * Accessible by all users.
     */
    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }


    /**
     * Retrieves a category by ID.
     * Returns 200 if found, otherwise 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable int id) {
        return ResponseEntity.ok(categoryService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }


    /***
     * Everyone can access this method
     * @return list of products by categoryId
     */
    @GetMapping("{categoryId}/products")
    public ResponseEntity<List<Product>> getProductsById(@PathVariable int categoryId) {
        return ResponseEntity.ok(productService.listByCategoryId(categoryId));
    }

    /***
     * Only ADMINS can access this method
     * @return newCreated category
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.create(category));
    }

    /***
     * Only ADMIN can access this method
     * Response Code (200) if category not found (404)
     * @return new updated category
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Category> updateCategory(@PathVariable int id, @RequestBody Category category) {
        return ResponseEntity.ok(categoryService.update(id, category));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        categoryService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
