package com.fiap.api.restaurante.services;

import com.fiap.api.restaurante.dtos.ItemCardapioDTO;
import com.fiap.api.restaurante.dtos.ItemCardapioResponseDTO;
import com.fiap.api.restaurante.entities.ItemCardapio;
import com.fiap.api.restaurante.entities.Restaurante;
import com.fiap.api.restaurante.repositories.ItemCardapioRepository;
import com.fiap.api.restaurante.repositories.RestauranteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemCardapioService {

    private final ItemCardapioRepository itemRepository;
    private final RestauranteRepository restauranteRepository;

    public ItemCardapioService(ItemCardapioRepository itemRepository, RestauranteRepository restauranteRepository) {
        this.itemRepository = itemRepository;
        this.restauranteRepository = restauranteRepository;
    }

    public Optional<ItemCardapioResponseDTO> criar(ItemCardapioDTO dto) {
        Optional<Restaurante> restauranteOpt = restauranteRepository.findById(dto.restauranteId());
        if (restauranteOpt.isEmpty()) return Optional.empty();

        ItemCardapio item = new ItemCardapio(
                dto.nome(),
                dto.descricao(),
                dto.preco(),
                dto.disponivelSomenteNoLocal(),
                dto.caminhoFoto(),
                restauranteOpt.get()
        );

        itemRepository.save(item);

        return Optional.of(new ItemCardapioResponseDTO(
                item.getNome(),
                item.getDescricao(),
                item.getPreco(),
                item.getDisponivelSomenteNoLocal(),
                item.getCaminhoFoto(),
                item.getRestaurante().getNome()
        ));
    }

    public boolean atualizar(Long id, ItemCardapioDTO dto) {
        Optional<ItemCardapio> itemOpt = itemRepository.findById(id);
        if (itemOpt.isEmpty()) return false;

        ItemCardapio item = itemOpt.get();
        item.atualizar(dto.nome(), dto.descricao(), dto.preco(), dto.disponivelSomenteNoLocal(), dto.caminhoFoto());
        itemRepository.save(item);
        return true;
    }

    public List<ItemCardapioResponseDTO> listarTodos() {
        return itemRepository.findAll().stream()
                .map(item -> new ItemCardapioResponseDTO(
                        item.getNome(),
                        item.getDescricao(),
                        item.getPreco(),
                        item.getDisponivelSomenteNoLocal(),
                        item.getCaminhoFoto(),
                        item.getRestaurante().getNome()
                ))
                .toList();
    }

    public boolean deletar(Long id) {
        if (!itemRepository.existsById(id)) return false;
        itemRepository.deleteById(id);
        return true;
    }
}
