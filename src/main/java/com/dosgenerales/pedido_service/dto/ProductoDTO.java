package com.dosgenerales.pedido_service.dto;

import lombok.Data;

@Data
public class ProductoDTO {
    private Long id;
    private String nombre;
    private Double precio;
    private Integer stock;
}
