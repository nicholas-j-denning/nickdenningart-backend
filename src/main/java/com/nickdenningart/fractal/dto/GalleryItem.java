package com.nickdenningart.fractal.dto;

import java.util.List;

public record GalleryItem(String id, String title, String printsUrl, List<String> tags) {
}
