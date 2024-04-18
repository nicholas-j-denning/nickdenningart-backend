package com.nickdenningart.fractal.dto.printify;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Product {
    private String id;
    private String title;
    private List<String> tags;
    private String description;
    private List<Variant> variants;
    private Integer blueprint_id;
    private Integer print_provider_id;
    private List<PrintArea> print_areas;
}
