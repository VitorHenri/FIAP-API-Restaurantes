package com.fiap.api.restaurante.controllers;

import com.fiap.api.restaurante.dtos.ItemCardapioDTO;
import com.fiap.api.restaurante.dtos.ItemCardapioResponseDTO;
import com.fiap.api.restaurante.services.ItemCardapioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/itens-cardapio")
public class ItemCardapioController {

    private final ItemCardapioService itemCardapioService;

    public ItemCardapioController(ItemCardapioService itemCardapioService) {
        this.itemCardapioService = itemCardapioService;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody ItemCardapioDTO dto) {
        Optional<ItemCardapioResponseDTO> responseOpt = itemCardapioService.criar(dto);
        return responseOpt
                .map(response -> ResponseEntity.created(URI.create("/itens-cardapio")).body(response))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody ItemCardapioDTO dto) {
        boolean atualizado = itemCardapioService.atualizar(id, dto);
        return atualizado ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<ItemCardapioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(itemCardapioService.listarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        boolean deletado = itemCardapioService.deletar(id);
        return deletado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
