package com.dosgenerales.pedido_service.dto;

import com.dosgenerales.pedido_service.model.PedidoItem;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class PedidoItemDTO {
    Long productoId;
    Integer cantidad;
    Double precioUnitario;

    public PedidoItemDTO(PedidoItem item) {
        productoId = item.getProductoId();
        cantidad = item.getCantidad();
        precioUnitario = item.getPrecioUnitario();
    }
}
