package com.fiap.api.restaurante.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fiap.api.restaurante.dtos.EnderecoDTO;
import com.fiap.api.restaurante.dtos.RestauranteDTO;
import com.fiap.api.restaurante.dtos.RestauranteResponseDTO;
import com.fiap.api.restaurante.services.RestauranteService;

public class RestauranteControllerTest {

    @InjectMocks
    private RestauranteController controller;
    @Mock
    private RestauranteService restauranteService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private RestauranteResponseDTO criarRestauranteResponseDTO() {
        EnderecoDTO endereco = new EnderecoDTO(
            "Rua Domingos Sinotti", "619", "Jardim São Valentim", "Apto 101",
            "13630-000", "Pirassununga", "SP"
        );

        return new RestauranteResponseDTO(
            1L,
            "Restaurante Teste",
            "Brasileira",
            "08:00 - 22:00",
            "Teste 1",
            endereco
        );
    }

    private RestauranteDTO criarRestauranteDTO() {
        EnderecoDTO endereco = new EnderecoDTO(
            "Rua Domingos Sinotti", "619", "Jardim São Valentim", "Apto 101",
            "13630-000", "Pirassununga", "SP"
        );

        return new RestauranteDTO(
            "Restaurante Teste",
            "Brasileira",
            "08:00 - 22:00",
            1L,
            endereco
        );
    }

    @Test
    void criar_retornaRestauranteCriado() {
        RestauranteDTO dto = criarRestauranteDTO();
        RestauranteResponseDTO responseDTO = criarRestauranteResponseDTO();

        when(restauranteService.criar(dto)).thenReturn(responseDTO);

        RestauranteResponseDTO resultado = controller.criar(dto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.nome()).isEqualTo("Restaurante Teste");
        verify(restauranteService).criar(dto);
    }

    @Test
    void listar_retornaListaDeRestaurantes() {
        RestauranteResponseDTO responseDTO = criarRestauranteResponseDTO();

        when(restauranteService.listar()).thenReturn(List.of(responseDTO));

        List<RestauranteResponseDTO> lista = controller.listar();

        assertThat(lista).isNotEmpty();
        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).nome()).isEqualTo("Restaurante Teste");
        verify(restauranteService).listar();
    }

    @Test
    void buscarPorId_retornaRestaurante() {
        RestauranteResponseDTO responseDTO = criarRestauranteResponseDTO();

        when(restauranteService.buscarPorId(1L)).thenReturn(responseDTO);

        RestauranteResponseDTO resultado = controller.buscarPorId(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
        verify(restauranteService).buscarPorId(1L);
    }

    @Test
    void deletar_chamaMetodoDeletar() {
        doNothing().when(restauranteService).deletar(1L);

        controller.deletar(1L);

        verify(restauranteService).deletar(1L);
    }

    @Test
    void atualizar_retornaRestauranteAtualizado() {
        RestauranteDTO dto = criarRestauranteDTO();
        RestauranteResponseDTO responseDTO = criarRestauranteResponseDTO();

        when(restauranteService.atualizar(1L, dto)).thenReturn(responseDTO);

        RestauranteResponseDTO resultado = controller.atualizar(1L, dto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.nome()).isEqualTo("Restaurante Teste");
        verify(restauranteService).atualizar(1L, dto);
    }
}
