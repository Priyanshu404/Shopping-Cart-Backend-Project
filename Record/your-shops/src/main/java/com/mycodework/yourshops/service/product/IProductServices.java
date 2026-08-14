package com.mycodework.yourshops.service.product;

import com.mycodework.yourshops.dto.ProductDto;
import com.mycodework.yourshops.model.Product;
import com.mycodework.yourshops.request.AddProductRequest;
import com.mycodework.yourshops.request.ProductUpdateRequest;

import java.util.List;

public interface IProductServices {
    Product addProduct(AddProductRequest product);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest product, Long productId);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category,String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductByBrandAndName(String brand,String name);

    List<ProductDto> getConvertedProducts(List<Product> products);

    Long countProductsByBrandAndName(String brand, String name);

    ProductDto convertToDto(Product product);
}
