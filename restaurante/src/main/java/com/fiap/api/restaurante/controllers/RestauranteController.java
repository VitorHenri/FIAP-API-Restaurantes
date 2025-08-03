package com.fiap.api.restaurante.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.fiap.api.restaurante.dtos.*;
import com.fiap.api.restaurante.services.RestauranteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/restaurantes")
@RequiredArgsConstructor
public class RestauranteController {

    private final RestauranteService restauranteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestauranteResponseDTO criar(@RequestBody RestauranteDTO dto) {
        return restauranteService.criar(dto);
    }

    @GetMapping
    public List<RestauranteResponseDTO> listar() {
        return restauranteService.listar();
    }

    @GetMapping("/{id}")
    public RestauranteResponseDTO buscarPorId(@PathVariable Long id) {
        return restauranteService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        restauranteService.deletar(id);
    }
    
    @PutMapping("/{id}")
    public RestauranteResponseDTO atualizar(@PathVariable Long id, @RequestBody RestauranteDTO dto) {
        return restauranteService.atualizar(id, dto);
    }

}
