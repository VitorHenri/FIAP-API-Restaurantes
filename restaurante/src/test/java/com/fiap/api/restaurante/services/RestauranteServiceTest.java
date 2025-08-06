package com.fiap.api.restaurante.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.fiap.api.restaurante.dtos.*;
import com.fiap.api.restaurante.entities.*;
import com.fiap.api.restaurante.repositories.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.server.ResponseStatusException;

class RestauranteServiceTest {

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EnderecoRepository enderecoRepository;

    @InjectMocks
    private RestauranteService restauranteService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void criar_DeveCriarRestauranteComSucesso() {
        Usuario dono = new Usuario();
        dono.setId(1L);
        dono.setNome("José Franklin");

        EnderecoDTO enderecoDTO = new EnderecoDTO(
            "Rua Domingos Sinotti",
            "619",
            "Jardim São Valentim",
            "",
            "13630-000",
            "Pirassununga",
            "SP"
        );
        RestauranteDTO dto = new RestauranteDTO(
            "Restaurante Teste",
            "Brasileira",
            "10:00-22:00",
            1L,
            enderecoDTO
        );

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(dono));

        Restaurante restauranteSalvo = new Restaurante();
        restauranteSalvo.setId(100L);
        restauranteSalvo.setNome(dto.nome());
        restauranteSalvo.setTipoCozinha(dto.tipoCozinha());
        restauranteSalvo.setHorarioFuncionamento(dto.horarioFuncionamento());
        restauranteSalvo.setDono(dono);
        restauranteSalvo.setEndereco(new Endereco(dto.endereco(), dono));

        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restauranteSalvo);

        RestauranteResponseDTO response = restauranteService.criar(dto);

        assertNotNull(response);
        assertEquals(100L, response.id());
        assertEquals("Restaurante Teste", response.nome());
        assertEquals("Brasileira", response.tipoCozinha());
        assertEquals("José Franklin", response.dono());
        assertEquals("Rua Domingos Sinotti", response.endereco().logradouro());
        assertEquals("619", response.endereco().numero());
        assertEquals("Jardim São Valentim", response.endereco().bairro());
        assertEquals("Pirassununga", response.endereco().cidade());
        assertEquals("SP", response.endereco().estado());

        verify(usuarioRepository).findById(1L);
        verify(restauranteRepository).save(any(Restaurante.class));
    }

    @Test
    void criar_DeveLancarErroSeUsuarioNaoEncontrado() {
        RestauranteDTO dto = new RestauranteDTO(
            "Restaurante Teste",
            "Brasileira",
            "10:00-22:00",
            99L,
            null
        );

        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> restauranteService.criar(dto));

        assertEquals("Usuário dono não encontrado", ex.getReason());
        assertEquals(400, ex.getStatusCode().value());
    }

    @Test
    void listar_DeveRetornarListaDeRestaurantes() {
        Usuario dono = new Usuario();
        dono.setNome("José Franklin");

        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua Domingos Sinotti");
        endereco.setNumero("619");
        endereco.setBairro("Jardim São Valentim");
        endereco.setComplemento("");
        endereco.setCep("13630-000");
        endereco.setCidade("Pirassununga");
        endereco.setEstado("SP");

        Restaurante r = new Restaurante();
        r.setId(1L);
        r.setNome("Restaurante Teste");
        r.setTipoCozinha("Brasileira");
        r.setHorarioFuncionamento("10:00-22:00");
        r.setDono(dono);
        r.setEndereco(endereco);

        when(restauranteRepository.findAll()).thenReturn(List.of(r));

        List<RestauranteResponseDTO> lista = restauranteService.listar();

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("Restaurante Teste", lista.get(0).nome());
        assertEquals("Rua Domingos Sinotti", lista.get(0).endereco().logradouro());
    }

    @Test
    void deletar_DeveChamarDeleteQuandoEncontrarRestaurante() {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);

        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
        doNothing().when(restauranteRepository).deleteById(1L);

        restauranteService.deletar(1L);

        verify(restauranteRepository).deleteById(1L);
    }

    @Test
    void deletar_DeveLancarErroSeNaoEncontrarRestaurante() {
        when(restauranteRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> restauranteService.deletar(1L));

        assertEquals("Restaurante não encontrado", ex.getReason());
        assertEquals(404, ex.getStatusCode().value());
    }

    @Test
    void buscarPorId_DeveRetornarRestauranteQuandoEncontrar() {
        Usuario dono = new Usuario();
        dono.setNome("José Franklin");

        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua Domingos Sinotti");
        endereco.setNumero("619");
        endereco.setBairro("Jardim São Valentim");
        endereco.setComplemento("");
        endereco.setCep("13630-000");
        endereco.setCidade("Pirassununga");
        endereco.setEstado("SP");

        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");
        restaurante.setTipoCozinha("Brasileira");
        restaurante.setHorarioFuncionamento("10:00-22:00");
        restaurante.setDono(dono);
        restaurante.setEndereco(endereco);

        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));

        RestauranteResponseDTO response = restauranteService.buscarPorId(1L);

        assertNotNull(response);
        assertEquals("Restaurante Teste", response.nome());
        assertEquals("José Franklin", response.dono());
        assertEquals("Rua Domingos Sinotti", response.endereco().logradouro());
    }

    @Test
    void buscarPorId_DeveLancarErroSeNaoEncontrar() {
        when(restauranteRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> restauranteService.buscarPorId(1L));

        assertEquals(404, ex.getStatusCode().value());
    }

    @Test
    void atualizar_DeveAtualizarQuandoEncontrar() {
        Usuario donoOriginal = new Usuario();
        donoOriginal.setId(1L);
        donoOriginal.setNome("José Franklin");

        Usuario novoDono = new Usuario();
        novoDono.setId(2L);
        novoDono.setNome("Vitor Santos");

        Endereco enderecoOriginal = new Endereco();
        enderecoOriginal.setId(10L);
        enderecoOriginal.setLogradouro("Rua Domingos Sinotti");
        enderecoOriginal.setNumero("619");
        enderecoOriginal.setBairro("Jardim São Valentim");
        enderecoOriginal.setComplemento("");
        enderecoOriginal.setCep("13630-000");
        enderecoOriginal.setCidade("Pirassununga");
        enderecoOriginal.setEstado("SP");

        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");
        restaurante.setTipoCozinha("Brasileira");
        restaurante.setHorarioFuncionamento("10:00-22:00");
        restaurante.setDono(donoOriginal);
        restaurante.setEndereco(enderecoOriginal);

        EnderecoDTO enderecoDTO = new EnderecoDTO(
            "Rua Domingos Sinotti",
            "620",
            "Jardim São Valentim",
            "Apto 2",
            "13630-000",
            "Pirassununga",
            "SP"
        );
        RestauranteDTO dto = new RestauranteDTO(
            "Restaurante Atualizado",
            "Churrascaria",
            "11:00-23:00",
            2L,
            enderecoDTO
        );

        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(novoDono));
        when(enderecoRepository.findById(10L)).thenReturn(Optional.of(enderecoOriginal));
        when(enderecoRepository.save(any(Endereco.class))).thenAnswer(i -> i.getArguments()[0]);
        when(restauranteRepository.save(any(Restaurante.class))).thenAnswer(i -> i.getArguments()[0]);

        RestauranteResponseDTO atualizado = restauranteService.atualizar(1L, dto);

        assertNotNull(atualizado);
        assertEquals("Restaurante Atualizado", atualizado.nome());
        assertEquals("Churrascaria", atualizado.tipoCozinha());
        assertEquals("Vitor Santos", atualizado.dono());
        assertEquals("Rua Domingos Sinotti", atualizado.endereco().logradouro());
        assertEquals("620", atualizado.endereco().numero());
        assertEquals("Apto 2", atualizado.endereco().complemento());
    }
}
