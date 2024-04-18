package com.nickdenningart.fractal.dto.printify;

import lombok.Builder;

@Builder
public record Image(
    String id, 
    Float x, 
    Float y, 
    Float scale, 
    Integer angle
) {}
