package com.entidadfinanciera.clienteservice.controller;

import com.entidadfinanciera.clienteservice.dto.ProductoDTO;
import com.entidadfinanciera.clienteservice.model.Cliente;
import com.entidadfinanciera.clienteservice.service.ClienteService;
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
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@GetMapping
	public List<Cliente> getAllClientes() {
		return clienteService.getAllClientes();
	}

	@GetMapping("/{id}")
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
	public ResponseEntity<?> createCliente(@RequestBody Cliente cliente) {
		try {
			Cliente newCliente = clienteService.createCliente(cliente);
			return ResponseEntity.ok(newCliente);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	 @PutMapping("/{id}")
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
	public ResponseEntity<List<ProductoDTO>> getProductosDelCliente(@PathVariable Long clienteId) {
		List<ProductoDTO> productos = clienteService.getProductosDelCliente(clienteId);
		if (productos.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(productos);
	}
}