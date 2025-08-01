package com.fiap.api.restaurante.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.fiap.api.restaurante.dtos.TipoUsuarioDTO;
import com.fiap.api.restaurante.services.TipoUsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tipos-usuario")
@RequiredArgsConstructor
public class TipoUsuarioController {

    private final TipoUsuarioService service;

    @GetMapping
    public List<TipoUsuarioDTO> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public TipoUsuarioDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TipoUsuarioDTO criar(@RequestBody TipoUsuarioDTO dto) {
        return service.criar(dto);
    }

    @PutMapping("/{id}")
    public TipoUsuarioDTO atualizar(@PathVariable Long id, @RequestBody TipoUsuarioDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
