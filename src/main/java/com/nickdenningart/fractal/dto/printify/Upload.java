package com.nickdenningart.fractal.dto.printify;

import lombok.Builder;

@Builder
public record Upload (
    String id,
    String file_name,
    String url
) {}
