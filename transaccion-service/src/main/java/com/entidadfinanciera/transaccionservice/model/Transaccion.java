package com.entidadfinanciera.transaccionservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaccion")
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_transaccion", nullable = false)
    private TipoTransaccion tipoTransaccion;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(nullable = false)
    private BigDecimal monto;

    @NotNull
    @Column(name = "producto_origen_id", nullable = false)
    private Long productoOrigenId;

    @Column(name = "producto_destino_id")
    private Long productoDestinoId;

    @Column(name = "fecha_transaccion", nullable = false)
    private LocalDateTime fechaTransaccion = LocalDateTime.now();

    public enum TipoTransaccion {
        CONSIGNACION, RETIRO, TRANSFERENCIA
    }

    @PrePersist
    protected void onCreate() {
        fechaTransaccion = LocalDateTime.now();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoTransaccion getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(TipoTransaccion tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Long getProductoOrigenId() {
        return productoOrigenId;
    }

    public void setProductoOrigenId(Long productoOrigenId) {
        this.productoOrigenId = productoOrigenId;
    }

    public Long getProductoDestinoId() {
        return productoDestinoId;
    }

    public void setProductoDestinoId(Long productoDestinoId) {
        this.productoDestinoId = productoDestinoId;
    }

    public LocalDateTime getFechaTransaccion() {
        return fechaTransaccion;
    }

	public void setFechaTransaccion(LocalDateTime fechaTransaccion) {
		this.fechaTransaccion = fechaTransaccion;
	}
}