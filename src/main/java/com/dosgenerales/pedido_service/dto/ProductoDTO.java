package com.dosgenerales.pedido_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductoDTO {
    private Long id;
    private String nombre;
    private Double precio;
    private Integer stock;
}
