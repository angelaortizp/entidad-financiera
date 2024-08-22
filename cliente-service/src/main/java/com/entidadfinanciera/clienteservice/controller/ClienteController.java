package com.entidadfinanciera.clienteservice.controller;

import com.entidadfinanciera.clienteservice.dto.ProductoDTO;
import com.entidadfinanciera.clienteservice.model.Cliente;
import com.entidadfinanciera.clienteservice.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Cliente", description = "API para operaciones con clientes")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@GetMapping
	@Operation(summary = "Obtener todos los clientes", description = "Retorna una lista de todos los clientes")
	@ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente", content = @Content(schema = @Schema(implementation = Cliente.class)))
	public List<Cliente> getAllClientes() {
		return clienteService.getAllClientes();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Obtener un cliente por ID", description = "Retorna un cliente basado en su ID")
	@ApiResponse(responseCode = "200", description = "Cliente encontrado", content = @Content(schema = @Schema(implementation = Cliente.class)))
	@ApiResponse(responseCode = "404", description = "Cliente no encontrado")
	public ResponseEntity<?> getClienteById(@PathVariable Long id) {
		Optional<Cliente> cliente = clienteService.getClienteById(id);
		Map<String, Object> response = new HashMap<>();

		if (cliente.isPresent()) {
			response.put("mensaje", "Cliente encontrado exitosamente");
			response.put("cliente", cliente.get());
		} else {
			response.put("mensaje", "No se encontró el cliente con el ID: " + id);
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping
	@Operation(summary = "Crear un nuevo cliente", description = "Crea un nuevo cliente y lo retorna")
	@ApiResponse(responseCode = "201", description = "Cliente creado exitosamente", content = @Content(schema = @Schema(implementation = Cliente.class)))
	@ApiResponse(responseCode = "400", description = "Datos de cliente inválidos")
	public ResponseEntity<?> createCliente(@RequestBody Cliente cliente) {
		Map<String, Object> response = new HashMap<>();
		try {
			Cliente newCliente = clienteService.createCliente(cliente);
			response.put("mensaje", "Cliente creado exitosamente");
			response.put("cliente", newCliente);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			response.put("mensaje", "Error al crear el cliente");
			response.put("error", e.getMessage());
			return ResponseEntity.ok(response);
		} catch (DataIntegrityViolationException e) {
			response.put("mensaje", "Error al crear el cliente: El número de identificación ya existe");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("mensaje", "Error inesperado al crear el cliente");
			response.put("error", e.getMessage());
			return ResponseEntity.ok(response);
		}
	}

	@PutMapping("/{id}")
	@Operation(summary = "Actualizar un cliente", description = "Actualiza un cliente existente")
	@ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente", content = @Content(schema = @Schema(implementation = Cliente.class)))
	@ApiResponse(responseCode = "404", description = "Cliente no encontrado")
	public ResponseEntity<?> updateCliente(@PathVariable Long id, @RequestBody Cliente clienteDetails) {
		try {
			Cliente updatedCliente = clienteService.updateCliente(id, clienteDetails);
			Map<String, Object> response = new HashMap<>();
			response.put("mensaje", "Cliente actualizado exitosamente");
			response.put("cliente", updatedCliente);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			Map<String, Object> response = new HashMap<>();
			response.put("mensaje", "No se encontró el cliente con el ID: " + id);
			return ResponseEntity.ok(response);
		} catch (DataIntegrityViolationException e) {
			Map<String, Object> response = new HashMap<>();
			response.put("mensaje", "Error al actualizar el cliente: El número de identificación ya existe");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("mensaje", "Error al actualizar el cliente");
			response.put("error", e.getMessage());
			return ResponseEntity.ok(response);
		}
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Eliminar un cliente", description = "Elimina un cliente existente")
	@ApiResponse(responseCode = "200", description = "Cliente eliminado exitosamente")
	@ApiResponse(responseCode = "404", description = "Cliente no encontrado")
	public ResponseEntity<?> deleteCliente(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		try {
			clienteService.deleteCliente(id);
			response.put("mensaje", "Cliente eliminado exitosamente");
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			response.put("mensaje", "No se encontró el cliente con el ID: " + id);
			return ResponseEntity.ok(response);
		} catch (DataIntegrityViolationException e) {
			response.put("mensaje", "No se puede eliminar el cliente porque tiene productos asociados");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("mensaje", "Error al intentar eliminar el cliente");
			response.put("error", e.getMessage());
			return ResponseEntity.ok(response);
		}
	}

	@GetMapping("/{clienteId}/productos")
	@Operation(summary = "Obtener productos de un cliente", description = "Retorna una lista de productos asociados a un cliente")
	@ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente", content = @Content(schema = @Schema(implementation = ProductoDTO.class)))
	@ApiResponse(responseCode = "404", description = "Cliente no encontrado")
	public ResponseEntity<?> getProductosDelCliente(@PathVariable Long clienteId) {
		Map<String, Object> response = new HashMap<>();
		try {
			List<ProductoDTO> productos = clienteService.getProductosDelCliente(clienteId);
			if (productos.isEmpty()) {
				response.put("mensaje", "El cliente no tiene productos asociados");
				return ResponseEntity.ok(response);
			} else {
				response.put("mensaje", "Productos obtenidos exitosamente");
				response.put("productos", productos);
				return ResponseEntity.ok(response);
			}
		} catch (IllegalArgumentException e) {
			response.put("mensaje", "Error al obtener los productos del cliente");
			response.put("error", "No se encontró el cliente con el ID: " + clienteId);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("mensaje", "Error inesperado al obtener los productos del cliente");
			response.put("error", e.getMessage());
			return ResponseEntity.ok(response);
		}
	}
}