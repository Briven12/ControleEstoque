package io.github.briven12.ControleEstoque.config.exception;

public class StockMovementException extends RuntimeException {
    public StockMovementException() {
        super("Quantidade deve ser maior que zero.");
    }
}
