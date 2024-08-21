package com.entidadfinanciera.clienteservice.repository;

import com.entidadfinanciera.clienteservice.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	boolean existsByNumeroIdentificacion(String numeroIdentificacion);
}