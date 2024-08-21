package com.entidadfinanciera.clienteservice;

import com.entidadfinanciera.clienteservice.model.Cliente;
import com.entidadfinanciera.clienteservice.repository.ClienteRepository;
import com.entidadfinanciera.clienteservice.service.ClienteService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente result = clienteService.createCliente(cliente);

        assertNotNull(result);
        assertEquals("Juan", result.getNombres());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void createCliente_MenorDeEdad_ThrowsException() {
        Cliente cliente = new Cliente();
        cliente.setFechaNacimiento(LocalDate.now().minusYears(17));

        assertThrows(IllegalArgumentException.class, () -> clienteService.createCliente(cliente));
    }

    @Test
    void deleteCliente_Success() {
        Long clienteId = 1L;
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);

        when(clienteRepository.findById(clienteId)).thenReturn(java.util.Optional.of(cliente));
        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(false);

        assertDoesNotThrow(() -> clienteService.deleteCliente(clienteId));
        verify(clienteRepository, times(1)).delete(cliente);
    }

    @Test
    void deleteCliente_ClienteConProductos_ThrowsException() {
        Long clienteId = 1L;
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);

        when(clienteRepository.findById(clienteId)).thenReturn(java.util.Optional.of(cliente));
        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> clienteService.deleteCliente(clienteId));
    }
}