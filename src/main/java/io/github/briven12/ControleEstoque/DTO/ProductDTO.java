package io.github.briven12.ControleEstoque.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {

    private Long id;

    private String name;

    private int quantity;

    private double price;

    private String description;


}
