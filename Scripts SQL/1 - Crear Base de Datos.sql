-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS entidad_financiera;
USE entidad_financiera;

-- Tabla CLIENTE
CREATE TABLE cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo_identificacion VARCHAR(20) NOT NULL,
    numero_identificacion VARCHAR(20) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    correo_electronico VARCHAR(100) NOT NULL UNIQUE,
    fecha_nacimiento DATE NOT NULL,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_nombres CHECK (LENGTH(nombres) >= 2),
    CONSTRAINT chk_apellidos CHECK (LENGTH(apellidos) >= 2),
    CONSTRAINT chk_correo_electronico CHECK (correo_electronico REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

-- Tabla PRODUCTO
CREATE TABLE producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo_cuenta ENUM('CORRIENTE', 'AHORROS') NOT NULL,
    numero_cuenta CHAR(10) NOT NULL UNIQUE,
    estado ENUM('ACTIVA', 'INACTIVA', 'CANCELADA') NOT NULL DEFAULT 'ACTIVA',
    saldo DECIMAL(15, 2) NOT NULL DEFAULT 0,
    exenta_GMF BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    cliente_id INT NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    CONSTRAINT chk_numero_cuenta CHECK (
        (tipo_cuenta = 'AHORROS' AND numero_cuenta LIKE '53%') OR
        (tipo_cuenta = 'CORRIENTE' AND numero_cuenta LIKE '33%')
    ),
    CONSTRAINT chk_saldo_ahorros CHECK (
        (tipo_cuenta = 'AHORROS' AND saldo >= 0) OR
        (tipo_cuenta = 'CORRIENTE')
    )
);

-- Tabla TRANSACCION
CREATE TABLE transaccion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo_transaccion ENUM('CONSIGNACION', 'RETIRO', 'TRANSFERENCIA') NOT NULL,
    monto DECIMAL(15, 2) NOT NULL,
    fecha_transaccion DATETIME DEFAULT CURRENT_TIMESTAMP,
    producto_origen_id INT NOT NULL,
    producto_destino_id INT,
    FOREIGN KEY (producto_origen_id) REFERENCES producto(id),
    FOREIGN KEY (producto_destino_id) REFERENCES producto(id)
);