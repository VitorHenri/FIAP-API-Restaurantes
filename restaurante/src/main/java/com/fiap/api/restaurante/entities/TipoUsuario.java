package com.fiap.api.restaurante.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TipoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
}
