package com.brasilburger.services.interfaces;

import java.io.File;

public interface IImageService {
    String uploadImage(File imageFile, String folder);
    boolean deleteImage(String imageUrl);
    String getImageUrl(String publicId);
}