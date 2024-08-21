package com.entidadfinanciera.transaccionservice.repository;

import com.entidadfinanciera.transaccionservice.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    List<Transaccion> findByProductoOrigenId(Long productoId);
    List<Transaccion> findByProductoDestinoId(Long productoId);
}
