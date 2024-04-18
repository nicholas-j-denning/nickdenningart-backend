package com.nickdenningart.fractal.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nickdenningart.fractal.model.Fractal;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import software.amazon.awssdk.enhanced.dynamodb.Key;

@Service
public class FractalService {

    @Autowired
    private DynamoDbTemplate dynamoDbTemplate;

    private String shopUrl = "https://shop.nickdenningart.com/search?q=";

    public Fractal putFractal(Fractal fractal) {
        String uuid = UUID.randomUUID().toString();
        fractal.setId(uuid);
        fractal.setDate(LocalDateTime.now().toString());
        fractal.setPrintsUrl(shopUrl+fractal.getTitle().replaceAll(" ","+"));
        dynamoDbTemplate.save(fractal);
        return fractal;
    }

    public Fractal getFractal(String id) {
        Key key = Key.builder().partitionValue(id).build();
        return dynamoDbTemplate.load(key, Fractal.class);
    }

    public Fractal update(Fractal fractal) {
        return dynamoDbTemplate.update(fractal);
    }
}
