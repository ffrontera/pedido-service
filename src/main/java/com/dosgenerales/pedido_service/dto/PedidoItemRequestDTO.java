package com.dosgenerales.pedido_service.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class PedidoItemRequestDTO {
    @NonNull
    private Long productoId;
    @Min(1)
    private Integer cantidad;
}
