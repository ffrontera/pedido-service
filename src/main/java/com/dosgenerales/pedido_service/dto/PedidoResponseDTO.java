package com.dosgenerales.pedido_service.dto;

import com.dosgenerales.pedido_service.model.EstadoPedido;
import com.dosgenerales.pedido_service.model.PedidoItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoResponseDTO {
    private Long id;
    private String userEmail;
    private LocalDateTime fechaCreacion;
    private Double precioTotal;
    private EstadoPedido estado;
    private List<PedidoItemDTO> items;
}
