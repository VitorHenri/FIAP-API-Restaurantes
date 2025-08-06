package com.fiap.api.restaurante.entities;

import com.fiap.api.restaurante.dtos.EnderecoDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Endereco {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String logradouro;
	private String numero;
	private String bairro;
	private String complemento;
	private String cep;
	private String cidade;
	private String estado;
	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;
	
	public Endereco(EnderecoDTO enderecoDTO,Usuario user) {
		this.logradouro = enderecoDTO.logradouro();
		this.numero = enderecoDTO.numero();
		this.bairro = enderecoDTO.bairro();
		this.complemento = enderecoDTO.complemento();
		this.cep = enderecoDTO.cep();
		this.cidade = enderecoDTO.cidade();
		this.estado = enderecoDTO.estado();
		this.usuario = user;
	}
	
	public void atualizarEndereco(EnderecoDTO enderecoDTO, Usuario user) {
	    if (enderecoDTO.logradouro() != null) {
	        this.logradouro = enderecoDTO.logradouro();
	    }
	    if (enderecoDTO.numero() != null) {
	        this.numero = enderecoDTO.numero();
	    }
	    if (enderecoDTO.bairro() != null) {
	        this.bairro = enderecoDTO.bairro();
	    }
	    if (enderecoDTO.complemento() != null) {
	        this.complemento = enderecoDTO.complemento();
	    }
	    if (enderecoDTO.cep() != null) {
	        this.cep = enderecoDTO.cep();
	    }
	    if (enderecoDTO.cidade() != null) {
	        this.cidade = enderecoDTO.cidade();
	    }
	    if (enderecoDTO.estado() != null) {
	        this.estado = enderecoDTO.estado();
	    }
	    if (user != null) {
	        this.usuario = user;
	    }
	}

}
