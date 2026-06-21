package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Category;
import org.yearup.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService
{
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository)
    {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories()
    {
        // get all categories
        return categoryRepository.findAll();
    }

    public Optional<Category> getById(int categoryId)
    {
        // get category by id
        return categoryRepository.findById(categoryId);
    }

    public Category create(Category category)
    {
        // create a new category
        return categoryRepository.save(category);
    }

    public Optional<Category> update(int categoryId, Category category)
    {
        Optional<Category> exitingCategory = getById(categoryId);
        // update category and return the updated category
        if (exitingCategory.isEmpty()) {
            return Optional.empty();
        }
        // To Unwarp is from Optional
        Category exiting = exitingCategory.get();
        exiting.setDescription(category.getDescription());
        exiting.setName(category.getName());
        return Optional.of(categoryRepository.save(exiting));
    }

    public void delete(int categoryId)
    {
        // delete category
        categoryRepository.deleteById(categoryId);
    }
}
