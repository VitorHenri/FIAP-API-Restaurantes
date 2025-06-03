package com.fiap.api.restaurante.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fiap.api.restaurante.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	Optional<Usuario> findByLogin(String login);

}
