package com.nickdenningart.fractal.dto.printify;

import java.util.List;

import lombok.Builder;

@Builder
public record PrintArea(
    List<Integer> variant_ids,
    List<Placeholder> placeholders
){}
