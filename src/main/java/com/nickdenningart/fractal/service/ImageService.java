package com.nickdenningart.fractal.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;

@Service
public class ImageService {
    @Autowired
    private S3Template s3Template;

    @Autowired
    private FractalService fractalService;

    @Value("${s3-bucket}") private String bucket;
    @Value("${aws-region}") private String region;

    private ObjectMetadata metadata(String id){
        String filename = "\"Nick Denning " + fractalService.getFractal(id).getTitle() + ".jpg\"";
        return ObjectMetadata.builder().contentDisposition("attachment; filename="+filename).build();
    }

    public void storeImage(MultipartFile file, String id, String size) throws IOException {
        String key = size + "/" + id + ".jpg";
        s3Template.upload(bucket, key, file.getInputStream(), metadata(id));
    }

}
