package com.mycodework.yourshops.service.image;

import com.mycodework.yourshops.dto.ImageDto;
import com.mycodework.yourshops.exceptions.ResourcesNotFoundException;
import com.mycodework.yourshops.model.Image;
import com.mycodework.yourshops.model.Product;
import com.mycodework.yourshops.service.product.IProductServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServices implements IImageServices {

    private final ImageReposiotry imageReposiotry;
    private final IProductServices productServices;
    @Override
    public Image getImagedById(Long id) {
        return imageReposiotry.findById(id).orElseThrow(()
                -> new ResourcesNotFoundException("Image not found with id: " +id));
    }

    @Override
    public void deleteImageByID(Long id) {
       imageReposiotry.findById(id).ifPresentOrElse(imageReposiotry
               :: delete,() ->{
            throw new ResourcesNotFoundException("Image not found with id: " +id);
       });
    }

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
        Product product = productServices.getProductById(productId);
        List<ImageDto> savedImageDto = new ArrayList<>();
        for (MultipartFile file : files){
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buildDownloadUrl = "/api/v1/images/image/download/";
                String downloadUrl = buildDownloadUrl+image.getId();
                image.setDownloadUrl(downloadUrl);
               Image savedImage= imageReposiotry.save(image);
               savedImage.setDownloadUrl(buildDownloadUrl+savedImage.getId());
               imageReposiotry.save(savedImage);

               ImageDto imageDto = new ImageDto();
               imageDto.setId(savedImage.getId());
               imageDto.setFileName(savedImage.getFileName());
               imageDto.setDownloadUrl(savedImage.getDownloadUrl());
               savedImageDto.add(imageDto);

            } catch (IOException|SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
     Image image = getImagedById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageReposiotry.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
