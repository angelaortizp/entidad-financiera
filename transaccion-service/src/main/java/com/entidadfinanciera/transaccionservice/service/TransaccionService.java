package com.entidadfinanciera.transaccionservice.service;

import com.entidadfinanciera.transaccionservice.model.Transaccion;
import com.entidadfinanciera.transaccionservice.model.Transaccion.TipoTransaccion;
import com.entidadfinanciera.transaccionservice.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransaccionService {

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

	private boolean productoExists(Long productoId) {
		String url = productoServiceUrl + "/api/productos/" + productoId + "/exists";
		Boolean exists = restTemplate.getForObject(url, Boolean.class);
		return exists != null && exists;
	}

}
