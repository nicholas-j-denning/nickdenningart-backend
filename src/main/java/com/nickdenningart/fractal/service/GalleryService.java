package com.nickdenningart.fractal.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nickdenningart.fractal.dto.GalleryItem;

import io.awspring.cloud.s3.S3Template;

@Service
public class GalleryService {

    private final FractalService fractalService;
    private final S3Template s3Template;
    private final String bucket;

    public GalleryService(FractalService fractalService,
                        S3Template s3Template, 
                        @Value("${gallery-bucket}") String bucket){
        this.fractalService = fractalService;
        this.s3Template = s3Template;
        this.bucket = bucket;
    }

    public List<GalleryItem> getGallery() {
        return fractalService.getFractals().stream()
                .sorted()
                .map(fractal -> 
                    new GalleryItem(
                        fractal.getId(),
                        fractal.getTitle(),
                        fractal.getPrintsUrl(),
                        fractal.getTags()))
                .collect(Collectors.toList());
    }

    public void updateGallery() throws JsonProcessingException {
        s3Template.store(bucket,"gallery.json",getGallery());
    }

}
