package com.entidadfinanciera.clienteservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "cliente")
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(name = "tipo_identificacion", nullable = false)
	private String tipoIdentificacion;

	@NotBlank
	@Column(name = "numero_identificacion", nullable = false, unique = true)
	private String numeroIdentificacion;

	@NotBlank
	@Size(min = 2, message = "El nombre debe tener al menos 2 caracteres")
	@Column(nullable = false)
	private String nombres;

	@NotBlank
	@Size(min = 2, message = "El apellido debe tener al menos 2 caracteres")
	@Column(nullable = false)
	private String apellidos;

	@NotBlank
	@Email(message = "El correo electrónico debe ser válido")
	@Column(name = "correo_electronico", nullable = false, unique = true)
	private String correoElectronico;

	@NotNull
	@Past(message = "La fecha de nacimiento debe ser en el pasado")
	@Column(name = "fecha_nacimiento", nullable = false)
	private LocalDate fechaNacimiento;

	@Column(name = "fecha_creacion", nullable = false, updatable = false)
	private LocalDateTime fechaCreacion;

	@Column(name = "fecha_modificacion")
	private LocalDateTime fechaModificacion;

	@PrePersist
	protected void onCreate() {
		fechaCreacion = LocalDateTime.now();
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

	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public LocalDateTime getFechaModificacion() {
		return fechaModificacion;
	}

}