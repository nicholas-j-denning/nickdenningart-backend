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
public class Image {
    private String id;    
    private Float x;
    private Float y;
    private Float scale;
    private Integer angle;

}
