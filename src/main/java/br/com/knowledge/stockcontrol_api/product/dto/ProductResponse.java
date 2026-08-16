package br.com.knowledge.stockcontrol_api.product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String sku,
        String category,
        Integer quantity,
        Integer minimumStock,
        BigDecimal unitPrice,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

}
