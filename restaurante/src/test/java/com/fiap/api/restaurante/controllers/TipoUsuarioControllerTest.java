package com.fiap.api.restaurante.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.fiap.api.restaurante.dtos.TipoUsuarioDTO;
import com.fiap.api.restaurante.services.TipoUsuarioService;

class TipoUsuarioControllerTest {

    private TipoUsuarioService tipoUsuarioService;
    private TipoUsuarioController controller;

    @BeforeEach
    void setUp() {
        tipoUsuarioService = mock(TipoUsuarioService.class);
        controller = new TipoUsuarioController(tipoUsuarioService);
    }

    @Test
    void testListarTodos() {
        List<TipoUsuarioDTO> listaMock = Arrays.asList(
                new TipoUsuarioDTO(1L, "Teste 1"),
                new TipoUsuarioDTO(2L, "Teste 2")
        );

        when(tipoUsuarioService.listarTodos()).thenReturn(listaMock);

        List<TipoUsuarioDTO> resultado = controller.listarTodos();

        assertEquals(2, resultado.size());
        assertEquals("Teste 1", resultado.get(0).nome());
    }

    @Test
    void testBuscarPorId_comSucesso() {
        TipoUsuarioDTO tipo = new TipoUsuarioDTO(1L, "Teste");

        when(tipoUsuarioService.buscarPorId(1L)).thenReturn(tipo);

        ResponseEntity<Object> resposta = controller.buscarPorId(1L);

        assertEquals(200, resposta.getStatusCode().value());
        assertEquals(tipo, resposta.getBody());
    }

    @Test
    void testBuscarPorId_comErro() {
        when(tipoUsuarioService.buscarPorId(99L)).thenThrow(new RuntimeException("Tipo não encontrado"));

        ResponseEntity<Object> resposta = controller.buscarPorId(99L);

        assertEquals(400, resposta.getStatusCode().value());
        assertEquals("Tipo não encontrado", resposta.getBody());
    }

    @Test
    void testCriar() {
        TipoUsuarioDTO entrada = new TipoUsuarioDTO(null, "Teste");
        TipoUsuarioDTO retorno = new TipoUsuarioDTO(1L, "Teste");

        when(tipoUsuarioService.criar(entrada)).thenReturn(retorno);

        TipoUsuarioDTO resultado = controller.criar(entrada);

        assertEquals(retorno, resultado);
    }

    @Test
    void testAtualizar_comSucesso() {
        TipoUsuarioDTO dto = new TipoUsuarioDTO(null, "Teste Atualizado");
        TipoUsuarioDTO atualizado = new TipoUsuarioDTO(1L, "Teste Atualizado");

        when(tipoUsuarioService.atualizar(1L, dto)).thenReturn(atualizado);

        ResponseEntity<Object> resposta = controller.atualizar(1L, dto);

        assertEquals(200, resposta.getStatusCode().value());
        assertEquals(atualizado, resposta.getBody());
    }

    @Test
    void testAtualizar_comErro() {
        TipoUsuarioDTO dto = new TipoUsuarioDTO(null, "Teste Atualizado");

        when(tipoUsuarioService.atualizar(1L, dto)).thenThrow(new RuntimeException("Erro ao atualizar"));

        ResponseEntity<Object> resposta = controller.atualizar(1L, dto);

        assertEquals(400, resposta.getStatusCode().value());
        assertEquals("Erro ao atualizar", resposta.getBody());
    }

    @Test
    void testDeletar_comSucesso() {
        ResponseEntity<String> resposta = controller.deletar(1L);

        verify(tipoUsuarioService, times(1)).deletar(1L);
        assertEquals(204, resposta.getStatusCode().value());
    }

    @Test
    void testDeletar_comErro() {
        doThrow(new RuntimeException("Erro ao deletar")).when(tipoUsuarioService).deletar(1L);

        ResponseEntity<String> resposta = controller.deletar(1L);

        assertEquals(400, resposta.getStatusCode().value());
        assertEquals("Erro ao deletar", resposta.getBody());
    }
}
