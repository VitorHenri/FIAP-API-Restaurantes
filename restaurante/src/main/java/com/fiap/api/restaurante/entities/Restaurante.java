package com.fiap.api.restaurante.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "restaurante")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String tipoCozinha;
    private String horarioFuncionamento;
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario dono;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Endereco endereco;
}
