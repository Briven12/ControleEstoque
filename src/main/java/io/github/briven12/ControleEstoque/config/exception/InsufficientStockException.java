package io.github.briven12.ControleEstoque.config.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(int qtd) {
        super("Quantidade Insuficiente. Disponivel:" + qtd);
    }
}
