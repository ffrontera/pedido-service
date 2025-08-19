package com.dosgenerales.pedido_service.controller;

import com.dosgenerales.pedido_service.dto.CreatePedidoRequestDTO;
import com.dosgenerales.pedido_service.dto.PedidoResponseDTO;
import com.dosgenerales.pedido_service.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> createPedido(
            @Valid @RequestBody CreatePedidoRequestDTO request,
            Principal principal) {

        String userEmail = principal.getName(); // Obtenemos el email del usuario desde el token JWT
        PedidoResponseDTO response = pedidoService.createPedido(request, userEmail);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> getMisPedidos(Principal principal) {
        String userEmail = principal.getName();
        return ResponseEntity.ok(pedidoService.findByUserEmail(userEmail));
    }
}
