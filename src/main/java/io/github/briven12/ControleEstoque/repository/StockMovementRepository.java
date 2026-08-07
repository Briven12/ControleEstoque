package io.github.briven12.ControleEstoque.repository;

import io.github.briven12.ControleEstoque.entity.Product;
import io.github.briven12.ControleEstoque.entity.Stock_Movement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockMovementRepository extends JpaRepository<Stock_Movement,Long> {
    List<Stock_Movement> findByProductId(Long id);
}
