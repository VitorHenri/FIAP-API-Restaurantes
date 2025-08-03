package com.fiap.api.restaurante.dtos;

public record RestauranteResponseDTO(
    Long id,
    String nome,
    String tipoCozinha,
    String horarioFuncionamento,
    String dono,
    EnderecoDTO endereco
) {}
