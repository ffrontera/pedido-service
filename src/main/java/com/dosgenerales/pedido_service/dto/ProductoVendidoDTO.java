package com.dosgenerales.pedido_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoVendidoDTO {
    private Long productoId;
    private Integer cantidad;
}
