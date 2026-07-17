package io.github.briven12.ControleEstoque.controller;


import io.github.briven12.ControleEstoque.model.Estoque;
import io.github.briven12.ControleEstoque.repository.EstoqueRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class EstoqueController {
    private EstoqueRepository estoqueRepository;

    public EstoqueController(EstoqueRepository estoqueRepository) {
        this.estoqueRepository = estoqueRepository;
    }

    @PostMapping
    public Estoque save(@RequestBody Estoque estoque){
        System.out.println("Produto Recebido: " + estoque);
        return estoqueRepository.save(estoque);
    }

    @GetMapping
    public List<Estoque> findAll(){
        return estoqueRepository.findAll();
    }

    @PutMapping("{id}")
    public void atualizar(@PathVariable Long id, @RequestBody Estoque estoque){
        estoque.setId(id);
        estoqueRepository.save(estoque);
    }

    @DeleteMapping("{id}")
    public void deletar(@PathVariable Long id){
        estoqueRepository.deleteById(id);
    }

    @GetMapping("/buscar")
    public List<Estoque> findByNameContaining(@RequestParam String name) {
        return estoqueRepository.findByNameContaining(name);
    }
}
