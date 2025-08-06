package com.fiap.api.restaurante.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.fiap.api.restaurante.entities.ItemCardapio;

public interface ItemCardapioRepository extends JpaRepository<ItemCardapio, Long> {}
