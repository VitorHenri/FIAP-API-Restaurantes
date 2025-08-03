package com.fiap.api.restaurante.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.fiap.api.restaurante.dtos.TipoUsuarioDTO;
import com.fiap.api.restaurante.entities.TipoUsuario;
import com.fiap.api.restaurante.repositories.TipoUsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.server.ResponseStatusException;

class TipoUsuarioServiceTest {

    @Mock
    private TipoUsuarioRepository repository;
    @InjectMocks
    private TipoUsuarioService service;
    
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarTodos_DeveRetornarLista() {
        List<TipoUsuario> tipos = List.of(
            new TipoUsuario(1L, "Teste 1"),
            new TipoUsuario(2L, "Teste 2")
        );

        when(repository.findAll()).thenReturn(tipos);

        List<TipoUsuarioDTO> lista = service.listarTodos();

        assertEquals(2, lista.size());
        assertEquals("Teste 1", lista.get(0).nome());
        assertEquals("Teste 2", lista.get(1).nome());
    }

    @Test
    void buscarPorId_DeveRetornarTipoQuandoEncontrar() {
        TipoUsuario tipo = new TipoUsuario(1L, "Teste 1");
        when(repository.findById(1L)).thenReturn(Optional.of(tipo));

        TipoUsuarioDTO dto = service.buscarPorId(1L);

        assertEquals(1L, dto.id());
        assertEquals("Teste 1", dto.nome());
    }

    @Test
    void buscarPorId_DeveLancarErroQuandoNaoEncontrar() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.buscarPorId(99L));
        assertEquals("Tipo de usuário não encontrado", ex.getReason());
        assertEquals(404, ex.getStatusCode().value());
    }

    @Test
    void criar_DeveSalvarTipo() {
        TipoUsuarioDTO dto = new TipoUsuarioDTO(null, "Teste 3");
        TipoUsuario salvo = new TipoUsuario(10L, "Teste 3");

        when(repository.save(any(TipoUsuario.class))).thenReturn(salvo);

        TipoUsuarioDTO criado = service.criar(dto);

        assertNotNull(criado.id());
        assertEquals("Teste 3", criado.nome());
    }

    @Test
    void atualizar_DeveAtualizarTipoQuandoEncontrar() {
        TipoUsuario existente = new TipoUsuario(5L, "Teste 2");
        TipoUsuarioDTO dtoAtualizado = new TipoUsuarioDTO(null, "Teste 3");

        when(repository.findById(5L)).thenReturn(Optional.of(existente));
        when(repository.save(any(TipoUsuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TipoUsuarioDTO atualizado = service.atualizar(5L, dtoAtualizado);

        assertEquals(5L, atualizado.id());
        assertEquals("Teste 3", atualizado.nome());
    }

    @Test
    void atualizar_DeveLancarErroQuandoNaoEncontrar() {
        when(repository.findById(7L)).thenReturn(Optional.empty());

        TipoUsuarioDTO dto = new TipoUsuarioDTO(null, "Teste 3");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.atualizar(7L, dto));
        assertEquals("Tipo de usuário não encontrado", ex.getReason());
        assertEquals(404, ex.getStatusCode().value());
    }

    @Test
    void deletar_DeveDeletarQuandoEncontrar() {
        TipoUsuario tipo = new TipoUsuario(3L, "Teste 1");
        when(repository.findById(3L)).thenReturn(Optional.of(tipo));
        doNothing().when(repository).delete(tipo);

        assertDoesNotThrow(() -> service.deletar(3L));

        verify(repository).delete(tipo);
    }

    @Test
    void deletar_DeveLancarErroQuandoNaoEncontrar() {
        when(repository.findById(8L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.deletar(8L));
        assertEquals("Tipo de usuário não encontrado", ex.getReason());
        assertEquals(404, ex.getStatusCode().value());
    }

    @Test
    void buscarEntidadePorId_DeveRetornarQuandoEncontrar() {
        TipoUsuario tipo = new TipoUsuario(4L, "Teste 2");
        when(repository.findById(4L)).thenReturn(Optional.of(tipo));

        TipoUsuario entidade = service.buscarEntidadePorId(4L);

        assertEquals(4L, entidade.getId());
        assertEquals("Teste 2", entidade.getNome());
    }

    @Test
    void buscarEntidadePorId_DeveLancarErroQuandoNaoEncontrar() {
        when(repository.findById(20L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.buscarEntidadePorId(20L));
        assertEquals("Tipo de usuário inválido", ex.getReason());
        assertEquals(400, ex.getStatusCode().value());
    }
}
