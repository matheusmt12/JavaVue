package com.example.thymeleaf.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinalizarAluguelDTO {
    private long idLocacao;
    private long idCarro;
    private int kmAtual;
}
