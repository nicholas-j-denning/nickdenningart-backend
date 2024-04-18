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
public class Upload {
    private String id;
    private String file_name;
    private String url;
}
