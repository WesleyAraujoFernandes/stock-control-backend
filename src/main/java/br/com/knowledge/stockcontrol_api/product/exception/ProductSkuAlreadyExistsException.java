package br.com.knowledge.stockcontrol_api.product.exception;

public class ProductSkuAlreadyExistsException extends RuntimeException {
    public ProductSkuAlreadyExistsException(String sku) {
        super("Ja existe um produto cadastrado com o SKU: " + sku);
    }
}
