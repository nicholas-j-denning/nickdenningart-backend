package com.nickdenningart.gallery.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GalleryItem {
    String id;
    String title;
    String printsUrl;
    List<String> tags;
}
