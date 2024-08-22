package com.entidadfinanciera.clienteservice;

import com.entidadfinanciera.clienteservice.model.Cliente;
import com.entidadfinanciera.clienteservice.repository.ClienteRepository;
import com.entidadfinanciera.clienteservice.service.ClienteService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

	@Mock
	private ClienteRepository clienteRepository;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private ClienteService clienteService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void createCliente_Success() {
		Cliente cliente = new Cliente();
		cliente.setNombres("Juan");
		cliente.setApellidos("Perez");
		cliente.setFechaNacimiento(LocalDate.now().minusYears(25));
		cliente.setNumeroIdentificacion("1234567890");
		cliente.setTipoIdentificacion("CC");
		cliente.setCorreoElectronico("juan.perez@example.com");

		when(clienteRepository.existsByNumeroIdentificacion(anyString())).thenReturn(false);
		when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

		Cliente result = clienteService.createCliente(cliente);

		assertNotNull(result);
		assertEquals("Juan", result.getNombres());
		verify(clienteRepository, times(1)).existsByNumeroIdentificacion(anyString());
		verify(clienteRepository, times(1)).save(any(Cliente.class));
	}

	@Test
	void createCliente_MenorDeEdad_ThrowsException() {
		Cliente cliente = new Cliente();
		cliente.setNombres("Juan");
		cliente.setApellidos("Perez");
		cliente.setFechaNacimiento(LocalDate.now().minusYears(17));
		cliente.setNumeroIdentificacion("1234567890");
		cliente.setTipoIdentificacion("CC");
		cliente.setCorreoElectronico("juan.perez@example.com");

		assertThrows(IllegalArgumentException.class, () -> clienteService.createCliente(cliente));
		verify(clienteRepository, never()).save(any(Cliente.class));
	}

}
