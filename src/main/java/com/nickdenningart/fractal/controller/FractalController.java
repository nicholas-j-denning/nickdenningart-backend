package com.nickdenningart.fractal.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.nickdenningart.fractal.exception.DynamoDbItemNotFoundException;
import com.nickdenningart.fractal.model.Fractal;
import com.nickdenningart.fractal.service.FractalService;
import com.nickdenningart.fractal.service.ImageService;
import com.nickdenningart.fractal.service.PrintifyService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
    private final String apiKey;

    public FractalController(FractalService fractalService, ImageService imageService, PrintifyService printifyService, @Value("${x-api-key}") String apiKey) {
        this.fractalService = fractalService;
        this.imageService = imageService;
        this.printifyService = printifyService;
        this.apiKey =  apiKey;
    }

    // get fractal
    @GetMapping("/fractal/{id}")
    public Fractal getFractal(@PathVariable String id) throws DynamoDbItemNotFoundException {
        return fractalService.getFractal(id);
    }

    // post a new fractal
    @PostMapping("/fractal")
    public Fractal postFractal(@RequestBody Fractal fractal, @RequestHeader("x-api-key") String key){
        if (!key.equals(apiKey))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return fractalService.putFractal(fractal);
    }

    @PostMapping("/fractal/{id}/{size}")
    public Fractal postImage(@RequestParam("image") MultipartFile file, @PathVariable String id, @PathVariable String size, @RequestHeader("x-api-key") String key)
        throws DynamoDbItemNotFoundException {
        if (!key.equals(apiKey))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        try {
            imageService.storeImage(file, id, size);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return fractalService.getFractal(id);
    }
    
    @PostMapping("/fractal/{id}/products")
    public Fractal post9000(@PathVariable String id, @RequestHeader("x-api-key") String key) throws DynamoDbItemNotFoundException{
        if (!key.equals(apiKey))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        printifyService.createProducts(id); 
        return fractalService.getFractal(id);
    }

}
