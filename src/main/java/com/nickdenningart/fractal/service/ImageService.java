package com.nickdenningart.fractal.service;

import com.nickdenningart.fractal.exception.DynamoDbItemNotFoundException;
import com.nickdenningart.fractal.exception.ImageFileReadException;

import java.io.IOException;

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

    public ImageService(S3Template s3Template, FractalService fractalService, @Value("${s3-bucket}") String bucket) {
        this.s3Template = s3Template;
        this.fractalService = fractalService;
        this.bucket = bucket;
    }

    private ObjectMetadata uploadMetadata(String id) throws DynamoDbItemNotFoundException{
        String filename = "\"Nick Denning " + fractalService.getFractal(id).getTitle() + ".jpg\"";
        return ObjectMetadata.builder().contentDisposition("attachment; filename="+filename).build();
    }

    public void storeImage(MultipartFile file, String id, String size) throws DynamoDbItemNotFoundException, ImageFileReadException {
        String key = size + "/" + id + ".jpg";
        try {
            s3Template.upload(bucket, key, file.getInputStream(), uploadMetadata(id));
        } catch (IOException e) {
            throw new ImageFileReadException();
        }
    }

    public void removeImage(String id, String size){
        String key = size + "/" + id + ".jpg";
        s3Template.deleteObject(bucket, key);
    }

    public boolean isImagePresent(String id, String size){
        return s3Template.objectExists(bucket, size+"/"+id+".jpg");
    }

}
