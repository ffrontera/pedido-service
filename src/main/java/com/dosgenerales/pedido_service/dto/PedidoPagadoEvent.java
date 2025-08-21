package com.dosgenerales.pedido_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoPagadoEvent {
    private Long  pedidoId;
    private List<ProductoVendidoDTO> productoVendidos;
}
