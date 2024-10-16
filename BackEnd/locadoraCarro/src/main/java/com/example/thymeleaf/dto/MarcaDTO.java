package com.example.thymeleaf.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MarcaDTO {

    private long id;
    private String name;
    private List<ModeloDTO> modelos;

}
