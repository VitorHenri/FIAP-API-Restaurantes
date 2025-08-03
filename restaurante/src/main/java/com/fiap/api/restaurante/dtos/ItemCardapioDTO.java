package com.fiap.api.restaurante.dtos;

import java.math.BigDecimal;

public record ItemCardapioDTO(
    String nome,
    String descricao,
    BigDecimal preco,
    Boolean disponivelSomenteNoLocal,
    String caminhoFoto,
    Long restauranteId
) {}
