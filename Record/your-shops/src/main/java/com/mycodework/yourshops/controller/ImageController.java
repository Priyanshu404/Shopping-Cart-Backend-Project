package com.mycodework.yourshops.controller;

import com.mycodework.yourshops.dto.ImageDto;
import com.mycodework.yourshops.exceptions.ResourcesNotFoundException;
import com.mycodework.yourshops.model.Image;
import com.mycodework.yourshops.response.ApiResponse;
import com.mycodework.yourshops.service.image.IImageServices;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {
    private final IImageServices imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImage(List<MultipartFile> files,
                                                 @RequestParam Long productId){
        try {
            List<ImageDto> imageDtos = imageService.saveImages(files,productId);
            return ResponseEntity.ok(new ApiResponse("Upload success!"
                    , imageDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to upload images",e.getMessage()));
        }
    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
        Image image = imageService.getImagedById(imageId);
        ByteArrayResource resource = new ByteArrayResource
                (image.getImage()
                        .getBytes(1
                                ,(int) image.getImage().length()));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + image.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .body(resource);
    }

    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId,@RequestBody MultipartFile file)
    {
        Image image = imageService.getImagedById(imageId);
        if (image != null){
            try {
                imageService.updateImage(file,imageId);
                return ResponseEntity.ok(new ApiResponse("Image updated successfully", null));
            } catch (ResourcesNotFoundException e) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
            }

        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Failed to updateimage", INTERNAL_SERVER_ERROR));
    }

    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId)
    {
        Image image = imageService.getImagedById(imageId);
        if (image != null){
            try {
                imageService.deleteImageByID(imageId);
                return ResponseEntity.ok(new ApiResponse("Image deleted successfully", null));
            } catch (ResourcesNotFoundException e) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
            }

        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Failed to delete image", INTERNAL_SERVER_ERROR));
    }
}
