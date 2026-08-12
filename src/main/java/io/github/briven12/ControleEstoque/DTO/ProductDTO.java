package io.github.briven12.ControleEstoque.DTO;

import io.github.briven12.ControleEstoque.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductDTO {

    private String name;
    private int quantity;
    private double price;
    private String description;

    public ProductDTO(String name, int quantity, double price, String description) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
    }

    public ProductDTO(Product product) {
        this.name = product.getName();
        this.quantity = product.getQuantity();
        this.price = product.getPrice();
        this.description = product.getDescription();
    }
}