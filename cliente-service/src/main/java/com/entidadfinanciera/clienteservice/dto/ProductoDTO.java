package com.entidadfinanciera.clienteservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductoDTO {

	private Long id;
	private String tipoCuenta;
	private String numeroCuenta;
	private String estado;
	private BigDecimal saldo;
	private boolean exentaGMF;
	private LocalDateTime fechaCreacion;
	private LocalDateTime fechaModificacion;
	private Long clienteId;

	public ProductoDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipoCuenta() {
		return tipoCuenta;
	}

	public void setTipoCuenta(String tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public boolean isExentaGMF() {
		return exentaGMF;
	}

	public void setExentaGMF(boolean exentaGMF) {
		this.exentaGMF = exentaGMF;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public LocalDateTime getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(LocalDateTime fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public Long getClienteId() {
		return clienteId;
	}

	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}

	@Override
	public String toString() {
		return "ProductoDTO{" + "id=" + id + ", tipoCuenta='" + tipoCuenta + '\'' + ", numeroCuenta='" + numeroCuenta
				+ '\'' + ", estado='" + estado + '\'' + ", saldo=" + saldo + ", exentaGMF=" + exentaGMF
				+ ", fechaCreacion=" + fechaCreacion + ", fechaModificacion=" + fechaModificacion + ", clienteId="
				+ clienteId + '}';
	}
}