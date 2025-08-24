package com.dosgenerales.pedido_service.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CreatePedidoRequestDTO {
    @NotEmpty
    private List<PedidoItemRequestDTO> items;
}
