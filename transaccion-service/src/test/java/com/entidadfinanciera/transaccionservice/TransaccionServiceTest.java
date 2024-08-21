package com.entidadfinanciera.transaccionservice;

import com.entidadfinanciera.transaccionservice.model.Transaccion;
import com.entidadfinanciera.transaccionservice.repository.TransaccionRepository;
import com.entidadfinanciera.transaccionservice.service.TransaccionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransaccionServiceTest {

    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TransaccionService transaccionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void realizarTransaccion_Consignacion_Success() {
        Transaccion transaccion = new Transaccion();
        transaccion.setTipoTransaccion(Transaccion.TipoTransaccion.CONSIGNACION);
        transaccion.setMonto(new BigDecimal("100.00"));
        transaccion.setProductoOrigenId(1L);

        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(transaccion);

        Transaccion result = transaccionService.realizarTransaccion(transaccion);

        assertNotNull(result);
        assertEquals(Transaccion.TipoTransaccion.CONSIGNACION, result.getTipoTransaccion());
        verify(restTemplate, times(1)).postForObject(anyString(), any(), eq(Void.class));
    }

    @Test
    void realizarTransaccion_Transferencia_Success() {
        Transaccion transaccion = new Transaccion();
        transaccion.setTipoTransaccion(Transaccion.TipoTransaccion.TRANSFERENCIA);
        transaccion.setMonto(new BigDecimal("100.00"));
        transaccion.setProductoOrigenId(1L);
        transaccion.setProductoDestinoId(2L);

        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(transaccion);
        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(true);

        Transaccion result = transaccionService.realizarTransaccion(transaccion);

        assertNotNull(result);
        assertEquals(Transaccion.TipoTransaccion.TRANSFERENCIA, result.getTipoTransaccion());
        verify(restTemplate, times(2)).postForObject(anyString(), any(), eq(Void.class));
    }

    @Test
    void realizarTransaccion_TransferenciaProductoInexistente_ThrowsException() {
        Transaccion transaccion = new Transaccion();
        transaccion.setTipoTransaccion(Transaccion.TipoTransaccion.TRANSFERENCIA);
        transaccion.setProductoOrigenId(1L);
        transaccion.setProductoDestinoId(2L);

        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> transaccionService.realizarTransaccion(transaccion));
    }
}