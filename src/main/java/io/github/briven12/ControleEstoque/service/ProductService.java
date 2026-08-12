package io.github.briven12.ControleEstoque.service;

import io.github.briven12.ControleEstoque.DTO.EntradaDTO;
import io.github.briven12.ControleEstoque.DTO.HistoricoProdutoDTO;
import io.github.briven12.ControleEstoque.DTO.ProductDTO;
import io.github.briven12.ControleEstoque.DTO.RetirarEstoqueDTO;
import io.github.briven12.ControleEstoque.exception.InsufficientStockException;
import io.github.briven12.ControleEstoque.exception.StockMovementException;
import io.github.briven12.ControleEstoque.entity.Product;
import io.github.briven12.ControleEstoque.entity.Stock_Movement;
import io.github.briven12.ControleEstoque.enums.MovementReason;
import io.github.briven12.ControleEstoque.enums.MovementType;
import io.github.briven12.ControleEstoque.repository.ProductRepository;
import io.github.briven12.ControleEstoque.repository.StockMovementRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public List<ProductDTO> salvar(List<ProductDTO> produtosDto) {
        List<ProductDTO> salvosDto = new ArrayList<>();

        for (ProductDTO dto : produtosDto) {
            Product product = new Product();
            product.setName(dto.getName());
            product.setQuantity(dto.getQuantity());
            product.setPrice(dto.getPrice());
            product.setDescription(dto.getDescription());

            Product produtoSalvo = productRepository.save(product);

            Stock_Movement sm = new Stock_Movement();
            sm.setProduct(produtoSalvo);
            sm.setQuantity(dto.getQuantity());
            sm.setType(MovementType.ENTRADA);
            sm.setReason(MovementReason.COMPRA);

            stockMovementRepository.save(sm);

            // Mapeia a entidade salva de volta para DTO (incluindo ID gerado)
            salvosDto.add(new ProductDTO(produtoSalvo));
        }

        return salvosDto;
    }

    @Transactional
    public void apagarProduto(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto nao encontrado"));
        Stock_Movement sm = new Stock_Movement();
        sm.setProduct(product);
        sm.setQuantity(product.getQuantity());
        sm.setType(MovementType.SAIDA);
        sm.setReason(MovementReason.PERDA);
        stockMovementRepository.save(sm);
        productRepository.deleteById(id);
    }

    @Transactional
    public Product entradaEstoque(Long id, EntradaDTO dto) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto nao encontrado"));

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
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto nao encontrado"));

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

    public void verificarEstoque(Product product, RetirarEstoqueDTO dtoqtd) {

        if (dtoqtd.getQuantity() <= 0) {
            throw new StockMovementException();
        }

        if (product.getQuantity() < dtoqtd.getQuantity()) {
            throw new InsufficientStockException(product.getQuantity());
        }

    }

    public List<HistoricoProdutoDTO> historicoProduto(Long id) {
        List<Stock_Movement> movimentacoes = stockMovementRepository.findByProductId(id);

        List<HistoricoProdutoDTO> historico = new ArrayList<>();

        for (Stock_Movement movimento : movimentacoes) {
            HistoricoProdutoDTO dto = new HistoricoProdutoDTO();

            dto.setName(movimento.getProduct().getName());
            dto.setQuantity(movimento.getQuantity());
            dto.setType(movimento.getType());
            dto.setReason(movimento.getReason());
            dto.setMovement_date(movimento.getMovement_date());

            historico.add(dto);
        }

        return historico;
    }

    private ProductDTO entityParaProductDTO(Product product) {
        return new ProductDTO(
                product.getName(),
                product.getQuantity(),
                product.getPrice(),
                product.getDescription()
        );
    }


    public Page<ProductDTO> listarProdutos(Pageable pag) {
        Page<Product> paginaProdutos = productRepository.findAll(pag);
        return paginaProdutos.map(this::entityParaProductDTO);
    }

}
