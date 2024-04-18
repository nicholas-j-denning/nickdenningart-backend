package com.nickdenningart.fractal.dto.printify;

import java.util.List;

import lombok.Builder;

@Builder
public record Product (
    String id,
    String title,
    List<String> tags,
    String description,
    List<Variant> variants,
    Integer blueprint_id,
    Integer print_provider_id,
    List<PrintArea> print_areas
){}
