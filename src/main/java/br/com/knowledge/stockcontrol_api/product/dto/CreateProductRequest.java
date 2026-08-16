package br.com.knowledge.stockcontrol_api.product.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateProductRequest(
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Size(max = 30) String sku,
        @NotBlank @Size(max = 100) String category,
        @NotNull @Min(0) Integer quantity,
        @NotNull @Min(0) Integer minimumStock,
        @NotNull @DecimalMin(value = "0.0", inclusive = true) BigDecimal unitPrice) {
}
