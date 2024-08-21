package com.entidadfinanciera.transaccionservice.service;


import com.entidadfinanciera.transaccionservice.model.Transaccion;
import com.entidadfinanciera.transaccionservice.model.Transaccion.TipoTransaccion;
import com.entidadfinanciera.transaccionservice.repository.TransaccionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TransaccionService {
	
	private static final Logger logger = LoggerFactory.getLogger(TransaccionService.class);

	@Autowired
	private TransaccionRepository transaccionRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${producto.service.url}")
	private String productoServiceUrl;

	public List<Transaccion> getAllTransacciones() {
		return transaccionRepository.findAll();
	}

	public Optional<Transaccion> getTransaccionById(Long id) {
		return transaccionRepository.findById(id);
	}

	public List<Transaccion> getTransaccionesByProductoId(Long productoId) {
		List<Transaccion> transaccionesOrigen = transaccionRepository.findByProductoOrigenId(productoId);
		List<Transaccion> transaccionesDestino = transaccionRepository.findByProductoDestinoId(productoId);

		return Stream.concat(transaccionesOrigen.stream(), transaccionesDestino.stream()).distinct()
				.collect(Collectors.toList());
	}

	@Transactional
	public Transaccion realizarTransaccion(Transaccion transaccion) {
		logger.info("Iniciando transacción de tipo: {} por un monto de: {}", 
                transaccion.getTipoTransaccion(), transaccion.getMonto());
		switch (transaccion.getTipoTransaccion()) {
		case CONSIGNACION:
			consignar(transaccion);
			break;
		case RETIRO:
			retirar(transaccion);
			break;
		case TRANSFERENCIA:
			transferir(transaccion);
			break;
		default:
			logger.error("Tipo de transacción no válido: {}", transaccion.getTipoTransaccion());
			throw new IllegalArgumentException("Tipo de transacción no válido");
		}
		Transaccion transaccionRealizada = transaccionRepository.save(transaccion);
        logger.info("Transacción realizada exitosamente. ID: {}", transaccionRealizada.getId());
        return transaccionRealizada;
	}

	@Transactional
	public Transaccion createTransaccion(Transaccion transaccion) {

		if (!productoExists(transaccion.getProductoOrigenId())) {
			throw new IllegalArgumentException("El producto de origen no existe");
		}

		if (transaccion.getTipoTransaccion() == TipoTransaccion.TRANSFERENCIA) {
			if (transaccion.getProductoDestinoId() == null) {
				throw new IllegalArgumentException("El producto de destino es requerido para transferencias");
			}
			if (!productoExists(transaccion.getProductoDestinoId())) {
				throw new IllegalArgumentException("El producto de destino no existe");
			}
		} else {

			transaccion.setProductoDestinoId(null);
		}

		return transaccionRepository.save(transaccion);
	}

	@Transactional
	public Optional<Transaccion> updateTransaccion(Long id, Transaccion transaccionDetails) {
		return transaccionRepository.findById(id).map(transaccion -> {

			if (!productoExists(transaccionDetails.getProductoOrigenId())) {
				throw new IllegalArgumentException("El producto de origen no existe");
			}

			if (transaccionDetails.getTipoTransaccion() == TipoTransaccion.TRANSFERENCIA) {
				if (transaccionDetails.getProductoDestinoId() == null) {
					throw new IllegalArgumentException("El producto de destino es requerido para transferencias");
				}
				if (!productoExists(transaccionDetails.getProductoDestinoId())) {
					throw new IllegalArgumentException("El producto de destino no existe");
				}
			} else {

				transaccionDetails.setProductoDestinoId(null);
			}

			transaccion.setTipoTransaccion(transaccionDetails.getTipoTransaccion());
			transaccion.setMonto(transaccionDetails.getMonto());
			transaccion.setProductoOrigenId(transaccionDetails.getProductoOrigenId());
			transaccion.setProductoDestinoId(transaccionDetails.getProductoDestinoId());

			if (transaccionDetails.getFechaTransaccion() != null) {
				transaccion.setFechaTransaccion(transaccionDetails.getFechaTransaccion());
			} else {

				if (transaccion.getFechaTransaccion() == null) {
					transaccion.setFechaTransaccion(LocalDateTime.now());
				}
			}

			return transaccionRepository.save(transaccion);
		});
	}

	public boolean deleteTransaccion(Long id) {
		if (transaccionRepository.existsById(id)) {
			transaccionRepository.deleteById(id);
			return true;
		}
		return false;
	}

	private void consignar(Transaccion transaccion) {
		actualizarSaldoProducto(transaccion.getProductoOrigenId(), transaccion.getMonto());
	}

	private void retirar(Transaccion transaccion) {
		actualizarSaldoProducto(transaccion.getProductoOrigenId(), transaccion.getMonto().negate());
	}

	private void transferir(Transaccion transaccion) {
		if (!productoExists(transaccion.getProductoDestinoId())) {
			throw new IllegalArgumentException("La cuenta de destino no existe");
		}
		actualizarSaldoProducto(transaccion.getProductoOrigenId(), transaccion.getMonto().negate());
		actualizarSaldoProducto(transaccion.getProductoDestinoId(), transaccion.getMonto());
	}

	private void actualizarSaldoProducto(Long productoId, BigDecimal monto) {
		String url = productoServiceUrl + "/api/productos/" + productoId + "/actualizar-saldo";
		System.out.println("Llamar a la URL tipo POST: " + url);
		System.out.println("Body: " + monto);
		restTemplate.postForObject(url, monto, Void.class);
	}

	private boolean productoExists(Long productoId) {
		String url = productoServiceUrl + "/api/productos/" + productoId + "/exists";
		Boolean exists = restTemplate.getForObject(url, Boolean.class);
		return exists != null && exists;
	}

}
