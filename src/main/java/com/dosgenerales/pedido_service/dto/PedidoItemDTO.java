package com.dosgenerales.pedido_service.dto;

import com.dosgenerales.pedido_service.model.PedidoItem;

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
