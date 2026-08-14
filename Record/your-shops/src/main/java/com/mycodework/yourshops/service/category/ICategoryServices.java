package com.mycodework.yourshops.service.category;

import com.mycodework.yourshops.model.Category;

import java.util.List;

public interface ICategoryServices {

    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    List<Category> getAllCategories();
    Category addCategory(Category category);
    Category updateCategory(Category category, Long id);
    void deleteCategoryById(Long id);
}
