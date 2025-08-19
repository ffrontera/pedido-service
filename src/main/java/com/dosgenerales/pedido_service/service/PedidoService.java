package com.dosgenerales.pedido_service.service;

import com.dosgenerales.pedido_service.dto.CreatePedidoRequestDTO;
import com.dosgenerales.pedido_service.dto.PedidoItemRequestDTO;
import com.dosgenerales.pedido_service.dto.PedidoResponseDTO;
import com.dosgenerales.pedido_service.dto.ProductoDTO;
import com.dosgenerales.pedido_service.model.EstadoPedido;
import com.dosgenerales.pedido_service.model.Pedido;
import com.dosgenerales.pedido_service.model.PedidoItem;
import com.dosgenerales.pedido_service.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final RestTemplate restTemplate;

    @Value("${catalogo.service.url}")
    private String catalogoServiceUrl;

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
        simularPagoExitoso(pedido);

        Pedido savedPedido = pedidoRepository.save(pedido);

        return new PedidoResponseDTO()
                .builder()
                .id(pedido.getId())
                .userEmail(pedido.getUserEmail())
                .fechaCreacion(pedido.getFechaCreacion())
                .items(pedido.getItems())
                .precioTotal(precioTotal)
                .estado(pedido.getEstado())
                .build();
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
                    .items(pedido.getItems())
                    .build());
        }
        return pedidosDTO;
    }

}
