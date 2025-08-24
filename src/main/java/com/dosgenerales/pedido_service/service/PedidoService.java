package com.dosgenerales.pedido_service.service;

import com.dosgenerales.pedido_service.dto.*;
import com.dosgenerales.pedido_service.model.EstadoPedido;
import com.dosgenerales.pedido_service.model.Pedido;
import com.dosgenerales.pedido_service.model.PedidoItem;
import com.dosgenerales.pedido_service.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final RestTemplate restTemplate;
    private final KafkaTemplate<String, PedidoPagadoEvent> kafkaTemplate;

    @Value("${catalogo.service.url}")
    private String catalogoServiceUrl;

    @Value("${app.kafka.topic.pedidos-pagados}")
    private String topicPedidosPagados;

    @Transactional
    public PedidoResponseDTO createPedido(CreatePedidoRequestDTO request, String userEmail) {
        Pedido pedido =new Pedido();
        pedido.setUserEmail(userEmail);
        pedido.setFechaCreacion(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);

        List<PedidoItem> pedidoItems = new ArrayList<>();
        Double precioTotal = 0.0;

        for (PedidoItemRequestDTO itemDto : request.getItems()) {
            ProductoDTO producto = restTemplate.getForObject(catalogoServiceUrl + "/" + itemDto.getProductoId(), ProductoDTO.class);

            if (producto == null || producto.getStock() < itemDto.getCantidad()) {
                throw new RuntimeException("Producto no disponible o sin stock suficiente.");
            }

            PedidoItem pedidoItem = new PedidoItem();
            pedidoItem.setProductoId(producto.getId());
            pedidoItem.setCantidad(itemDto.getCantidad());
            pedidoItem.setPrecioUnitario(producto.getPrecio());
            pedidoItem.setPedido(pedido);
            pedidoItems.add(pedidoItem);

            precioTotal += producto.getPrecio() * itemDto.getCantidad();
        }

        pedido.setItems(pedidoItems);
        pedido.setPrecioTotal(precioTotal);

        //Pago simulado
        //TODO: generar un enlace de pago
        //simularPagoExitoso(pedido);

        Pedido savedPedido = pedidoRepository.save(pedido);
        //Simulacion
        marcarPedidoComoPagado(savedPedido.getId());

        return new PedidoResponseDTO()
                .builder()
                .id(pedido.getId())
                .userEmail(pedido.getUserEmail())
                .fechaCreacion(pedido.getFechaCreacion())
                .items(pedido.getItems().stream()
                        .map(item -> new PedidoItemDTO(item))
                        .collect(Collectors.toList()))
                .precioTotal(precioTotal)
                .estado(pedido.getEstado())
                .build();
    }

    private void marcarPedidoComoPagado(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(/*...*/);
        pedido.setEstado(EstadoPedido.PAGADO);
        pedidoRepository.save(pedido);

        // ¡AQUÍ ESTÁ LA MAGIA!
        // Creamos el evento con los datos de los productos vendidos
        List<ProductoVendidoDTO> productosVendidos = pedido.getItems().stream()
                .map(item -> new ProductoVendidoDTO(item.getProductoId(), item.getCantidad()))
                .collect(Collectors.toList());

        PedidoPagadoEvent event = new PedidoPagadoEvent(pedido.getId(), productosVendidos);

        // Enviamos el evento al topic de Kafka
        kafkaTemplate.send(topicPedidosPagados, event);
    }

    private void simularPagoExitoso(Pedido pedido) {
        pedido.setEstado(EstadoPedido.PAGADO);
    }

    public List<PedidoResponseDTO> findByUserEmail(String userEmail) {
        ArrayList<PedidoResponseDTO> pedidosDTO = new ArrayList<>();
        List<Pedido> pedidos = pedidoRepository.findByUserEmail(userEmail);
        for (Pedido pedido : pedidos) {
            pedidosDTO.add(new PedidoResponseDTO().builder()
                    .id(pedido.getId())
                    .userEmail(pedido.getUserEmail())
                    .fechaCreacion(pedido.getFechaCreacion())
                    .precioTotal(pedido.getPrecioTotal())
                    .estado(pedido.getEstado())
                    .items(pedido.getItems().stream()
                            .map(item -> new PedidoItemDTO(item))
                            .collect(Collectors.toList()))
                    .build());
        }
        return pedidosDTO;
    }

}
