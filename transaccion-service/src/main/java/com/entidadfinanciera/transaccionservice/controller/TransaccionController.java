package com.entidadfinanciera.transaccionservice.controller;

import com.entidadfinanciera.transaccionservice.model.Transaccion;
import com.entidadfinanciera.transaccionservice.service.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

	@Autowired
	private TransaccionService transaccionService;

	@GetMapping
	public List<Transaccion> getAllTransacciones() {
		return transaccionService.getAllTransacciones();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getTransaccionById(@PathVariable Long id) {
		Optional<Transaccion> transaccion = transaccionService.getTransaccionById(id);
		if (transaccion.isPresent()) {
			return ResponseEntity.ok(transaccion.get());
		} else {
			Map<String, Object> response = new HashMap<>();
			response.put("mensaje", "Transacción no encontrada");
			response.put("id", id);
			return ResponseEntity.ok(response);
		}
	}

	@PostMapping
	public ResponseEntity<?> createTransaccion(@RequestBody Transaccion transaccion) {
		try {
			Transaccion nuevaTransaccion = transaccionService.createTransaccion(transaccion);
			Map<String, Object> response = new HashMap<>();
			response.put("mensaje", "Transacción creada exitosamente");
			response.put("transaccion", nuevaTransaccion);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			Map<String, Object> response = new HashMap<>();
			response.put("mensaje", e.getMessage());
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("mensaje", "Error al procesar la transacción");
			response.put("detalleError", e.getMessage());
			return ResponseEntity.ok(response);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateTransaccion(@PathVariable Long id, @RequestBody Transaccion transaccionDetails) {
		try {
			Optional<Transaccion> updatedTransaccion = transaccionService.updateTransaccion(id, transaccionDetails);
			if (updatedTransaccion.isPresent()) {
				Map<String, Object> response = new HashMap<>();
				response.put("mensaje", "Transacción actualizada exitosamente");
				response.put("transaccion", updatedTransaccion.get());
				return ResponseEntity.ok(response);
			} else {
				Map<String, Object> response = new HashMap<>();
				response.put("mensaje", "No se encontró la transacción con el ID: " + id);
				return ResponseEntity.ok(response);
			}
		} catch (IllegalArgumentException e) {
			Map<String, Object> response = new HashMap<>();
			response.put("mensaje", e.getMessage());
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("mensaje", "Error al actualizar la transacción");
			response.put("detalleError", e.getMessage());
			return ResponseEntity.ok(response);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteTransaccion(@PathVariable Long id) {
		try {
			boolean isDeleted = transaccionService.deleteTransaccion(id);
			Map<String, Object> response = new HashMap<>();

			if (isDeleted) {
				response.put("mensaje", "Transacción eliminada exitosamente");
			} else {
				response.put("mensaje", "No se encontró la transacción con el ID: " + id);
			}

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("mensaje", "Error al intentar eliminar la transacción");
			response.put("detalleError", e.getMessage());
			return ResponseEntity.ok(response);
		}
	}
	
	 @PostMapping("/consignar")
	    public ResponseEntity<?> consignar(@RequestBody Transaccion transaccion) {
	        Map<String, Object> response = new HashMap<>();
	        try {
	            transaccion.setTipoTransaccion(Transaccion.TipoTransaccion.CONSIGNACION);
	            Transaccion resultado = transaccionService.realizarTransaccion(transaccion);
	            response.put("mensaje", "Consignación realizada exitosamente");
	            response.put("transaccion", resultado);
	            return ResponseEntity.ok(response);
	        } catch (Exception e) {
	            response.put("mensaje", "Error al realizar la consignación");
	            response.put("detalleError", e.getMessage());
	            return ResponseEntity.ok(response);
	        }
	    }

	    @PostMapping("/retirar")
	    public ResponseEntity<?> retirar(@RequestBody Transaccion transaccion) {
	        Map<String, Object> response = new HashMap<>();
	        try {
	            transaccion.setTipoTransaccion(Transaccion.TipoTransaccion.RETIRO);
	            Transaccion resultado = transaccionService.realizarTransaccion(transaccion);
	            response.put("mensaje", "Retiro realizado exitosamente");
	            response.put("transaccion", resultado);
	            return ResponseEntity.ok(response);
	        } catch (Exception e) {
	            response.put("mensaje", "Error al realizar el retiro");
	            response.put("detalleError", e.getMessage());
	            return ResponseEntity.ok(response);
	        }
	    }

	    @PostMapping("/transferir")
	    public ResponseEntity<?> transferir(@RequestBody Transaccion transaccion) {
	        Map<String, Object> response = new HashMap<>();
	        try {
	            transaccion.setTipoTransaccion(Transaccion.TipoTransaccion.TRANSFERENCIA);
	            Transaccion resultado = transaccionService.realizarTransaccion(transaccion);
	            response.put("mensaje", "Transferencia realizada exitosamente");
	            response.put("transaccion", resultado);
	            return ResponseEntity.ok(response);
	        } catch (Exception e) {
	            response.put("mensaje", "Error al realizar la transferencia");
	            response.put("detalleError", e.getMessage());
	            return ResponseEntity.ok(response);
	        }
	    }

	    @GetMapping("/producto/{productoId}")
	    public ResponseEntity<?> getTransaccionesByProductoId(@PathVariable Long productoId) {
	        Map<String, Object> response = new HashMap<>();
	        try {
	            List<Transaccion> transacciones = transaccionService.getTransaccionesByProductoId(productoId);
	            if (transacciones.isEmpty()) {
	                response.put("mensaje", "No se encontraron transacciones para el producto con ID: " + productoId);
	            } else {
	                response.put("mensaje", "Transacciones obtenidas exitosamente");
	                response.put("transacciones", transacciones);
	            }
	            return ResponseEntity.ok(response);
	        } catch (Exception e) {
	            response.put("mensaje", "Error al obtener las transacciones");
	            response.put("detalleError", e.getMessage());
	            return ResponseEntity.ok(response);
	        }
	    }
}
