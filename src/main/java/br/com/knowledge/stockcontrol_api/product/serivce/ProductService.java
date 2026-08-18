package br.com.knowledge.stockcontrol_api.product.serivce;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.knowledge.stockcontrol_api.product.dto.CreateProductRequest;
import br.com.knowledge.stockcontrol_api.product.dto.ProductResponse;
import br.com.knowledge.stockcontrol_api.product.dto.UpdateProductRequest;
import br.com.knowledge.stockcontrol_api.product.entity.ProductEntity;
import br.com.knowledge.stockcontrol_api.product.exception.ProductNotFoundException;
import br.com.knowledge.stockcontrol_api.product.exception.ProductSkuAlreadyExistsException;
import br.com.knowledge.stockcontrol_api.product.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ProductResponse create(CreateProductRequest request) {
        if (repository.existsBySku(request.sku())) {
            throw new ProductSkuAlreadyExistsException("Já existe um produto cadastrado com o SKU informado:" + request.sku());
        }
        LocalDateTime now = LocalDateTime.now();

        ProductEntity entity = new ProductEntity(
                UUID.randomUUID(),
                request.name(),
                request.sku(),
                request.category(),
                request.quantity(),
                request.minimumStock(),
                request.unitPrice(),
                true,
                now,
                now);
        ProductEntity saved = repository.save(entity);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(UUID id) {
        return repository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Transactional
    public ProductResponse update(UUID id, UpdateProductRequest request) {
        ProductEntity product = repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        if (repository.existsBySku(request.sku())
                && !product.getSku().equals(request.sku())) {
            throw new ProductSkuAlreadyExistsException("Já existe um produto com o SKU informado:" + request.sku());
        }

        product.setName(request.name());
        product.setSku(request.sku());
        product.setCategory(request.category());
        product.setQuantity(request.quantity());
        product.setMinimumStock(request.minimumStock());
        product.setUnitPrice(request.unitPrice());
        product.setUpdatedAt(LocalDateTime.now());

        ProductEntity updatedProduct = repository.save(product);
        return toResponse(updatedProduct);
    }

    @Transactional
    public ProductResponse toggleActive(UUID id) {
        ProductEntity product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        product.setActive(!product.getActive());
        product.setUpdatedAt(LocalDateTime.now());

        ProductEntity updateProduct = repository.save(product);
        return toResponse(updateProduct);
    }

    @Transactional
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        repository.deleteById(id);
    }

    private ProductResponse toResponse(ProductEntity product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getSku(),
                product.getCategory(),
                product.getQuantity(),
                product.getMinimumStock(),
                product.getUnitPrice(),
                product.getActive(),
                product.getCreatedAt(),
                product.getUpdatedAt());
    }
}
