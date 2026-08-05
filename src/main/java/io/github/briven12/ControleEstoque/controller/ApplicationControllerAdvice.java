package io.github.briven12.ControleEstoque.controller;

import io.github.briven12.ControleEstoque.config.exception.InsufficientStockException;
import io.github.briven12.ControleEstoque.config.exception.StockMovementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApplicationControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InsufficientStockException.class)
    ProblemDetail handleInsufficient(InsufficientStockException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Insufficient stock");
        return problemDetail;
    }

    @ExceptionHandler(StockMovementException.class)
    ProblemDetail handleStockMovement(StockMovementException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Erro Quantidade Solicitada");
        return problemDetail;
    }

}
