package com.nickdenningart.fractal.controller;

import org.springframework.web.bind.annotation.RestController;

import com.nickdenningart.fractal.dto.GalleryItem;
import com.nickdenningart.fractal.service.GalleryService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@CrossOrigin
public class GalleryController {

    @Autowired
    GalleryService galleryService;

    @GetMapping("/gallery")
    public List<GalleryItem> getGallery() {
        return galleryService.getGallery();
    }
    
}
