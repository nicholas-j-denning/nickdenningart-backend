package com.nickdenningart.fractal.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nickdenningart.fractal.dto.S3Url;
import com.nickdenningart.fractal.exception.AuthorizationException;
import com.nickdenningart.fractal.exception.DynamoDbItemNotFoundException;
import com.nickdenningart.fractal.model.Fractal;
import com.nickdenningart.fractal.service.AuthorizationService;
import com.nickdenningart.fractal.service.FractalService;
import com.nickdenningart.fractal.service.GalleryService;
import com.nickdenningart.fractal.service.ImageService;
import com.nickdenningart.fractal.service.PrintifyService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@CrossOrigin
public class FractalController {

    private final FractalService fractalService;
    private final ImageService imageService;
    private final GalleryService galleryService;
    private final PrintifyService printifyService;
    private final AuthorizationService authorizationService;

    public FractalController(FractalService fractalService, 
                            ImageService imageService, 
                            GalleryService galleryService,
                            PrintifyService printifyService, 
                            AuthorizationService authorizationService) {
        this.fractalService = fractalService;
        this.imageService = imageService;
        this.galleryService = galleryService;
        this.printifyService = printifyService;
        this.authorizationService = authorizationService;
    }

    // get fractal
    @GetMapping("/fractal/{id}")
    public Fractal getFractal(@PathVariable String id) 
        throws DynamoDbItemNotFoundException {
        return fractalService.getFractal(id);
    }

    // post a new fractal
    @PostMapping("/fractal")
    public Fractal postFractal(@RequestBody Fractal fractal, @RequestHeader("x-api-key") String key) 
        throws AuthorizationException{
        authorizationService.checkKey(key);
        return fractalService.putFractal(fractal);
    }

    // get image presigned url
    @GetMapping("/fractal/{id}/{size}")
    public S3Url getImagePresignedUrl(@PathVariable String id, @PathVariable String size, @RequestHeader("x-api-key") String key)
        throws DynamoDbItemNotFoundException, AuthorizationException {
        authorizationService.checkKey(key);
        return new S3Url(imageService.getImagePresignedUrl(id, size));
    }

    // update gallery and create products on printify    
    @PostMapping("/fractal/{id}/publish")
    public void createProducts(@PathVariable String id, @RequestHeader("x-api-key") String key) 
        throws DynamoDbItemNotFoundException, AuthorizationException, JsonProcessingException{
        authorizationService.checkKey(key);
        galleryService.updateGallery();
        printifyService.createProducts(id); 
    }

    // delete fractal images and db record
    @DeleteMapping("/fractal/{id}")
    public void deleteFractal(@PathVariable String id, @RequestHeader("x-api-key") String key) 
        throws DynamoDbItemNotFoundException, AuthorizationException, JsonProcessingException {
        authorizationService.checkKey(key);
        imageService.removeAllImages(id);
        fractalService.deleteFractal(id);
        galleryService.updateGallery();
    }

}
