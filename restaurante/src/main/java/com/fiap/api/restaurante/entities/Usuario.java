package com.fiap.api.restaurante.entities;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fiap.api.restaurante.dtos.UsuarioDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.ForeignKey;

@Table
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario implements UserDetails{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3598027399769434228L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String email;
	private String login;
	private String senha;
	private LocalDateTime dataUltimaAlteracao;
	private boolean isAdmin;
	@ManyToOne
	@JoinColumn(name = "tipo_usuario_id", foreignKey = @ForeignKey(name = "fk_usuario_tipo_usuario"))
	private TipoUsuario tipoUsuario;
	
	@OneToMany(mappedBy = "usuario",cascade = CascadeType.ALL)
	private List<Endereco> endereco;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_USER"));
	}
	@Override
	public String getPassword() {
		return senha;
	}
	@Override
	public String getUsername() {
		return login;
	}
	
	public void encodePassword(String senha) {
		this.senha = new BCryptPasswordEncoder().encode(senha);
	}
	
	public Usuario(UsuarioDTO usuarioDTO) {
		this.nome = usuarioDTO.nome();
		this.email = usuarioDTO.email();
		this.login = usuarioDTO.login();
		encodePassword(usuarioDTO.senha());
		dataUltimaAlteracao = LocalDateTime.now();
		endereco = List.of(new Endereco(usuarioDTO.endereco(),this));
	}
	
	public void atualizarUsuario(UsuarioDTO usuarioDTO) {
		if(usuarioDTO.nome()!=null) this.nome = usuarioDTO.nome();
		if(usuarioDTO.email()!=null)this.email = usuarioDTO.email();
		if(usuarioDTO.login()!=null) this.login = usuarioDTO.login();
		if(usuarioDTO.senha()!=null) encodePassword(usuarioDTO.senha());
		dataUltimaAlteracao = LocalDateTime.now();
		if(usuarioDTO.endereco()!=null)
			endereco = List.of(new Endereco(usuarioDTO.endereco(),this));
	}
}
