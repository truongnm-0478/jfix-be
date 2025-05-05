package com.dut.jfix_be.service.impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.dut.jfix_be.service.CloudinaryService;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(
        @Value("${cloudinary.cloud_name}") String cloudName,
        @Value("${cloudinary.api_key}") String apiKey,
        @Value("${cloudinary.api_secret}") String apiSecret
    ) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", cloudName,
            "api_key", apiKey,
            "api_secret", apiSecret
        ));
    }

    @Override
    public String uploadImage(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Upload image failed", e);
        }
    }

    @Override
    public void deleteImageByUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) return;
        try {
            String[] parts = imageUrl.split("/image/upload/");
            if (parts.length < 2) return;
            String publicIdWithVersion = parts[1];
            String publicId = publicIdWithVersion.replaceAll("^v[0-9]+/", "");
            int dotIdx = publicId.lastIndexOf('.');
            if (dotIdx > 0) publicId = publicId.substring(0, dotIdx);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Delete image failed", e);
        }
    }

    @Override
    public String uploadAudio(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Upload audio failed", e);
        }
    }

    @Override
    public void deleteAudio(String audioUrl) {
        if (audioUrl == null || audioUrl.isEmpty()) return;
        try {
            String[] parts = audioUrl.split("/upload/");
            if (parts.length < 2) return;
            String publicIdWithVersion = parts[1];
            String publicId = publicIdWithVersion.replaceAll("^v[0-9]+/", "");
            int dotIdx = publicId.lastIndexOf('.');
            if (dotIdx > 0) publicId = publicId.substring(0, dotIdx);
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "video"));
        } catch (Exception e) {
            throw new RuntimeException("Delete audio failed", e);
        }
    }
} 