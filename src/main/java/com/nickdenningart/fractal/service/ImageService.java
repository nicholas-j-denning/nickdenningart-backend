package com.nickdenningart.fractal.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nickdenningart.fractal.exception.DynamoDbItemNotFoundException;

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

    private ObjectMetadata metadata(String id) throws DynamoDbItemNotFoundException{
        String filename = "\"Nick Denning " + fractalService.getFractal(id).getTitle() + ".jpg\"";
        return ObjectMetadata.builder().contentDisposition("attachment; filename="+filename).build();
    }

    public void storeImage(MultipartFile file, String id, String size) throws IOException, DynamoDbItemNotFoundException {
        String key = size + "/" + id + ".jpg";
        s3Template.upload(bucket, key, file.getInputStream(), metadata(id));
    }

}
