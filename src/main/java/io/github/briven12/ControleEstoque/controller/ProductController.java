package io.github.briven12.ControleEstoque.controller;


import io.github.briven12.ControleEstoque.DTO.EntradaDTO;
import io.github.briven12.ControleEstoque.DTO.HistoricoProdutoDTO;
import io.github.briven12.ControleEstoque.DTO.ProductDTO;
import io.github.briven12.ControleEstoque.DTO.RetirarEstoqueDTO;
import io.github.briven12.ControleEstoque.entity.Product;
import io.github.briven12.ControleEstoque.repository.ProductRepository;
import io.github.briven12.ControleEstoque.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductService productService;

    public ProductController(
            ProductRepository productRepository,
            ProductService productService) {

        this.productRepository = productRepository;
        this.productService = productService;
    }
    @PostMapping
    public ResponseEntity<List<ProductDTO>> save(@RequestBody List<ProductDTO> dtos) {
        List<ProductDTO> salvos = productService.salvar(dtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvos);
    }

    @GetMapping
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Product> findById(@PathVariable Long id) {
        return productRepository.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        productService.apagarProduto(id);
    }

    @GetMapping("/buscar")
    public List<Product> findByNameContaining(
            @RequestParam String name) {

        return productRepository.findByNameContaining(name);
    }

    @PutMapping("/{id}/retirar-estoque")
    public Product retirarEstoque(
            @PathVariable Long id,
            @RequestBody RetirarEstoqueDTO dtoqtd) {

        return productService.retirarEstoque(id, dtoqtd);
    }

    @PutMapping("/{id}/adcionar-estoque")
    public Product adcionarEstoque(
            @PathVariable Long id,
            @RequestBody EntradaDTO dtoqtd) {
        return productService.entradaEstoque(id,dtoqtd);
    }

    @PutMapping("/{id}/ajuste-estoque")
    public Product ajusteEstoque(
            @PathVariable Long id,
            @RequestBody EntradaDTO dtoqtd
    ) {
        return productService.ajusteEstoque(id,dtoqtd);
    }

    @GetMapping("/{id}/historico-produto")
    public List<HistoricoProdutoDTO> historicoProduto(
            @PathVariable Long id){
        return productService.historicoProduto(id);
    }

    @GetMapping("/listaProdutos")
    public ResponseEntity<Page<ProductDTO>> listarProdutos(
            @PageableDefault(page = 0,size = 10,sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {

        Page<ProductDTO> paginaDeProdutos = productService.listarProdutos(pageable);

        return ResponseEntity.ok(paginaDeProdutos);
    }


}