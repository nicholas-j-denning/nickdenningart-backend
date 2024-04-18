package com.nickdenningart.fractal.dto.printify;

import lombok.Builder;

@Builder
public record PublishingProperties(
    Boolean images,    
    Boolean variants,
    Boolean title,
    Boolean description,
    Boolean tags    
) {}
