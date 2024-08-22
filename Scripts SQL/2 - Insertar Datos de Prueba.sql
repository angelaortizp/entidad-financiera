USE entidad_financiera;



-- Insertar clientes
INSERT INTO cliente (tipo_identificacion, numero_identificacion, nombres, apellidos, correo_electronico, fecha_nacimiento) VALUES
('CC', '1234567890', 'Juan', 'Pérez', 'juan.perez@email.com', '1990-05-15'),
('CC', '0987654321', 'María', 'González', 'maria.gonzalez@email.com', '1985-10-20'),
('CE', '1122334455', 'Carlos', 'Rodríguez', 'carlos.rodriguez@email.com', '1988-03-25');

-- Insertar productos (cuentas)
INSERT INTO producto (tipo_cuenta, numero_cuenta, estado, saldo, exenta_GMF, cliente_id) VALUES
('AHORROS', '5300000001', 'ACTIVA', 1000000.00, false, 1),
('CORRIENTE', '3300000001', 'ACTIVA', 5000000.00, true, 1),
('AHORROS', '5300000002', 'ACTIVA', 2500000.00, false, 2),
('CORRIENTE', '3300000002', 'ACTIVA', 7500000.00, true, 3);

-- Insertar transacciones
INSERT INTO transaccion (tipo_transaccion, monto, producto_origen_id, producto_destino_id) VALUES
('CONSIGNACION', 500000.00, 1, NULL),
('RETIRO', 200000.00, 2, NULL),
('TRANSFERENCIA', 100000.00, 3, 1),
('CONSIGNACION', 1000000.00, 4, NULL),
('RETIRO', 50000.00, 1, NULL);

-- Actualizar saldos de las cuentas después de las transacciones
UPDATE producto SET saldo = saldo + 500000.00 WHERE id = 1;
UPDATE producto SET saldo = saldo - 200000.00 WHERE id = 2;
UPDATE producto SET saldo = saldo - 100000.00 WHERE id = 3;
UPDATE producto SET saldo = saldo + 100000.00 WHERE id = 1;
UPDATE producto SET saldo = saldo + 1000000.00 WHERE id = 4;
UPDATE producto SET saldo = saldo - 50000.00 WHERE id = 1;