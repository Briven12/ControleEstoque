package io.github.briven12.ControleEstoque.entity;

import io.github.briven12.ControleEstoque.enums.MovementReason;
import io.github.briven12.ControleEstoque.enums.MovementType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "stock_movement")
public class Stock_Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MovementType type;

    @Column(name = "quantity")
    private int quantity;

    @CreationTimestamp
    @Column(name = "movement_date")
    private LocalDateTime movement_date;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason")
    private MovementReason reason;

}
