package com.dosgenerales.pedido_service.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pedido_item")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productoId;
    private Integer cantidad;
    private Double precioUnitario;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;
}
