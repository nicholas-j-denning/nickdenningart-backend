package com.nickdenningart.fractal.service;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.nickdenningart.fractal.exception.DynamoDbItemNotFoundException;
import com.nickdenningart.fractal.model.Fractal;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import software.amazon.awssdk.enhanced.dynamodb.Key;

@Service
public class FractalService {

    private final DynamoDbTemplate dynamoDbTemplate;
    private final String shopUrl = "https://shop.nickdenningart.com/search?q=";

    FractalService( DynamoDbTemplate dynamoDbTemplate){
        this.dynamoDbTemplate = dynamoDbTemplate;
    }

    public Fractal putFractal(Fractal fractal) {
        String uuid = UUID.randomUUID().toString();
        fractal.setId(uuid);
        fractal.setDate(LocalDateTime.now().toString());
        fractal.setPrintsUrl(shopUrl+fractal.getTitle().replaceAll(" ","+"));
        return dynamoDbTemplate.save(fractal);
    }

    public Fractal getFractal(String id) throws DynamoDbItemNotFoundException {
        Key key = Key.builder().partitionValue(id).build();
        Fractal result =  dynamoDbTemplate.load(key, Fractal.class);
        if (result==null) throw new DynamoDbItemNotFoundException();
        return result;
    }

    public List<Fractal> getFractals(){
        return dynamoDbTemplate.scanAll(Fractal.class).items().stream().collect(toList());
    }

    public void deleteFractal(String id) {
        Key key = Key.builder().partitionValue(id).build();
        dynamoDbTemplate.delete(key, Fractal.class);
    }
}
