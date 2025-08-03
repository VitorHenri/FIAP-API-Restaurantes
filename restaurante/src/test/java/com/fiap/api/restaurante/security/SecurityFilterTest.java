package com.fiap.api.restaurante.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fiap.api.restaurante.entities.Usuario;
import com.fiap.api.restaurante.repositories.UsuarioRepository;
import com.fiap.api.restaurante.utils.TokenGenerator;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SecurityFilterTest {

    private SecurityFilter securityFilter;
    private TokenGenerator tokenGenerator;
    private UsuarioRepository userRepository;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    public void setup() {
        tokenGenerator = mock(TokenGenerator.class);
        userRepository = mock(UsuarioRepository.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);

        securityFilter = new SecurityFilter();
        securityFilter.tokenGenerator = tokenGenerator;
        securityFilter.userRepository = userRepository;

        SecurityContextHolder.clearContext();
    }

    @Test
    public void testDoFilterInternal_ComTokenValido_AutenticaUsuario() throws Exception {
        String tokenErrado = "Bearer abc.def.ghi";
        String login = "usuarioTeste";

        Usuario mockUsuario = new Usuario();
        mockUsuario.setLogin(login);

        when(request.getHeader("Authorization")).thenReturn(tokenErrado);
        when(tokenGenerator.getLogin(tokenErrado)).thenReturn(login);
        when(userRepository.findByLogin(login)).thenReturn(Optional.of(mockUsuario));

        securityFilter.doFilterInternal(request, response, filterChain);

        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals(login, ((Usuario) auth.getPrincipal()).getLogin());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternal_SemToken_NaoAutenticaUsuario() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        securityFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }
}
