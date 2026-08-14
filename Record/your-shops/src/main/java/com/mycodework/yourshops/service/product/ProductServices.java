package com.mycodework.yourshops.service.product;

import com.mycodework.yourshops.dto.ImageDto;
import com.mycodework.yourshops.dto.ProductDto;
import com.mycodework.yourshops.exceptions.AlreadyExistingException;
import com.mycodework.yourshops.exceptions.ProductsNotFoundException;
import com.mycodework.yourshops.model.Category;
import com.mycodework.yourshops.model.Image;
import com.mycodework.yourshops.model.Product;
import com.mycodework.yourshops.repository.CategoryRepository;
import com.mycodework.yourshops.repository.ProductRepository;
import com.mycodework.yourshops.request.AddProductRequest;
import com.mycodework.yourshops.request.ProductUpdateRequest;
import com.mycodework.yourshops.service.image.ImageReposiotry;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProductServices implements IProductServices {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageReposiotry imageReposiotry;


    @Override
    public Product addProduct(AddProductRequest request) {
        //check if the category is found in db
        // if yes ,set it as the new product category
        //if no,then save it as a new category
        // Then set as the new product category

        if(productExists(request.getName(),request.getBrand())){
            throw new AlreadyExistingException(request.getBrand() +" "
            +request.getName()+" already exists,you may update this product instead");
        }
        Category category = Optional.ofNullable(categoryRepository
                .findByName(request.getCategory().getName()))
                        .orElseGet(() -> {
                            Category newCategory = new Category
                                    (request.getCategory().getName());
                            return categoryRepository.save(newCategory);
                        });

        request.setCategory(category);
        return productRepository.save(createProduct(request,category));
    }

    private boolean productExists(String name,String brand){
        return productRepository.existsByNameAndBrand(name,brand);
    }




    private Product createProduct(AddProductRequest request,
                                  Category category)
    {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getQuantity(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.
                findById(id).orElseThrow(() -> new ProductsNotFoundException("Product not found!"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).
                ifPresentOrElse(productRepository::delete,
                        () ->{ throw new ProductsNotFoundException("Product not found");});
    }

    @Override
    public Product updateProduct(ProductUpdateRequest product, Long productId) {
      return productRepository.findById(productId)
              .map(existingProduct -> updateExistingProduct(existingProduct, product))
              .map(productRepository::save)
              .orElseThrow(() -> new ProductsNotFoundException("Product not found!"));
    }

    private Product updateExistingProduct(Product existingProduct
            , ProductUpdateRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setQuantity(request.getQuantity());
        existingProduct.setDescription(request.getDescription());

        Category category= categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand,name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream()
                .map(this::convertToDto)
                .toList();
    }
    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand,name);
    }
    @Override
    public ProductDto convertToDto(Product product){
        ProductDto productDto= modelMapper.map(product, ProductDto.class);
        List<Image> images = imageReposiotry.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class)).toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
}
