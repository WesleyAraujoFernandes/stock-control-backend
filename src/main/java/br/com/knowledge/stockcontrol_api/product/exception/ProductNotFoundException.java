package br.com.knowledge.stockcontrol_api.product.exception;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(UUID id) {
        super("Produto nao encontrado: " + id);
    }
}
