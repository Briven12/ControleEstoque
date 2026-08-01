package io.github.briven12.ControleEstoque.repository;

import io.github.briven12.ControleEstoque.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstoqueRepository extends JpaRepository<Product,Long> {

    List<Product> findByNameContaining(String name);
}
