package br.com.knowledge.stockcontrol_api.product.exception;

public class ProductSkuAlreadyExistsException extends RuntimeException {
    public ProductSkuAlreadyExistsException(String sku) {
        super(sku);
    }
}
