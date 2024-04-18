package com.nickdenningart.gallery;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nickdenningart.gallery.DAO.Fractal;
import com.nickdenningart.gallery.DTO.GalleryItem;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;

@Service
public class GalleryService {
    @Autowired
    DynamoDbTemplate dynamoDbTemplate;

    public List<GalleryItem> getGallery() {
        return dynamoDbTemplate.scanAll(Fractal.class).items().stream()
            .sorted()
            .map(fractal -> 
                new GalleryItem(
                    fractal.getId(),
                    fractal.getTitle(),
                    fractal.getPrintsUrl(),
                    fractal.getTags()))
            .collect(Collectors.toList());
    }

}
