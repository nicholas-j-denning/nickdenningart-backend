package com.nickdenningart.fractal.service;

import com.nickdenningart.fractal.exception.DynamoDbItemNotFoundException;
import com.nickdenningart.fractal.model.Fractal;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Service
public class ImageService {

    private final S3Template s3Template;
    private final FractalService fractalService;
    private final String bucket;

    public ImageService(S3Template s3Template, 
                        S3Client s3Client, 
                        S3Presigner presigner,
                        FractalService fractalService, 
                        @Value("${image-bucket}") String bucket) {
        this.s3Template = s3Template;
        this.fractalService = fractalService;
        this.bucket = bucket;
    }

    // the key for an image in the s3 bucket
    private String getKey(String id, String size){
        return id + "/" + size + ".jpg";
    }

    // returns a presigned put url the client uses to upload an image
    public String getImagePresignedUrl(String id, String size) throws DynamoDbItemNotFoundException {
        String key = getKey(id, size);
        Fractal fractal = fractalService.getFractal(id);
        String filename = "\"Nick Denning " + fractal.getTitle() + ".jpg\"";
        // content disposition makes the client download the image with a nice name instead of displaying in the browser
        ObjectMetadata metadata = ObjectMetadata.builder().contentDisposition("attachment; filename="+filename).build();
        return s3Template.createSignedPutURL(bucket,key, Duration.ofMinutes(5), metadata, "image/jpeg").toString();
    }

    public void removeImage(String id, String size){
        String key = getKey(id, size);
        s3Template.deleteObject(bucket, key);
    }

    public void removeAllImages(String id){
        s3Template.listObjects(bucket, id).stream()
            .forEach(s3resource -> 
                s3Template.deleteObject(bucket, s3resource.getFilename()));
    }

    public boolean isImagePresent(String id, String size){
        String key = getKey(id, size);
        return s3Template.objectExists(bucket,key);
    }

}
