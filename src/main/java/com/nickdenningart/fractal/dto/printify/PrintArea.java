package com.nickdenningart.fractal.dto.printify;

import java.util.List;

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
public class PrintArea {
    private List<Integer> variant_ids;
    private List<Placeholder> placeholders;
}
