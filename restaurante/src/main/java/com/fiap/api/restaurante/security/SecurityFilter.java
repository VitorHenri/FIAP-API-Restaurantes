package com.fiap.api.restaurante.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fiap.api.restaurante.entities.Usuario;
import com.fiap.api.restaurante.repositories.UsuarioRepository;
import com.fiap.api.restaurante.utils.TokenGenerator;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
@CrossOrigin
public class SecurityFilter extends OncePerRequestFilter {
	
	@Autowired
	private TokenGenerator tokenGenerator;
	@Autowired
	private UsuarioRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = getToken(request);
		
		if (token != null && !token.isBlank()) {
			String login = tokenGenerator.getLogin(token);
			Usuario user = userRepository.findByLogin(login).get();
			Authentication auth = UsernamePasswordAuthenticationToken.authenticated(user, null, user.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(auth);
		}

		filterChain.doFilter(request, response);

	}

	private String getToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		return token;
	}

}
