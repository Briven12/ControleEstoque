package io.github.briven12.ControleEstoque.controller;


import io.github.briven12.ControleEstoque.DTO.ProductDTO;
import io.github.briven12.ControleEstoque.DTO.RetirarEstoqueDTO;
import io.github.briven12.ControleEstoque.entity.Product;
import io.github.briven12.ControleEstoque.repository.EstoqueRepository;
import io.github.briven12.ControleEstoque.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
public class EstoqueController {

    private final EstoqueRepository estoqueRepository;
    private final ProductService productService;

    public EstoqueController(
            EstoqueRepository estoqueRepository,
            ProductService productService) {

        this.estoqueRepository = estoqueRepository;
        this.productService = productService;
    }

    @PostMapping
    public Product save(@RequestBody ProductDTO dto) {
        System.out.println("Produto Recebido: " + dto.toString());
        return productService.salvar(dto);
    }

    @GetMapping
    public List<Product> findAll() {
        return estoqueRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Product> findById(@PathVariable Long id) {
        return estoqueRepository.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        productService.apagarProduto(id);
    }

    @GetMapping("/buscar")
    public List<Product> findByNameContaining(
            @RequestParam String name) {

        return estoqueRepository.findByNameContaining(name);
    }

    @PutMapping("/{id}/retirar-estoque")
    public Product retirarEstoque(
            @PathVariable Long id,
            @RequestBody RetirarEstoqueDTO dtoqtd) {

        return productService.retirarEstoque(id, dtoqtd);
    }
}