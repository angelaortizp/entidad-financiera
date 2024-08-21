package com.entidadfinanciera.productoservice.controller;

import com.entidadfinanciera.productoservice.dto.ProductoDTO;
import com.entidadfinanciera.productoservice.model.Producto;
import com.entidadfinanciera.productoservice.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

	@Autowired
	private ProductoService productoService;

	@GetMapping
	public List<Producto> getAllProductos() {
		return productoService.getAllProductos();
	}

	@GetMapping("/{id}")
    public ResponseEntity<?> getProductoById(@PathVariable Long id) {
        Optional<Producto> producto = productoService.getProductoById(id);
        Map<String, Object> response = new HashMap<>();

        if (producto.isPresent()) {
            response.put("mensaje", "Producto encontrado exitosamente");
            response.put("producto", producto.get());
        } else {
            response.put("mensaje", "No se encontró el producto con el ID: " + id);
        }

        return ResponseEntity.ok(response);
    }
	
	 @GetMapping("/{id}/exists")
	    public ResponseEntity<Boolean> productoExists(@PathVariable Long id) {
	        boolean exists = productoService.existsById(id);
	        return ResponseEntity.ok(exists);
	    }

	 @PostMapping
	    public ResponseEntity<?> createProducto(@Valid @RequestBody Producto producto) {
	        Map<String, Object> response = new HashMap<>();
	        try {
	            Producto newProducto = productoService.createProducto(producto);
	            response.put("mensaje", "Producto creado exitosamente");
	            response.put("producto", newProducto);
	            return ResponseEntity.ok(response);
	        } catch (IllegalArgumentException e) {
	            response.put("mensaje", "Error al crear el producto");
	            response.put("error", e.getMessage());
	            return ResponseEntity.ok(response);
	        } catch (Exception e) {
	            response.put("mensaje", "Error inesperado al crear el producto");
	            response.put("error", e.getMessage());
	            return ResponseEntity.ok(response);
	        }
	    }

	 @PutMapping("/{id}")
	    public ResponseEntity<?> updateProducto(@PathVariable Long id, @Valid @RequestBody Producto productoDetails) {
	        Map<String, Object> response = new HashMap<>();
	        try {
	            Producto updatedProducto = productoService.updateProducto(id, productoDetails);
	            response.put("mensaje", "Producto actualizado exitosamente");
	            response.put("producto", updatedProducto);
	            return ResponseEntity.ok(response);
	        } catch (IllegalArgumentException e) {
	            response.put("mensaje", "No se encontró el producto con el ID: " + id);
	            return ResponseEntity.ok(response);
	        } catch (DataIntegrityViolationException e) {
	            response.put("mensaje", "Error de integridad de datos al actualizar el producto");
	            response.put("error", e.getMessage());
	            return ResponseEntity.ok(response);
	        } catch (Exception e) {
	            response.put("mensaje", "Error inesperado al actualizar el producto");
	            response.put("error", e.getMessage());
	            return ResponseEntity.ok(response);
	        }
	    }


	 @DeleteMapping("/{id}")
	    public ResponseEntity<?> deleteProducto(@PathVariable Long id) {
	        Map<String, Object> response = new HashMap<>();
	        try {
	            productoService.deleteProducto(id);
	            response.put("mensaje", "Producto eliminado exitosamente");
	            return ResponseEntity.ok(response);
	        } catch (IllegalArgumentException e) {
	            response.put("mensaje", "No se encontró el producto con el ID: " + id);
	            return ResponseEntity.ok(response);
	        } catch (IllegalStateException e) {
	            response.put("mensaje", "No se puede eliminar el producto");
	            response.put("error", e.getMessage());
	            return ResponseEntity.ok(response);
	        } catch (Exception e) {
	            response.put("mensaje", "Error inesperado al eliminar el producto");
	            response.put("error", e.getMessage());
	            return ResponseEntity.ok(response);
	        }
	    }

	@GetMapping("/cliente/{clienteId}/tiene-productos")
	public boolean clienteTieneProductos(@PathVariable Long clienteId) {
		return productoService.clienteTieneProductos(clienteId);
	}

	@GetMapping("/cliente/{clienteId}")
	public ResponseEntity<List<ProductoDTO>> getProductosByClienteId(@PathVariable Long clienteId) {
		
		System.out.println("ENTRO ACA");
	    List<Producto> productos = productoService.findByClienteId(clienteId);
	    List<ProductoDTO> productoDTOs = productos.stream().map(this::convertToDTO).collect(Collectors.toList());
	    return ResponseEntity.ok(productoDTOs);
	}


	private ProductoDTO convertToDTO(Producto producto) {
		ProductoDTO dto = new ProductoDTO();
		dto.setId(producto.getId());
		dto.setTipoCuenta(producto.getTipoCuenta().toString());
		dto.setNumeroCuenta(producto.getNumeroCuenta());
		dto.setEstado(producto.getEstado().toString());
		dto.setSaldo(producto.getSaldo());
		//dto.setExentaGMF(producto.isExentaGMF());
		dto.setFechaCreacion(producto.getFechaCreacion());
		dto.setFechaModificacion(producto.getFechaModificacion());
		dto.setClienteId(producto.getClienteId());
		return dto;
	}
}