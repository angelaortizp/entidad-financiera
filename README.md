# Entidad Financiera

Este proyecto es un sistema de gestión de entidades financieras basado en microservicios. Consta de tres servicios principales: Servicio de Cliente, Servicio de Producto y Servicio de Transacción. Estos servicios trabajan juntos para proporcionar una solución integral para la gestión de clientes, productos financieros y transacciones.

## Estructura del Proyecto

El proyecto está organizado como un proyecto Maven multi-módulo con la siguiente estructura:

```
entidad-financiera/
├── cliente-service/
├── producto-service/
├── transaccion-service/
└── pom.xml
```

## Servicios

### 1. Servicio de Cliente

Este servicio gestiona la información de los clientes. Proporciona funcionalidades como:

- Creación de nuevos clientes
- Actualización de información de clientes
- Recuperación de detalles de clientes
- Eliminación de clientes (con verificaciones de productos asociados)

### 2. Servicio de Producto

Este servicio maneja los productos financieros. Sus principales características incluyen:

- Creación de nuevos productos financieros (por ejemplo, cuentas de ahorro, cuentas corrientes)
- Actualización de información de productos
- Recuperación de detalles de productos
- Gestión del estado de los productos (activo, inactivo, cancelado)
- Manejo de actualizaciones de saldo de productos

### 3. Servicio de Transacción

Este servicio gestiona las transacciones financieras. Ofrece funcionalidades como:

- Procesamiento de diferentes tipos de transacciones (depósitos, retiros, transferencias)
- Registro del historial de transacciones
- Actualización de saldos de cuentas mediante la interacción con el Servicio de Producto

## Stack Tecnológico

- Java 17
- Spring Boot 3.1.2
- Maven
- JPA / Hibernate
- APIs RESTful
- MySQL

## Primeros Pasos

### Preparación de la Base de Datos

1. Localiza los siguientes archivos SQL en el directorio raíz del proyecto:
   - `1 - Crear Base de Datos.sql`
   - `2 - Insertar Datos de Prueba.sql`

2. Ejecuta estos scripts en tu sistema de gestión de bases de datos en el orden indicado:
   a. Primero, ejecuta `1 - Crear Base de Datos.sql` para crear la estructura de la base de datos.
   b. Luego, ejecuta `2 - Insertar Datos de Prueba.sql` para poblar la base de datos con datos de prueba.

### Configuración del Proyecto

3. Clona el repositorio:
   ```
   git clone https://github.com/angelaortizp/entidad-financiera.git
   ```

4. Navega al directorio del proyecto:
   ```
   cd entidad-financiera
   ```

5. Construye el proyecto:
   ```
   mvn clean install
   ```

6. Importa el proyecto en Spring Tool Suite 4 (STS4):
   a. Abre Spring Tool Suite 4.
   b. Ve a "File" > "Import".
   c. Selecciona "Maven" > "Existing Maven Projects".
   d. Navega hasta el directorio donde clonaste el repositorio y selecciona el proyecto.
   e. Haz clic en "Finish".

7. Configura cada servicio:
   Para cada uno de los servicios (cliente-service, producto-service, transaccion-service):
   a. Abre el archivo `src/main/resources/application.properties`.
   b. Configura las propiedades de conexión a la base de datos según sea necesario.

8. Ejecuta cada servicio:
   Para cada servicio, haz clic derecho en el proyecto > "Run As" > "Spring Boot App".

## Configuración

Cada servicio tiene su propio archivo `application.properties` para la configuración. Asegúrate de configurar las conexiones de base de datos necesarias y otras propiedades antes de ejecutar los servicios.

## Documentación de la API

La documentación de la API para cada servicio se puede encontrar en sus respectivas páginas de Swagger UI:

- Servicio de Cliente: `http://localhost:8080/swagger-ui.html`
- Servicio de Producto: `http://localhost:8081/swagger-ui.html`
- Servicio de Transacción: `http://localhost:8082/swagger-ui.html`

## Importación de Colecciones de Postman

Para probar las APIs de los servicios, puedes importar las siguientes colecciones de Postman:

1. Localiza los siguientes archivos en el directorio raíz del proyecto:
   - `Cliente Service API.postman_collection.json`
   - `Producto Service API.postman_collection.json`
   - `Transaccion Service API.postman_collection.json`

2. Abre Postman.

3. Haz clic en "Import" en la esquina superior izquierda.

4. Arrastra y suelta los archivos de colección o haz clic en "Upload Files" y selecciona los archivos.

5. Confirma la importación.

Ahora tendrás acceso a todas las solicitudes predefinidas para probar los servicios de Cliente, Producto y Transacción.
