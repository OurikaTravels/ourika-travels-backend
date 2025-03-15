package com.ouchin.ourikat.service;

import com.ouchin.ourikat.dto.request.TrekImageResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TrekImageService {
    TrekImageResponse addImage(Long trekId, MultipartFile file, Boolean isPrimary);
    List<TrekImageResponse> getImagesByTrekId(Long trekId);
    void deleteImage(Long imageId) throws BadRequestException;
    TrekImageResponse togglePrimaryStatus(Long imageId);
    void validateTrekImages(Long trekId) throws BadRequestException;

}



//TODO : insert multi images to trek with validation .                                                                                                                                                                                                                          