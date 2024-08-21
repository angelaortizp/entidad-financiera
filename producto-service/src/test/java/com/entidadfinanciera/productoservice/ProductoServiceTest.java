package com.entidadfinanciera.productoservice;

import com.entidadfinanciera.productoservice.model.Producto;
import com.entidadfinanciera.productoservice.repository.ProductoRepository;
import com.entidadfinanciera.productoservice.service.ProductoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProducto_Success() {
        Producto producto = new Producto();
        producto.setTipoCuenta(Producto.TipoCuenta.AHORROS);
        producto.setSaldo(new BigDecimal("1000.00"));

        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto result = productoService.createProducto(producto);

        assertNotNull(result);
        assertTrue(result.getNumeroCuenta().startsWith("53"));
        assertEquals(10, result.getNumeroCuenta().length());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void createProducto_SaldoNegativoAhorros_ThrowsException() {
        Producto producto = new Producto();
        producto.setTipoCuenta(Producto.TipoCuenta.AHORROS);
        producto.setSaldo(new BigDecimal("-100.00"));

        assertThrows(IllegalArgumentException.class, () -> productoService.createProducto(producto));
    }

    @Test
    void cancelarProducto_Success() {
        Long productoId = 1L;
        Producto producto = new Producto();
        producto.setId(productoId);
        producto.setSaldo(BigDecimal.ZERO);

        when(productoRepository.findById(productoId)).thenReturn(java.util.Optional.of(producto));

        assertDoesNotThrow(() -> productoService.cancelarProducto(productoId));
        assertEquals(Producto.EstadoCuenta.CANCELADA, producto.getEstado());
        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    void cancelarProducto_SaldoNoZero_ThrowsException() {
        Long productoId = 1L;
        Producto producto = new Producto();
        producto.setId(productoId);
        producto.setSaldo(new BigDecimal("100.00"));

        when(productoRepository.findById(productoId)).thenReturn(java.util.Optional.of(producto));

        assertThrows(IllegalStateException.class, () -> productoService.cancelarProducto(productoId));
    }
}