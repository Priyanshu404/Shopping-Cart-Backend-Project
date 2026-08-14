package com.mycodework.yourshops.service.category;

import com.mycodework.yourshops.exceptions.AlreadyExistingException;
import com.mycodework.yourshops.exceptions.ResourcesNotFoundException;
import com.mycodework.yourshops.model.Category;
import com.mycodework.yourshops.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryServices{
    private final CategoryRepository categoryRepository;
    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException
                        ("Categories not found!"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(c ->
                !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository :: save).orElseThrow(() ->
                        new AlreadyExistingException
                                (category.getName()+"Category already exists!"));
    }

    @Override
    public Category updateCategory(Category category,Long id) {
        return Optional.ofNullable(getCategoryById(id))
                .map(oldCategory -> {
                    oldCategory.setName(category.getName());
                    return categoryRepository.save(oldCategory);
                }).orElseThrow(() -> new ResourcesNotFoundException("Category not found!"));
    }

    @Override
    public void deleteCategoryById(Long id) {
       categoryRepository.findById(id)
               .ifPresentOrElse(categoryRepository::delete, () -> {
                   throw new ResourcesNotFoundException("Category not found!");
               });

    }
}
