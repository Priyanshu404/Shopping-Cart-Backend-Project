package com.mycodework.yourshops.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    private String name;


    @JsonIgnore // avoid infinite recursion when serializing Category -> Product -> Category
    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();


    public Category(String name) {

        this.name = name;
    }
}
