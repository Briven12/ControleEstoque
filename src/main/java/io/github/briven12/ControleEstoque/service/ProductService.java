package io.github.briven12.ControleEstoque.service;

import io.github.briven12.ControleEstoque.DTO.ProductDTO;
import io.github.briven12.ControleEstoque.DTO.RetirarEstoqueDTO;
import io.github.briven12.ControleEstoque.entity.Product;
import io.github.briven12.ControleEstoque.repository.EstoqueRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final EstoqueRepository estoqueRepository;

    public ProductService(EstoqueRepository estoqueRepository) {
        this.estoqueRepository = estoqueRepository;
    }

    public Product retirarEstoque(Long id, RetirarEstoqueDTO dtoqtd) {

        Product product = estoqueRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Produto nao encontrado")
                );

        if (dtoqtd.getQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "Quantidade deve ser maior que 0"
            );
        }

        if (product.getQuantity() < dtoqtd.getQuantity()) {
            throw new IllegalArgumentException(
                    "Estoque insuficiente"
            );
        }

        product.setQuantity(
                product.getQuantity() - dtoqtd.getQuantity()
        );

        return estoqueRepository.save(product);
    }

    public Product salvar(ProductDTO dto) {

        Product product = new Product();

        product.setName(dto.getName());
        product.setQuantity(dto.getQuantity());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());

        return estoqueRepository.save(product);
    }

    public void apagarProduto(Long id) {
        Product product = estoqueRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto nao encontrado"));
        estoqueRepository.deleteById(id);
    }
}
