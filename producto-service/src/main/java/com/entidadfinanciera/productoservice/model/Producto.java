package com.entidadfinanciera.productoservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cuenta", nullable = false)
    private TipoCuenta tipoCuenta;

    @NotNull
    @Pattern(regexp = "^(53|33)\\d{8}$", message = "El número de cuenta debe tener 10 dígitos y comenzar con 53 para ahorros o 33 para corriente")
    @Column(name = "numero_cuenta", unique = true, length = 10)
    private String numeroCuenta;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCuenta estado;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true, message = "El saldo no puede ser negativo")
    @Column(nullable = false)
    private BigDecimal saldo;

    @NotNull
    @Column(name = "exenta_gmf", nullable = false)
    private Boolean exentaGMF;

    @NotNull
    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    public enum TipoCuenta {
        CORRIENTE, AHORROS
    }

    public enum EstadoCuenta {
        ACTIVA, INACTIVA, CANCELADA
    }

    @PrePersist
    protected void onCreate() {
    	this.numeroCuenta = generarNumeroCuenta();
        this.fechaCreacion = LocalDateTime.now();
    }
    
    private String generarNumeroCuenta() {
        String prefix = this.tipoCuenta == TipoCuenta.AHORROS ? "53" : "33";
        String randomPart = String.format("%08d", new Random().nextInt(100000000));
        return prefix + randomPart;
    }

    @PreUpdate
    protected void onUpdate() {
        fechaModificacion = LocalDateTime.now();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public EstadoCuenta getEstado() {
        return estado;
    }

    public void setEstado(EstadoCuenta estado) {
        this.estado = estado;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Boolean getExentaGMF() {
        return exentaGMF;
    }

    public void setExentaGMF(Boolean exentaGMF) {
        this.exentaGMF = exentaGMF;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }
}