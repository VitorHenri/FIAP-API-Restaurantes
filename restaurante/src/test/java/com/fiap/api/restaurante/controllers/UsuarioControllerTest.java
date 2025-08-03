package com.fiap.api.restaurante.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.util.UriComponentsBuilder;

import com.fiap.api.restaurante.dtos.EnderecoDTO;
import com.fiap.api.restaurante.dtos.TokenData;
import com.fiap.api.restaurante.dtos.UsuarioDTO;
import com.fiap.api.restaurante.entities.TipoUsuario;
import com.fiap.api.restaurante.entities.Usuario;
import com.fiap.api.restaurante.repositories.UsuarioRepository;
import com.fiap.api.restaurante.services.TipoUsuarioService;
import com.fiap.api.restaurante.utils.TokenGenerator;

class UsuarioControllerTest {

    @InjectMocks
    private UsuarioController controller;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private TipoUsuarioService tipoUsuarioService;
    @Mock
    private AuthenticationManager authManager;
    @Mock
    private TokenGenerator tokenGenerator;
    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private UsuarioDTO criarUsuarioDTO(String nome, String email, String login, String senha, Long tipoUsuarioId) {
        return new UsuarioDTO(
            nome,
            email,
            login,
            senha,
            LocalDateTime.now(),
            new EnderecoDTO(
            	    "Rua Domingos Sinotti",
            	    "619",     
            	    "Jardim São Valentim",
            	    "Apto 101",         
            	    "13630-000",     
            	    "Pirassununga",      
            	    "SP"     
            	),
            tipoUsuarioId
        );
    }

    @Test
    void testCadastroUsuarioComTipoUsuarioValido() {
        UsuarioDTO dto = criarUsuarioDTO("Teste 1", "teste1@email.com", "teste1", "123456", 1L);
        Usuario usuarioSalvo = new Usuario(dto);
        usuarioSalvo.setId(10L);

        TipoUsuario tipoUsuario = new TipoUsuario(1L, "Teste");

        when(tipoUsuarioService.buscarEntidadePorId(1L)).thenReturn(tipoUsuario);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

        ResponseEntity<Object> response = controller.cadastroUsuario(dto, uriBuilder);

        assertEquals(201, response.getStatusCode().value());
        assertTrue(response.getHeaders().getLocation().toString().contains("/usuario/10"));

        verify(tipoUsuarioService, times(1)).buscarEntidadePorId(1L);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testCadastroUsuarioComTipoUsuarioInvalidoRetornaBadRequest() {
        UsuarioDTO dto = criarUsuarioDTO("Teste 2", "teste2@email.com", "teste2", "123456", 2L);

        when(tipoUsuarioService.buscarEntidadePorId(2L))
            .thenThrow(new RuntimeException("Tipo de usuário inválido"));

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

        ResponseEntity<Object> response = controller.cadastroUsuario(dto, uriBuilder);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Tipo de usuário inválido", response.getBody());

        verify(tipoUsuarioService, times(1)).buscarEntidadePorId(2L);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testLoginUsuarioRetornaToken() {
        UsuarioDTO dto = criarUsuarioDTO("Teste 3", "teste3@email.com", "teste3", "123456", null);

        Authentication authentication = mock(Authentication.class);
        Usuario usuario = new Usuario(dto);
        usuario.setId(15L);

        when(authManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(usuario);
        when(tokenGenerator.generateToken(usuario)).thenReturn("token123");

        ResponseEntity<TokenData> response = controller.loginUsuario(dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(15L, response.getBody().getIdUser());
        assertEquals("token123", response.getBody().getToken());

        verify(authManager, times(1)).authenticate(any());
        verify(tokenGenerator, times(1)).generateToken(usuario);
    }

}
