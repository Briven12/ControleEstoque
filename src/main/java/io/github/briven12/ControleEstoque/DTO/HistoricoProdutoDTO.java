package io.github.briven12.ControleEstoque.DTO;

import io.github.briven12.ControleEstoque.entity.Product;
import io.github.briven12.ControleEstoque.enums.MovementReason;
import io.github.briven12.ControleEstoque.enums.MovementType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
public class HistoricoProdutoDTO {

    private String name;

    private int quantity;

    private MovementType type;

    private MovementReason reason;

    private LocalDateTime movement_date;
}
