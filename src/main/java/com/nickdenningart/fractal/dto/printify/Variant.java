package com.nickdenningart.fractal.dto.printify;

import lombok.Builder;

@Builder
public record Variant(
    Integer id,
    Integer price,
    Boolean is_enabled
) {}
