package com.nickdenningart.fractal.service;

import com.nickdenningart.fractal.exception.DynamoDbItemNotFoundException;
import com.nickdenningart.fractal.model.Fractal;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.awspring.cloud.s3.S3Template;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.MetadataDirective;

@Service
public class ImageService {

    private final S3Template s3Template;
    private final S3Client s3Client;
    private final FractalService fractalService;
    private final String bucket;

    public ImageService(S3Template s3Template, S3Client s3Client, FractalService fractalService, @Value("${image-bucket}") String bucket) {
        this.s3Template = s3Template;
        this.s3Client = s3Client;
        this.fractalService = fractalService;
        this.bucket = bucket;
    }

    private String getKey(String id, String size){
        return id + "/" + size + ".jpg";
    }

    public String getImagePresignedUrl(String id, String size) throws DynamoDbItemNotFoundException {
        String key = getKey(id, size);
        return s3Template.createSignedPutURL(bucket,key, Duration.ofMinutes(5)).toString();
    }

    public void registerUploadedImage(String id, String size) throws DynamoDbItemNotFoundException {
        // make s3 object key
        String key = getKey(id, size);

        // get fractal info from db
        Fractal fractal = fractalService.getFractal(id);
        
        // set content diposition to make file download with nice name instead of displaying in browser
        String filename = "\"Nick Denning " + fractal.getTitle() + ".jpg\"";
        // ObjectMetadata metadata = ObjectMetadata.builder().contentDisposition("attachment; filename="+filename).build();
        CopyObjectRequest request = CopyObjectRequest.builder()
            .sourceBucket(bucket)
            .sourceKey(key)
            .destinationBucket(bucket)
            .destinationKey(key)
            .contentDisposition("attachment; filename="+filename)
            .contentType("image/jpeg")
            .metadataDirective(MetadataDirective.REPLACE)
            .build();
        s3Client.copyObject(request);

        // register new size in database
        List<String> sizes = new ArrayList<>();
        sizes.addAll(fractal.getSizes());
        if(sizes.stream().filter(sz -> sz.equals(size)).findFirst().isEmpty())
            sizes.add(size);
        fractal.setSizes(sizes);
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
