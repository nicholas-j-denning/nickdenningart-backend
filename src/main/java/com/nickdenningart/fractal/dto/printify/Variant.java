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
public class Variant {
    private Integer id;
    private Integer price;
    private Boolean is_enabled;
}
