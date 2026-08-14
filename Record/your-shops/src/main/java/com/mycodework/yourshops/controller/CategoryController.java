package com.mycodework.yourshops.controller;

import com.mycodework.yourshops.exceptions.AlreadyExistingException;
import com.mycodework.yourshops.exceptions.ResourcesNotFoundException;
import com.mycodework.yourshops.model.Category;
import com.mycodework.yourshops.response.ApiResponse;
import com.mycodework.yourshops.service.category.ICategoryServices;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final ICategoryServices categoryServices;

    @GetMapping("/all/")
    public ResponseEntity<ApiResponse> getAllCategories(){
        try {
            List<Category> categories = categoryServices.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("found!",categories));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error", INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/add/")
    public ResponseEntity<ApiResponse> addCategoryById(@RequestBody Category name){
        try {
            Category thecategory = categoryServices.addCategory(name);
            return ResponseEntity.ok(new ApiResponse("Category added successfully",thecategory));
        } catch (AlreadyExistingException e) {
            return ResponseEntity.status(CONFLICT)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/category/{id}/category")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id){
        try {
            Category thecategory = categoryServices.getCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Category found!",thecategory));
        } catch (ResourcesNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
    @GetMapping("/category/{name}/category")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name){
        try {
            Category thecategory = categoryServices.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("Category found!",thecategory));
        } catch (ResourcesNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id){
        try {
             categoryServices.deleteCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Category deleted successfully!",null));
        } catch (ResourcesNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
     @PutMapping("/category/{id}/update")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id, @RequestBody Category category){
        try {
            Category updatedCategory = categoryServices.updateCategory(category,id);
            return ResponseEntity.ok(new ApiResponse("Category updated successfully!",updatedCategory));
        } catch (ResourcesNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
}
