package com.mycodework.yourshops.service.image;

import com.mycodework.yourshops.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageReposiotry extends JpaRepository<Image, Long> {
    List<Image> findByProductId(Long id);
}
