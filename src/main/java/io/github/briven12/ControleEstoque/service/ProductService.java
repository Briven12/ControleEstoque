package io.github.briven12.ControleEstoque.service;

import io.github.briven12.ControleEstoque.DTO.EntradaDTO;
import io.github.briven12.ControleEstoque.DTO.ProductDTO;
import io.github.briven12.ControleEstoque.DTO.RetirarEstoqueDTO;
import io.github.briven12.ControleEstoque.config.exception.InsufficientStockException;
import io.github.briven12.ControleEstoque.entity.Product;
import io.github.briven12.ControleEstoque.entity.Stock_Movement;
import io.github.briven12.ControleEstoque.enums.MovementReason;
import io.github.briven12.ControleEstoque.enums.MovementType;
import io.github.briven12.ControleEstoque.repository.ProductRepository;
import io.github.briven12.ControleEstoque.repository.StockMovementRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final StockMovementRepository stockMovementRepository;

    public ProductService(ProductRepository productRepository, StockMovementRepository stockMovementRepository) {
        this.productRepository = productRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    @Transactional
    public Product retirarEstoque(Long id, RetirarEstoqueDTO dtoqtd) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Produto nao encontrado")
                );

        verificarEstoque(product, dtoqtd);

        product.setQuantity(
                product.getQuantity() - dtoqtd.getQuantity()
        );

        Stock_Movement sm = new Stock_Movement();
        sm.setProduct(product);
        sm.setQuantity(dtoqtd.getQuantity());
        sm.setType(MovementType.SAIDA);
        sm.setReason(MovementReason.VENDA);
        stockMovementRepository.save(sm);

        return productRepository.save(product);
    }

    @Transactional
    public Product salvar(ProductDTO dto) {

        Product product = new Product();

        product.setName(dto.getName());
        product.setQuantity(dto.getQuantity());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        Product produtoSalvo = productRepository.save(product);

        Stock_Movement  sm = new Stock_Movement();
        sm.setProduct(produtoSalvo);
        sm.setQuantity(dto.getQuantity());
        sm.setType(MovementType.ENTRADA);
        sm.setReason(MovementReason.COMPRA);

        stockMovementRepository.save(sm);

        return produtoSalvo;
    }

    @Transactional
    public void apagarProduto(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto nao encontrado"));
        Stock_Movement  sm = new Stock_Movement();
        sm.setProduct(product);
        sm.setQuantity(product.getQuantity());
        sm.setType(MovementType.SAIDA);
        sm.setReason(MovementReason.PERDA);
        stockMovementRepository.save(sm);
        productRepository.deleteById(id);
    }

    @Transactional
    public Product entradaEstoque(Long id, EntradaDTO dto) {
        Product product =  productRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto nao encontrado"));

        if (dto.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que 0");
        }

        product.setQuantity(product.getQuantity() + dto.getQuantity());

        Stock_Movement sm = new Stock_Movement();
        sm.setProduct(product);
        sm.setQuantity(dto.getQuantity());
        sm.setType(MovementType.ENTRADA);
        sm.setReason(MovementReason.COMPRA);

        stockMovementRepository.save(sm);

        return productRepository.save(product);

    }


    public Product ajusteEstoque(Long id, EntradaDTO dto) {
        Product product =  productRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto nao encontrado"));

        if (dto.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que 0");
        }

        product.setQuantity(dto.getQuantity());
        Stock_Movement sm = new Stock_Movement();
        sm.setProduct(product);
        sm.setQuantity(dto.getQuantity());
        sm.setType(MovementType.AJUSTE);
        sm.setReason(MovementReason.AJUSTE);

        stockMovementRepository.save(sm);
        return productRepository.save(product);
    }

    public void verificarEstoque(Product product,RetirarEstoqueDTO dtoqtd) {

        if (dtoqtd.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        }

        if (product.getQuantity() < dtoqtd.getQuantity()) {
            throw new InsufficientStockException(product.getQuantity());
        }

    }
}
