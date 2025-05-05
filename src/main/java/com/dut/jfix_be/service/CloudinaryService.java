package com.dut.jfix_be.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    String uploadImage(MultipartFile file);
    void deleteImageByUrl(String imageUrl);
    String uploadAudio(MultipartFile file);
    void deleteAudio(String audioUrl);
} 