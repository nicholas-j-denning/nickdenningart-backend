package com.nickdenningart.fractal.dto.printify;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PublishingProperties {
    private Boolean images;    
    private Boolean variants;    
    private Boolean title;    
    private Boolean description;    
    private Boolean tags;    
}
