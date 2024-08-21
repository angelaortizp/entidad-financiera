package com.entidadfinanciera.productoservice.service;

import com.entidadfinanciera.productoservice.model.Producto;
import com.entidadfinanciera.productoservice.model.Producto.TipoCuenta;
import com.entidadfinanciera.productoservice.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

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

	public Producto createProducto(Producto producto) {
		if (producto.getTipoCuenta() == Producto.TipoCuenta.AHORROS
				&& producto.getSaldo().compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("La cuenta de ahorros no puede tener saldo negativo");
		}
		producto.setEstado(Producto.EstadoCuenta.ACTIVA);
		return productoRepository.save(producto);
	}

	@Transactional
    public Producto updateProducto(Long id, Producto productoDetails) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con id: " + id));
        
        // Validar el número de cuenta
        if (!isNumeroCuentaValido(productoDetails.getTipoCuenta(), productoDetails.getNumeroCuenta())) {
            throw new IllegalArgumentException("El número de cuenta no es válido para el tipo de cuenta especificado");
        }

        // Actualizar los campos
        producto.setTipoCuenta(productoDetails.getTipoCuenta());
        producto.setNumeroCuenta(productoDetails.getNumeroCuenta());
        producto.setEstado(productoDetails.getEstado());
        producto.setSaldo(productoDetails.getSaldo());
       // producto.setExentaGMF(productoDetails.isExentaGMF());
        producto.setClienteId(productoDetails.getClienteId());

        return productoRepository.save(producto);
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