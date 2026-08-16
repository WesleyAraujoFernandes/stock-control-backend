package br.com.knowledge.stockcontrol_api.product.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.knowledge.stockcontrol_api.product.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    boolean existsBySku(String sku);
}
