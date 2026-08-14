package com.mycodework.yourshops.service.image;

import com.mycodework.yourshops.dto.ImageDto;
import com.mycodework.yourshops.model.Image;
import com.mycodework.yourshops.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageServices {
    Image getImagedById(Long id);
    void deleteImageByID(Long id);
    List<ImageDto> saveImages(List<MultipartFile> files, Long productId);
    void updateImage(MultipartFile file,Long imageId);
}
