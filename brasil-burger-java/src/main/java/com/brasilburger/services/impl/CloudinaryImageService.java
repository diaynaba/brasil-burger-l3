package com.brasilburger.services.impl;

import com.brasilburger.config.CloudinaryConfig;
import com.brasilburger.config.EnvConfig;
import com.brasilburger.services.interfaces.IImageService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.util.Map;

public class CloudinaryImageService implements IImageService {
    private final Cloudinary cloudinary;
    private final String baseFolder;
    
    public CloudinaryImageService() {
        this.cloudinary = CloudinaryConfig.getInstance();
        this.baseFolder = EnvConfig.get("CLOUDINARY_FOLDER", "brasil-burger");
    }
    
    @Override
    public String uploadImage(File imageFile, String folder) {
        if (imageFile == null || !imageFile.exists()) {
            throw new IllegalArgumentException("Le fichier image n'existe pas");
        }
        
        if (!imageFile.isFile()) {
            throw new IllegalArgumentException("Le chemin ne pointe pas vers un fichier");
        }
        
        
        long fileSizeInMB = imageFile.length() / (1024 * 1024);
        if (fileSizeInMB > 10) {
            throw new IllegalArgumentException("L'image est trop volumineuse (max 10MB)");
        }
        
       
        String fileName = imageFile.getName().toLowerCase();
        if (!fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg") && 
            !fileName.endsWith(".png") && !fileName.endsWith(".gif")) {
            throw new IllegalArgumentException("Format d'image non supporté. Utilisez JPG, PNG ou GIF");
        }
        
        try {
            String fullFolder = baseFolder + "/" + folder;
            
            System.out.println("📤 Upload de l'image vers Cloudinary...");
            
           
            Map uploadResult = cloudinary.uploader().upload(imageFile, 
                ObjectUtils.asMap(
                    "folder", fullFolder,
                    "resource_type", "image",
                    "overwrite", true
                )
            );
            
            String imageUrl = (String) uploadResult.get("secure_url");
            System.out.println("✅ Image uploadée avec succès!");
            System.out.println("   URL: " + imageUrl);
            
            return imageUrl;
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'upload: " + e.getMessage());
            throw new RuntimeException("Erreur lors de l'upload de l'image: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean deleteImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return false;
        }
        
        try {
            String publicId = extractPublicId(imageUrl);
            
            if (publicId == null || publicId.isEmpty()) {
                System.err.println("⚠️ Impossible d'extraire le public_id de l'URL");
                return false;
            }
            
            System.out.println("🗑️ Suppression de l'image: " + publicId);
            
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            String status = (String) result.get("result");
            
            if ("ok".equals(status)) {
                System.out.println("✅ Image supprimée avec succès");
                return true;
            } else {
                System.err.println("⚠️ Suppression échouée: " + status);
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la suppression: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public String getImageUrl(String publicId) {
        if (publicId == null || publicId.isEmpty()) {
            return null;
        }
        
        try {
            return cloudinary.url().generate(publicId);
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la génération de l'URL: " + e.getMessage());
            return null;
        }
    }
    
    private String extractPublicId(String imageUrl) {
        try {
            if (!imageUrl.contains("/upload/")) {
                return null;
            }
            
            String[] parts = imageUrl.split("/upload/");
            if (parts.length < 2) {
                return null;
            }
            
            String afterUpload = parts[1];
            
            if (afterUpload.startsWith("v")) {
                int slashIndex = afterUpload.indexOf('/');
                if (slashIndex > 0) {
                    afterUpload = afterUpload.substring(slashIndex + 1);
                }
            }
            
            int lastDotIndex = afterUpload.lastIndexOf('.');
            if (lastDotIndex > 0) {
                afterUpload = afterUpload.substring(0, lastDotIndex);
            }
            
            return afterUpload;
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'extraction du public_id: " + e.getMessage());
            return null;
        }
    }
}