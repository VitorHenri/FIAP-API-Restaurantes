package com.fiap.api.restaurante.dtos;

import java.math.BigDecimal;

public record ItemCardapioResponseDTO(
    String nome,
    String descricao,
    BigDecimal preco,
    Boolean disponivelSomenteNoLocal,
    String caminhoFoto,
    String restaurante
) {}
