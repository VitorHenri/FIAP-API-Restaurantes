package com.fiap.api.restaurante.services;

import com.fiap.api.restaurante.dtos.ItemCardapioDTO;
import com.fiap.api.restaurante.dtos.ItemCardapioResponseDTO;
import com.fiap.api.restaurante.entities.ItemCardapio;
import com.fiap.api.restaurante.entities.Restaurante;
import com.fiap.api.restaurante.repositories.ItemCardapioRepository;
import com.fiap.api.restaurante.repositories.RestauranteRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemCardapioServiceTest {

    @InjectMocks
    private ItemCardapioService service;
    @Mock
    private ItemCardapioRepository itemRepository;
    @Mock
    private RestauranteRepository restauranteRepository;
    private Restaurante restaurante;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");
    }

    @Test
    void deveCriarItemComSucesso() {
        ItemCardapioDTO dto = new ItemCardapioDTO(
                "Item",
                "Item Teste",
                new BigDecimal("10.00"),
                true,
                "/imagens/teste.jpg",
                1L
        );

        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
        when(itemRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Optional<ItemCardapioResponseDTO> result = service.criar(dto);

        assertTrue(result.isPresent());
        assertEquals("Item", result.get().nome());
        assertEquals("Restaurante Teste", result.get().restaurante());
    }

    @Test
    void naoDeveCriarItemSeRestauranteNaoExiste() {
        ItemCardapioDTO dto = new ItemCardapioDTO(
                "Item", "desc", new BigDecimal("45"), true, "/itens.jpg", 999L
        );

        when(restauranteRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<ItemCardapioResponseDTO> result = service.criar(dto);

        assertTrue(result.isEmpty());
    }

    @Test
    void deveAtualizarItemExistente() {
        ItemCardapio item = new ItemCardapio();
        item.setId(1L);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);

        boolean atualizado = service.atualizar(1L, new ItemCardapioDTO(
                "Novo Nome", "Nova desc", new BigDecimal("50"), false, "/img.jpg", 1L
        ));

        assertTrue(atualizado);
    }

    @Test
    void naoDeveAtualizarItemInexistente() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        boolean atualizado = service.atualizar(1L, new ItemCardapioDTO(
                "Novo Nome", "Nova desc", new BigDecimal("50"), false, "/img.jpg", 1L
        ));

        assertFalse(atualizado);
    }

    @Test
    void deveListarTodosItens() {
        ItemCardapio item = new ItemCardapio("Item", "desc", new BigDecimal("30"), true, "/item.jpg", restaurante);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<ItemCardapioResponseDTO> lista = service.listarTodos();

        assertEquals(1, lista.size());
        assertEquals("Item", lista.get(0).nome());
    }

    @Test
    void deveDeletarItem() {
        when(itemRepository.existsById(1L)).thenReturn(true);
        boolean result = service.deletar(1L);
        assertTrue(result);
        verify(itemRepository).deleteById(1L);
    }

    @Test
    void naoDeveDeletarItemInexistente() {
        when(itemRepository.existsById(1L)).thenReturn(false);
        boolean result = service.deletar(1L);
        assertFalse(result);
        verify(itemRepository, never()).deleteById(any());
    }
}
