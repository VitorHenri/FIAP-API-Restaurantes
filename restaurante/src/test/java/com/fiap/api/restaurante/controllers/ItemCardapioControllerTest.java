package com.fiap.api.restaurante.controllers;

import com.fiap.api.restaurante.dtos.ItemCardapioDTO;
import com.fiap.api.restaurante.dtos.ItemCardapioResponseDTO;
import com.fiap.api.restaurante.services.ItemCardapioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemCardapioControllerTest {

    @Mock
    private ItemCardapioService itemCardapioService;
    @InjectMocks
    private ItemCardapioController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void criar_RetornaCreatedQuandoItemCriado() {
        ItemCardapioDTO dto = new ItemCardapioDTO(
            "item teste",
            "descricao teste",
            new BigDecimal("10.50"),
            true,
            "/fotos/item-teste.jpg",
            1L
        );

        ItemCardapioResponseDTO responseDTO = new ItemCardapioResponseDTO(
            "item teste",
            "descricao teste",
            new BigDecimal("10.50"),
            true,
            "/fotos/item-teste.jpg",
            "Restaurante teste"
        );

        when(itemCardapioService.criar(dto)).thenReturn(Optional.of(responseDTO));

        ResponseEntity<?> response = controller.criar(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody() instanceof ItemCardapioResponseDTO);
        verify(itemCardapioService, times(1)).criar(dto);
    }

    @Test
    void criar_RetornaNotFoundQuandoNaoCriar() {
        ItemCardapioDTO dto = new ItemCardapioDTO(
            "item teste",
            "descricao teste",
            new BigDecimal("10.50"),
            true,
            "/fotos/item-teste.jpg",
            1L
        );

        when(itemCardapioService.criar(dto)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.criar(dto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(itemCardapioService, times(1)).criar(dto);
    }

    @Test
    void atualizar_RetornaOkQuandoAtualizado() {
        Long id = 1L;
        ItemCardapioDTO dto = new ItemCardapioDTO(
            "item teste",
            "descricao teste",
            new BigDecimal("10.50"),
            true,
            "/fotos/item-teste.jpg",
            1L
        );

        when(itemCardapioService.atualizar(id, dto)).thenReturn(true);

        ResponseEntity<?> response = controller.atualizar(id, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(itemCardapioService, times(1)).atualizar(id, dto);
    }

    @Test
    void atualizar_RetornaNotFoundQuandoNaoAtualizar() {
        Long id = 1L;
        ItemCardapioDTO dto = new ItemCardapioDTO(
            "item teste",
            "descricao teste",
            new BigDecimal("10.50"),
            true,
            "/fotos/item-teste.jpg",
            1L
        );

        when(itemCardapioService.atualizar(id, dto)).thenReturn(false);

        ResponseEntity<?> response = controller.atualizar(id, dto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(itemCardapioService, times(1)).atualizar(id, dto);
    }

    @Test
    void listarTodos_RetornaListaItens() {
        List<ItemCardapioResponseDTO> listaMock = List.of(
            new ItemCardapioResponseDTO(
                    "item teste",
                    "descricao teste",
                    new BigDecimal("10.50"),
                    true,
                    "/fotos/item-teste.jpg",
                    "Restaurante teste"
            ),
            new ItemCardapioResponseDTO(
                    "item teste",
                    "descricao teste",
                    new BigDecimal("10.50"),
                    true,
                    "/fotos/item-teste.jpg",
                    "Restaurante teste"
            )
        );

        when(itemCardapioService.listarTodos()).thenReturn(listaMock);

        ResponseEntity<List<ItemCardapioResponseDTO>> response = controller.listarTodos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(itemCardapioService, times(1)).listarTodos();
    }

    @Test
    void deletar_RetornaNoContentQuandoDeletado() {
        Long id = 1L;

        when(itemCardapioService.deletar(id)).thenReturn(true);

        ResponseEntity<?> response = controller.deletar(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(itemCardapioService, times(1)).deletar(id);
    }

    @Test
    void deletar_RetornaNotFoundQuandoNaoDeletado() {
        Long id = 1L;

        when(itemCardapioService.deletar(id)).thenReturn(false);

        ResponseEntity<?> response = controller.deletar(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(itemCardapioService, times(1)).deletar(id);
    }
}
