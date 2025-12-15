package com.brasilburger.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class CloudinaryConfig {
    private static Cloudinary instance;
    
    public static Cloudinary getInstance() {
        if (instance == null) {
            instance = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", EnvConfig.get("CLOUDINARY_CLOUD_NAME"),
                "api_key", EnvConfig.get("CLOUDINARY_API_KEY"),
                "api_secret", EnvConfig.get("CLOUDINARY_API_SECRET")
            ));
            System.out.println("✅ Cloudinary configuré!");
        }
        return instance;
    }
}