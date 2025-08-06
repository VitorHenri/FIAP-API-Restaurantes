package com.fiap.api.restaurante.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Object> buscarPorId(@PathVariable Long id) {
    	try{
    		return ResponseEntity.ok(service.buscarPorId(id));
    	}catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TipoUsuarioDTO criar(@RequestBody TipoUsuarioDTO dto) {
        return service.criar(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(@PathVariable Long id, @RequestBody TipoUsuarioDTO dto) {
    	try{
    		return ResponseEntity.ok(service.atualizar(id, dto));
    	}catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deletar(@PathVariable Long id) {
    	try{
    		service.deletar(id);
    		return ResponseEntity.noContent().build();
    	}catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    }
}
