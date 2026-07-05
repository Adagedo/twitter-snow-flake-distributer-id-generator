package code.adagedo.server.models;

import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.math.BigDecimal;

public record CreateProductResponse(@JsonSerialize(using = ToStringSerializer.class) long productId, String name, String description, int quantity, BigDecimal price) {
}
