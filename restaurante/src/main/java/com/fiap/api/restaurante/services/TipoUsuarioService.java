package com.fiap.api.restaurante.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fiap.api.restaurante.dtos.TipoUsuarioDTO;
import com.fiap.api.restaurante.entities.TipoUsuario;
import com.fiap.api.restaurante.repositories.TipoUsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TipoUsuarioService {

    private final TipoUsuarioRepository repository;

    public List<TipoUsuarioDTO> listarTodos() {
        return repository.findAll().stream()
            .map(t -> new TipoUsuarioDTO(t.getId(), t.getNome()))
            .toList();
    }

    public TipoUsuarioDTO buscarPorId(Long id) {
        TipoUsuario tipo = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de usuário não encontrado"));
        return new TipoUsuarioDTO(tipo.getId(), tipo.getNome());
    }

    public TipoUsuarioDTO criar(TipoUsuarioDTO dto) {
        TipoUsuario tipo = new TipoUsuario(null, dto.nome());
        tipo = repository.save(tipo);
        return new TipoUsuarioDTO(tipo.getId(), tipo.getNome());
    }

    public TipoUsuarioDTO atualizar(Long id, TipoUsuarioDTO dto) {
        TipoUsuario tipo = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de usuário não encontrado"));

        tipo.setNome(dto.nome());
        tipo = repository.save(tipo);

        return new TipoUsuarioDTO(tipo.getId(), tipo.getNome());
    }

    public void deletar(Long id) {
        TipoUsuario tipo = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de usuário não encontrado"));

        repository.delete(tipo);
    }

    public TipoUsuario buscarEntidadePorId(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de usuário inválido"));
    }
}

