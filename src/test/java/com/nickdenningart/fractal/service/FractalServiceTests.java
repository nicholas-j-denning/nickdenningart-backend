package com.nickdenningart.fractal.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.nickdenningart.fractal.exception.DynamoDbItemNotFoundException;
import com.nickdenningart.fractal.model.Fractal;

// Integration tests
@SpringBootTest
public class FractalServiceTests {

    @Autowired
    private FractalService fractalService;

    @Test
    public void crudWorks() {
        // generate random fractal to put
        String id = UUID.randomUUID().toString();
        String date = UUID.randomUUID().toString();
        String title = UUID.randomUUID().toString();
        String printsUrl = UUID.randomUUID().toString();
        String tag1 = UUID.randomUUID().toString();
        String tag2 = UUID.randomUUID().toString();
        List<String> tags = List.of(tag1,tag2);
        Fractal fractal = Fractal.builder()
            .id(id)
            .date(date)
            .title(title)
            .printsUrl(printsUrl)
            .tags(tags)
            .build();
        
        // check fractal is not in db and exception is thrown
        assertThrows(DynamoDbItemNotFoundException.class,()->fractalService.getFractal(id));

        // count fractals in db
        int count1 = fractalService.getFractals().size();
        
        // put fractal in db
        Fractal putFractal = fractalService.putFractal(fractal);

        String putId = putFractal.getId();
        String putDate = putFractal.getDate();
        String putPrintsUrl = putFractal.getPrintsUrl();

        // check that generated values were set
        assertThat(putId).isNotEqualTo(id);
        assertThat(putDate).isNotEqualTo(date);
        assertThat(putPrintsUrl).isNotEqualTo(printsUrl);

        // count fractals in db
        int count2 = fractalService.getFractals().size();

        // check 1 fractal was added
        assertThat(count1+1).isEqualTo(count2);

        // delete fractal from db
        fractalService.deleteFractal(id);

        // count fractals in db
        int count3 = fractalService.getFractals().size();

        // check fractal was removed and count is the same as before
        assertThat(count1+1).isEqualTo(count3);

        // check fractal is not in db and exception is thrown
        assertThrows(DynamoDbItemNotFoundException.class,()->fractalService.getFractal(id));
    }

}
