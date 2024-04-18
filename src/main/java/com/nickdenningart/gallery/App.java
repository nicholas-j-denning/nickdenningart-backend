package com.nickdenningart.gallery;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.nickdenningart.gallery.DTO.GalleryItem;

@SpringBootApplication
public class App {
	private final GalleryService galleryService;

	public App(GalleryService galleryService) {
		this.galleryService = galleryService;
	}
	/*
	 * You need this main method (empty) or explicit <start-class>example.FunctionConfiguration</start-class>
	 * in the POM to ensure boot plug-in makes the correct entry
	 */
	public static void main(String[] args) {
		// empty unless using Custom runtime at which point it should include
		// SpringApplication.run(FunctionConfiguration.class, args);
	}

	@Bean
	public Supplier<List<GalleryItem>> gallery() {
		return () -> galleryService.getGallery();
	}
}