package com.fiap.api.restaurante.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fiap.api.restaurante.entities.Restaurante;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
}