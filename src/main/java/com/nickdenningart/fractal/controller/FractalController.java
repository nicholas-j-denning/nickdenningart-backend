package com.nickdenningart.fractal.controller;

import com.nickdenningart.fractal.exception.AuthorizationException;
import com.nickdenningart.fractal.exception.DynamoDbItemNotFoundException;
import com.nickdenningart.fractal.exception.ImageFileReadException;
import com.nickdenningart.fractal.model.Fractal;
import com.nickdenningart.fractal.service.AuthorizationService;
import com.nickdenningart.fractal.service.FractalService;
import com.nickdenningart.fractal.service.ImageService;
import com.nickdenningart.fractal.service.PrintifyService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@CrossOrigin
public class FractalController {

    private final FractalService fractalService;
    private final ImageService imageService;
    private final PrintifyService printifyService;
    private final AuthorizationService authorizationService;

    public FractalController(
        FractalService fractalService, 
        ImageService imageService, 
        PrintifyService printifyService, 
        AuthorizationService authorizationService) {
        this.fractalService = fractalService;
        this.imageService = imageService;
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

    // post an image
    @PostMapping("/fractal/{id}/{size}")
    public Fractal postImage(@RequestParam("image") MultipartFile file, @PathVariable String id, @PathVariable String size, @RequestHeader("x-api-key") String key)
        throws DynamoDbItemNotFoundException, AuthorizationException, ImageFileReadException {
        authorizationService.checkKey(key);
        imageService.storeImage(file, id, size);
        return fractalService.getFractal(id);
    }

    // create products on printify    
    @PostMapping("/fractal/{id}/products")
    public Fractal post9000(@PathVariable String id, @RequestHeader("x-api-key") String key) 
        throws DynamoDbItemNotFoundException, AuthorizationException{
        authorizationService.checkKey(key);
        printifyService.createProducts(id); 
        return fractalService.getFractal(id);
    }

}
