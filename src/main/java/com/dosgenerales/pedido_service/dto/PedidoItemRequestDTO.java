package com.dosgenerales.pedido_service.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NonNull;

@Data
public class PedidoItemRequestDTO {
    @NonNull
    private Long productoId;
    @Min(1)
    private Integer cantidad;
}
