package com.fiap.api.restaurante.dtos;

public record RestauranteDTO(
    String nome,
    String tipoCozinha,
    String horarioFuncionamento,
    Long usuarioId,
    EnderecoDTO endereco
) {}
