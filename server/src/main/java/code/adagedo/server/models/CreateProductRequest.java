package code.adagedo.server.models;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record CreateProductRequest(
        String name,
        String description,
        int quantity,
        BigDecimal price) {
}
