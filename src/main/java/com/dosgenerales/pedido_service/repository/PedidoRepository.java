package com.dosgenerales.pedido_service.repository;

import com.dosgenerales.pedido_service.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUserEmail(String userEmail);
}
