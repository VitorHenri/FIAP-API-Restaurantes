package com.fiap.api.restaurante.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fiap.api.restaurante.entities.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

}
