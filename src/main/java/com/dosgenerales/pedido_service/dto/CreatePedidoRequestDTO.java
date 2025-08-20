package com.dosgenerales.pedido_service.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CreatePedidoRequestDTO {
    @NotEmpty
    private List<PedidoItemRequestDTO> items;
}
