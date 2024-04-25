package com.nickdenningart.fractal.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;

import com.nickdenningart.fractal.exception.DynamoDbItemNotFoundException;
import com.nickdenningart.fractal.model.Fractal;

// Integration tests, webenvironment needed to make s3 presigned put http call
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Profile("dev")
public class ImageServiceTests {

    @MockBean FractalService fractalService;

    @Autowired ImageService imageService;
    @Autowired TestRestTemplate restTemplate;

    @Test
    void createRemoveWorks() throws IOException, DynamoDbItemNotFoundException, URISyntaxException {
        // Pick random id and set size for upload
        String id = UUID.randomUUID().toString();
        String size = "500x500";
        String title = "test title";

        // set title imageService will use to set s3 download name
        when(fractalService.getFractal(id))
            .thenReturn(Fractal.builder()
                .title(title)
                .sizes(List.of(size))
                .build());

        // Check image is not present
        assertThat(imageService.isImagePresent(id, size)).isFalse();

        // upload file
        String fileName = "test500x500.jpg";
        URI uri = new URI(imageService.getImagePresignedUrl(id, size));
        byte[] body = new FileSystemResource(fileName).getContentAsByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        String s3filename = "\"Nick Denning " + title + ".jpg\"";
        headers.set("Content-Disposition", "attachment; filename="+s3filename);
        HttpEntity<byte[]> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(uri,HttpMethod.PUT,requestEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Check image is present
        assertThat(imageService.isImagePresent(id, size)).isTrue();

        // delete file
        imageService.removeImage(id, size);
        
        // Check image is not present
        assertThat(imageService.isImagePresent(id, size)).isFalse();

    }

    @Test
    void createRemoveAllWorks() throws IOException, DynamoDbItemNotFoundException, URISyntaxException {
        // Pick random id and set size for upload
        String id = UUID.randomUUID().toString();
        String size = "500x500";
        String title = "test title";

        // set title imageService will use to set s3 download name
        when(fractalService.getFractal(id))
            .thenReturn(Fractal.builder()
                .title(title)
                .sizes(List.of(size))
                .build());

        // Check image is not present
        assertThat(imageService.isImagePresent(id, size)).isFalse();

        // upload file
        String fileName = "test500x500.jpg";
        URI uri = new URI(imageService.getImagePresignedUrl(id, size));
        byte[] body = new FileSystemResource(fileName).getContentAsByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        String s3filename = "\"Nick Denning " + title + ".jpg\"";
        headers.set("Content-Disposition", "attachment; filename="+s3filename);
        HttpEntity<byte[]> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(uri,HttpMethod.PUT,requestEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Check image is present
        assertThat(imageService.isImagePresent(id, size)).isTrue();

        // delete file
        imageService.removeAllImages(id);
        
        // Check image is not present
        assertThat(imageService.isImagePresent(id, size)).isFalse();

    }
}
