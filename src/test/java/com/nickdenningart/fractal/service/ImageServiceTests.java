package com.nickdenningart.fractal.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.nickdenningart.fractal.exception.DynamoDbItemNotFoundException;
import com.nickdenningart.fractal.exception.ImageFileReadException;
import com.nickdenningart.fractal.model.Fractal;

import com.nickdenningart.fractal.exception.DynamoDbItemNotFoundException;

@SpringBootTest
public class ImageServiceTests {

    @MockBean FractalService fractalService;

    @Autowired ImageService imageService;

    @Test
    void crudWorks() throws IOException, DynamoDbItemNotFoundException, ImageFileReadException{
        // try random delete
        imageService.removeImage("asdf", "asdfasdfas");
        // Make mock MultipartFile from local file
        String fileName = "test500x500.jpg";
        Path path = Paths.get(fileName);
        byte[] content = Files.readAllBytes(path);
        MultipartFile multipartFile = new MockMultipartFile(fileName, content);

        // Pick random id and set size for upload
        String id = UUID.randomUUID().toString();
        String size = "500x500";

        // set title imageService will use to set s3 download name
        when(fractalService.getFractal(id))
            .thenReturn(Fractal.builder().title("test title").build());

        // Check image is not present
        assertThat(imageService.isImagePresent(id, size)).isFalse();

        // upload file
        imageService.storeImage(multipartFile, id, size);

        // Check image is present
        assertThat(imageService.isImagePresent(id, size)).isTrue();

        // delete file
        imageService.removeImage(id, size);
        
        // Check image is not present
        assertThat(imageService.isImagePresent(id, size)).isFalse();

    }
}
