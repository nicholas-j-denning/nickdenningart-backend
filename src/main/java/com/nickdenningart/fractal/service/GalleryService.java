package com.nickdenningart.fractal.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nickdenningart.fractal.dto.GalleryItem;
import com.nickdenningart.fractal.model.Fractal;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import io.awspring.cloud.s3.S3Template;

@Service
public class GalleryService {

    private final DynamoDbTemplate dynamoDbTemplate;
    private final S3Template s3Template;
    private final String bucket;

    public GalleryService(
        DynamoDbTemplate dynamoDbTemplate, 
        S3Template s3Template, 
        @Value("${gallery-bucket}") 
        String bucket
        ){
        this.dynamoDbTemplate = dynamoDbTemplate;
        this.s3Template = s3Template;
        this.bucket = bucket;
    }

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

    public void updateGallery() throws JsonProcessingException {
        //List<GalleryItem> gallery = getGallery();
        //String json = mapper.writeValueAsString(gallery);
        s3Template.store(bucket,"gallery.json",getGallery());
    }

}
