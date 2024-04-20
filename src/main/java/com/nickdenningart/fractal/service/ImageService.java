package com.nickdenningart.fractal.service;

import com.nickdenningart.fractal.exception.DynamoDbItemNotFoundException;
import com.nickdenningart.fractal.exception.ImageFileReadException;
import com.nickdenningart.fractal.model.Fractal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;

@Service
public class ImageService {

    private final S3Template s3Template;
    private final FractalService fractalService;
    private final String bucket;

    public ImageService(S3Template s3Template, FractalService fractalService, @Value("${image-bucket}") String bucket) {
        this.s3Template = s3Template;
        this.fractalService = fractalService;
        this.bucket = bucket;
    }

    private ObjectMetadata uploadMetadata(String id) throws DynamoDbItemNotFoundException{
        String filename = "\"Nick Denning " + fractalService.getFractal(id).getTitle() + ".jpg\"";
        return ObjectMetadata.builder().contentDisposition("attachment; filename="+filename).build();
    }

    private String getKey(String id, String size){
        return id + "/" + size + ".jpg";
    }

    public void storeImage(MultipartFile file, String id, String size) throws DynamoDbItemNotFoundException, ImageFileReadException {
        String key = getKey(id, size);
        try {
            s3Template.upload(bucket, key, file.getInputStream(), uploadMetadata(id));
        } catch (IOException e) {
            throw new ImageFileReadException();
        }
        Fractal fractal = fractalService.getFractal(id);
        List<String> sizes = new ArrayList<>();
        sizes.addAll(fractal.getSizes());
        sizes.add(size);
        fractalService.updateFractal(fractal);
    }

    public void removeImage(String id, String size){
        String key = getKey(id, size);
        s3Template.deleteObject(bucket, key);
    }

    public void removeAllImages(String id) throws DynamoDbItemNotFoundException{
        List<String> sizes = fractalService.getFractal(id).getSizes();
        for(String size : sizes) removeImage(id,size);
    }

    public boolean isImagePresent(String id, String size){
        String key = getKey(id, size);
        return s3Template.objectExists(bucket,key);
    }

}
