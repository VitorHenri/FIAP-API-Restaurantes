package com.fiap.api.restaurante.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.fiap.api.restaurante.dtos.TokenData;
import com.fiap.api.restaurante.dtos.UsuarioDTO;
import com.fiap.api.restaurante.entities.TipoUsuario;
import com.fiap.api.restaurante.entities.Usuario;
import com.fiap.api.restaurante.repositories.UsuarioRepository;
import com.fiap.api.restaurante.services.TipoUsuarioService;
import com.fiap.api.restaurante.utils.TokenGenerator;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private TipoUsuarioService tipoUsuarioService;
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private TokenGenerator tokenGenerator;

	@PostMapping("/cadastro")
	public ResponseEntity<Object> cadastroUsuario(@RequestBody UsuarioDTO dadosUsuario, UriComponentsBuilder uriBuilder) {
		Usuario user = new Usuario(dadosUsuario);
		if(dadosUsuario.tipoUsuarioId()!=null) {
			try {
				TipoUsuario tipousuario = tipoUsuarioService.buscarEntidadePorId(dadosUsuario.tipoUsuarioId());
				user.setTipoUsuario(tipousuario);
			}catch (Exception e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		user = usuarioRepository.save(user);
		URI uri = URI.create(uriBuilder.path("/usuario/{id}").build(user.getId()).toString());
		return ResponseEntity.created(uri).build();
	}
	
	@PostMapping("/login")
	public ResponseEntity<TokenData> loginUsuario(@RequestBody UsuarioDTO dadosUsuario) {
		UsernamePasswordAuthenticationToken autenticacao = new UsernamePasswordAuthenticationToken(dadosUsuario.login(),dadosUsuario.senha());
		Authentication auth = authManager.authenticate(autenticacao);
		Usuario user = (Usuario)auth.getPrincipal();
		String token = tokenGenerator.generateToken(user);
		TokenData tokenData = new TokenData(user.getId(), token);
		return ResponseEntity.ok().body(tokenData);
	}


	@PatchMapping("/{id}")
	public ResponseEntity<String> atualizarUsuario(@PathVariable(name = "id") Long id,@RequestBody UsuarioDTO dadosUsuario) {
		Usuario autenticado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Usuario user = usuarioRepository.findById(id).orElse(null);
		if (user != null && (autenticado.getId() == user.getId() || autenticado.isAdmin())) {
			user.atualizarUsuario(dadosUsuario);
			if(dadosUsuario.tipoUsuarioId()!=null) {
				try {
					TipoUsuario tipousuario = tipoUsuarioService.buscarEntidadePorId(dadosUsuario.tipoUsuarioId());
					user.setTipoUsuario(tipousuario);
				}catch (Exception e) {
					return ResponseEntity.badRequest().body(e.getMessage());
				}
			}
			usuarioRepository.save(user);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.badRequest().body("Operação não permitida ou usuário não encontrado");
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletarUsuario(@PathVariable(name = "id")Long id){
		Usuario user = usuarioRepository.getReferenceById(id);
		if(user!=null) 
			usuarioRepository.delete(user);
		return ResponseEntity.noContent().build();
			
	}

}
