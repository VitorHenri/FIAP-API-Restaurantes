package com.fiap.api.restaurante.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "item_cardapio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemCardapio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @Column(length = 1000)
    private String descricao;
    private BigDecimal preco;
    @Column(name = "disponivel_somente_no_local")
    private Boolean disponivelSomenteNoLocal;
    @Column(name = "caminho_foto")
    private String caminhoFoto;
    @ManyToOne
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    
    public ItemCardapio(String nome, String descricao, BigDecimal preco, Boolean disponivelSomenteNoLocal, String caminhoFoto, Restaurante restaurante) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.disponivelSomenteNoLocal = disponivelSomenteNoLocal;
        this.caminhoFoto = caminhoFoto;
        this.restaurante = restaurante;
    }
    
    public void atualizar(String nome, String descricao, BigDecimal preco, Boolean disponivelSomenteNoLocal, String caminhoFoto) {
        if (nome != null) this.nome = nome;
        if (descricao != null) this.descricao = descricao;
        if (preco != null) this.preco = preco;
        if (disponivelSomenteNoLocal != null) this.disponivelSomenteNoLocal = disponivelSomenteNoLocal;
        if (caminhoFoto != null) this.caminhoFoto = caminhoFoto;
    }
}
