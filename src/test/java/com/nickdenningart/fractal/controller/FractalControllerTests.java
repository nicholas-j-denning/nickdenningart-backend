package com.nickdenningart.fractal.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.core.StringContains.containsString;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.nickdenningart.fractal.model.Fractal;
import com.nickdenningart.fractal.service.AuthorizationService;
import com.nickdenningart.fractal.service.FractalService;
import com.nickdenningart.fractal.service.GalleryService;
import com.nickdenningart.fractal.service.ImageService;
import com.nickdenningart.fractal.service.PrintifyService;

@WebMvcTest(FractalController.class)
public class FractalControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FractalService fractalService;
    @MockBean 
    private GalleryService galleryService;
    @MockBean
    private ImageService imageService;
    @MockBean
    private PrintifyService printifyService;
    @MockBean 
    private AuthorizationService authorizationService;

    @Test
    void getFractalWorks() throws Exception {
        String id = "test";
        Fractal fractal = Fractal.builder()
            .id(id)
            .build();
        when(fractalService.getFractal(id)).thenReturn(fractal);
        this.mockMvc.perform(get("/fractal/"+id))
            .andExpectAll(
                status().isOk(),
                content().contentType("application/json"),
                content().string(containsString(id))
            );
    }

    @Test
    void deleteFractalWorks() throws Exception {
        String id = "testid";
        String key = "testkey";
        this.mockMvc.perform(delete("/fractal/"+id).header("x-api-key", key))
            .andExpectAll(
                status().isOk()
            );
    }
    
}
