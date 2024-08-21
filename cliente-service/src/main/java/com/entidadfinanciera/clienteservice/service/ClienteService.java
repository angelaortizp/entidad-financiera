package com.entidadfinanciera.clienteservice.service;

import com.entidadfinanciera.clienteservice.dto.ProductoDTO;
import com.entidadfinanciera.clienteservice.model.Cliente;
import com.entidadfinanciera.clienteservice.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	private final RestTemplate restTemplate;

	public ClienteService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	private final String productoServiceUrl = "http://localhost:8081/api/productos";

	public List<Cliente> getAllClientes() {
		return clienteRepository.findAll();
	}

	public Optional<Cliente> getClienteById(Long id) {
		return clienteRepository.findById(id);
	}

	public Cliente createCliente(Cliente cliente) {
		if (isMenorDeEdad(cliente.getFechaNacimiento())) {
			throw new IllegalArgumentException("El cliente debe ser mayor de edad");
		}
		if (clienteRepository.existsByNumeroIdentificacion(cliente.getNumeroIdentificacion())) {
			throw new IllegalArgumentException("Ya existe un cliente con este número de identificación");
		}
		return clienteRepository.save(cliente);
	}

	 @Transactional
	    public Cliente updateCliente(Long id, Cliente clienteDetails) {
	        Cliente cliente = clienteRepository.findById(id)
	                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con id: " + id));
	        
	        if (!cliente.getNumeroIdentificacion().equals(clienteDetails.getNumeroIdentificacion()) &&
	            clienteRepository.existsByNumeroIdentificacion(clienteDetails.getNumeroIdentificacion())) {
	            throw new DataIntegrityViolationException("El número de identificación ya existe");
	        }
	        
	        cliente.setTipoIdentificacion(clienteDetails.getTipoIdentificacion());
	        cliente.setNumeroIdentificacion(clienteDetails.getNumeroIdentificacion());
	        cliente.setNombres(clienteDetails.getNombres());
	        cliente.setApellidos(clienteDetails.getApellidos());
	        cliente.setCorreoElectronico(clienteDetails.getCorreoElectronico());
	        cliente.setFechaNacimiento(clienteDetails.getFechaNacimiento());

	        return clienteRepository.save(cliente);
	    }

	 @Transactional
	    public void deleteCliente(Long id) {
	        Cliente cliente = clienteRepository.findById(id)
	                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con id: " + id));
	        
	        
	        String url = productoServiceUrl + "/cliente/" + id + "/tiene-productos";
	        try {
	            Boolean tieneProductos = restTemplate.getForObject(url, Boolean.class);
	            if (tieneProductos != null && tieneProductos) {
	                throw new DataIntegrityViolationException("El cliente tiene productos asociados y no puede ser eliminado");
	            }
	        } catch (HttpClientErrorException.NotFound e) {
	            throw new RuntimeException("No se pudo verificar los productos del cliente. Servicio no disponible.");
	        } catch (RestClientException e) {
	            throw new RuntimeException("Error al comunicarse con el servicio de productos: " + e.getMessage());
	        }
	        
	        clienteRepository.delete(cliente);
	    }

	private boolean isMenorDeEdad(LocalDate fechaNacimiento) {
		return Period.between(fechaNacimiento, LocalDate.now()).getYears() < 18;
	}

	public List<ProductoDTO> getProductosDelCliente(Long clienteId) {
		return restTemplate.getForObject(productoServiceUrl + "/cliente/" + clienteId, List.class);
	}
}