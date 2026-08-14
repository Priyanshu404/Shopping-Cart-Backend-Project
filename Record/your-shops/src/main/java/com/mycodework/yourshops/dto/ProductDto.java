package com.mycodework.yourshops.dto;

import com.mycodework.yourshops.model.Category;
import com.mycodework.yourshops.model.Image;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int quantity;
    private String description;


    private Category category;

    private List<ImageDto> images;
}
