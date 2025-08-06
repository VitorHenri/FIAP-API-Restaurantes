package com.fiap.api.restaurante.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fiap.api.restaurante.dtos.*;
import com.fiap.api.restaurante.entities.*;
import com.fiap.api.restaurante.repositories.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
public class RestauranteService {

    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;
    private final EnderecoRepository enderecoRepository;

    public RestauranteResponseDTO criar(RestauranteDTO dto) {
        Usuario dono = usuarioRepository.findById(dto.usuarioId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário dono não encontrado"));

        Endereco endereco = new Endereco(dto.endereco(), dono);

        Restaurante restaurante = new Restaurante();
        restaurante.setNome(dto.nome());
        restaurante.setTipoCozinha(dto.tipoCozinha());
        restaurante.setHorarioFuncionamento(dto.horarioFuncionamento());
        restaurante.setDono(dono);
        restaurante.setEndereco(endereco);

        Restaurante salvo = restauranteRepository.save(restaurante);

        return new RestauranteResponseDTO(
            salvo.getId(),
            salvo.getNome(),
            salvo.getTipoCozinha(),
            salvo.getHorarioFuncionamento(),
            salvo.getDono().getNome(),
            new EnderecoDTO(
            	    salvo.getEndereco().getLogradouro(),
            	    salvo.getEndereco().getNumero(),
            	    salvo.getEndereco().getBairro(),
            	    salvo.getEndereco().getComplemento(),
            	    salvo.getEndereco().getCep(),
            	    salvo.getEndereco().getCidade(),
            	    salvo.getEndereco().getEstado()
            	)
        );
    }

    public List<RestauranteResponseDTO> listar() {
        return restauranteRepository.findAll().stream()
            .map(r -> new RestauranteResponseDTO(
                r.getId(),
                r.getNome(),
                r.getTipoCozinha(),
                r.getHorarioFuncionamento(),
                r.getDono().getNome(),
                new EnderecoDTO(
                	    r.getEndereco().getLogradouro(),
                	    r.getEndereco().getNumero(),
                	    r.getEndereco().getBairro(),
                	    r.getEndereco().getComplemento(),
                	    r.getEndereco().getCep(),
                	    r.getEndereco().getCidade(),
                	    r.getEndereco().getEstado()
                	)

            ))
            .toList();
    }

    public void deletar(Long id) {
    	restauranteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurante não encontrado"));
        restauranteRepository.deleteById(id);
    }

    public RestauranteResponseDTO buscarPorId(Long id) {
        Restaurante r = restauranteRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new RestauranteResponseDTO(
            r.getId(), r.getNome(), r.getTipoCozinha(), r.getHorarioFuncionamento(), r.getDono().getNome(),                 
            new EnderecoDTO(
            	    r.getEndereco().getLogradouro(),
            	    r.getEndereco().getNumero(),
            	    r.getEndereco().getBairro(),
            	    r.getEndereco().getComplemento(),
            	    r.getEndereco().getCep(),
            	    r.getEndereco().getCidade(),
            	    r.getEndereco().getEstado()
            	)
        );
    }
    
    public RestauranteResponseDTO atualizar(Long id, RestauranteDTO dto) {
        Restaurante restaurante = restauranteRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurante não encontrado"));
        
        Usuario dono = null;
        
        if(dto.usuarioId() > 0) {
            dono = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário dono não encontrado"));
        }
        
        if(dto.endereco() != null) {
            Endereco enderecoAtualizado;
            if(restaurante.getEndereco() != null && restaurante.getEndereco().getId() > 0) {
                enderecoAtualizado = enderecoRepository.findById(restaurante.getEndereco().getId())
                    .orElse(new Endereco(dto.endereco(), dono));
            } else {
                enderecoAtualizado = new Endereco(dto.endereco(), dono);
            }
            enderecoAtualizado.atualizarEndereco(dto.endereco(), dono);
            enderecoAtualizado = enderecoRepository.save(enderecoAtualizado);  // salvar explicitamente o endereço
            restaurante.setEndereco(enderecoAtualizado);
        }
        
        if(dto.nome() != null)
            restaurante.setNome(dto.nome());
        if(dto.tipoCozinha() != null)
            restaurante.setTipoCozinha(dto.tipoCozinha());
        if(dto.horarioFuncionamento() != null)
            restaurante.setHorarioFuncionamento(dto.horarioFuncionamento());
        if(dono != null)
            restaurante.setDono(dono);
        
        Restaurante atualizado = restauranteRepository.save(restaurante);

        return new RestauranteResponseDTO(
            atualizado.getId(),
            atualizado.getNome(),
            atualizado.getTipoCozinha(),
            atualizado.getHorarioFuncionamento(),
            atualizado.getDono().getNome(),
            new EnderecoDTO(
                atualizado.getEndereco().getLogradouro(),
                atualizado.getEndereco().getNumero(),
                atualizado.getEndereco().getBairro(),
                atualizado.getEndereco().getComplemento(),
                atualizado.getEndereco().getCep(),
                atualizado.getEndereco().getCidade(),
                atualizado.getEndereco().getEstado()
            )
        );
    }


}
