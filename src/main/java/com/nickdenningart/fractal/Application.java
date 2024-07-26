package com.nickdenningart.fractal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

import com.nickdenningart.fractal.controller.FractalController;
import com.nickdenningart.fractal.controlleradvice.ControllerExceptionHandler;
import com.nickdenningart.fractal.service.AuthorizationService;
import com.nickdenningart.fractal.service.FractalService;
import com.nickdenningart.fractal.service.GalleryService;
import com.nickdenningart.fractal.service.ImageService;
import com.nickdenningart.fractal.service.PrintifyService;

@SpringBootConfiguration
@EnableAutoConfiguration
@Import({
	FractalController.class, 
	ControllerExceptionHandler.class,
	AuthorizationService.class,
	FractalService.class,
	GalleryService.class,
	ImageService.class,
	PrintifyService.class
})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}
}
