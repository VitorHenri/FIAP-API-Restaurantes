package com.fiap.api.restaurante.dtos;

import java.time.LocalDateTime;

import com.fiap.api.restaurante.entities.Usuario;

public record UsuarioDTO(String nome,String email,String login,String senha,LocalDateTime dataUltimaAlteracao,EnderecoDTO endereco) {
	
	public Usuario createUser() {
		return new Usuario(this);
	}

}
