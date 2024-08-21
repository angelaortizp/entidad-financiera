package com.entidadfinanciera.productoservice.repository;

import com.entidadfinanciera.productoservice.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByClienteId(Long clienteId);
    boolean existsByClienteId(Long clienteId);
}
