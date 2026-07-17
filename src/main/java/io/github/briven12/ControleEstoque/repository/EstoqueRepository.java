package io.github.briven12.ControleEstoque.repository;

import io.github.briven12.ControleEstoque.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstoqueRepository extends JpaRepository<Estoque,Long> {

    List<Estoque> findByNameContaining(String name);
}
