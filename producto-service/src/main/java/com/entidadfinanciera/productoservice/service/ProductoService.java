package com.entidadfinanciera.productoservice.service;


import com.entidadfinanciera.productoservice.model.Producto;
import com.entidadfinanciera.productoservice.model.Producto.TipoCuenta;
import com.entidadfinanciera.productoservice.repository.ProductoRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ProductoService {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductoService.class);

	@Autowired
	private ProductoRepository productoRepository;

	public List<Producto> getAllProductos() {
		return productoRepository.findAll();
	}

	public boolean existsById(Long id) {
		return productoRepository.existsById(id);
	}

	public Optional<Producto> getProductoById(Long id) {
		return productoRepository.findById(id);
	}

	@Transactional
	public Producto createProducto(Producto producto) {
	    logger.info("Creando nuevo producto de tipo: {}", producto.getTipoCuenta());
	    
	    if (producto.getNumeroCuenta() == null || producto.getNumeroCuenta().isEmpty()) {
	        producto.setNumeroCuenta(generarNumeroCuenta(producto.getTipoCuenta()));
	    } else {
	        validarNumeroCuenta(producto.getNumeroCuenta(), producto.getTipoCuenta());
	    }
	    
	    validarSaldo(producto);
	    
	    if (producto.getEstado() == null) {
	        producto.setEstado(Producto.EstadoCuenta.ACTIVA);
	    }
	    
	    return productoRepository.save(producto);
	}

	@Transactional
    public Producto updateProducto(Long id, Producto productoDetails) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con id: " + id));

        producto.setEstado(productoDetails.getEstado());
        validarSaldo(producto);
        validarNumeroCuenta(producto.getNumeroCuenta(), producto.getTipoCuenta());

        return productoRepository.save(producto);
    }

	@Transactional
	public void cancelarProducto(Long id) {
		logger.info("Intentando cancelar producto con ID: {}", id);
		Producto producto = productoRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con id: " + id));

		if (producto.getSaldo().compareTo(BigDecimal.ZERO) != 0) {
			logger.warn("Intento de cancelar producto con saldo distinto de cero. ID: {}", id);
			throw new IllegalStateException("No se puede cancelar una cuenta con saldo distinto de cero");
		}

		producto.setEstado(Producto.EstadoCuenta.CANCELADA);
		productoRepository.save(producto);
		logger.info("Producto cancelado exitosamente. ID: {}", id);
	}

	public void activarProducto(Long id) {
		cambiarEstadoProducto(id, Producto.EstadoCuenta.ACTIVA);
	}

	public void inactivarProducto(Long id) {
		cambiarEstadoProducto(id, Producto.EstadoCuenta.INACTIVA);
	}

	private void cambiarEstadoProducto(Long id, Producto.EstadoCuenta nuevoEstado) {
		Producto producto = productoRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con id: " + id));
		producto.setEstado(nuevoEstado);
		productoRepository.save(producto);
	}

	private String generarNumeroCuenta(Producto.TipoCuenta tipoCuenta) {
        String prefix = tipoCuenta == Producto.TipoCuenta.AHORROS ? "53" : "33";
        String randomPart = String.format("%08d", new Random().nextInt(100000000));
        return prefix + randomPart;
    }

	private void validarSaldo(Producto producto) {
        if (producto.getTipoCuenta() == Producto.TipoCuenta.AHORROS
                && producto.getSaldo().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Las cuentas de ahorro no pueden tener saldo negativo");
        }
    }
	
	private void validarNumeroCuenta(String numeroCuenta, TipoCuenta tipoCuenta) {
        if (numeroCuenta == null || numeroCuenta.length() != 10) {
            throw new IllegalArgumentException("El número de cuenta debe tener 10 dígitos");
        }

        String prefix = tipoCuenta == TipoCuenta.AHORROS ? "53" : "33";
        if (!numeroCuenta.startsWith(prefix)) {
            throw new IllegalArgumentException("El número de cuenta para " + tipoCuenta + " debe comenzar con " + prefix);
        }
    }

	private boolean isNumeroCuentaValido(TipoCuenta tipoCuenta, String numeroCuenta) {
		if (numeroCuenta == null || numeroCuenta.length() != 10) {
			return false;
		}

		switch (tipoCuenta) {
		case AHORROS:
			return numeroCuenta.startsWith("53");
		case CORRIENTE:
			return numeroCuenta.startsWith("33");
		default:
			return false;
		}
	}

	public void deleteProducto(Long id) {
		Producto producto = productoRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con id: " + id));

		if (!producto.getSaldo().equals(BigDecimal.ZERO)) {
			throw new IllegalStateException("No se puede eliminar un producto con saldo distinto de cero");
		}

		productoRepository.delete(producto);
	}

	public boolean clienteTieneProductos(Long clienteId) {
		return productoRepository.existsByClienteId(clienteId);
	}

	public List<Producto> findByClienteId(Long clienteId) {
		return productoRepository.findByClienteId(clienteId);
	}
}