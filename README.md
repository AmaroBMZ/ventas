# Microservicio Ventas

Microservicio encargado de registrar ventas y coordinar el descuento de stock con los microservicios de Inventario y Bodega mediante `RestTemplate`.

## Funcionalidad

- Crear ventas.
- Listar ventas registradas.
- Buscar una venta por ID.
- Actualizar ventas.
- Eliminar ventas.
- Descontar stock en Inventario al crear una venta.
- Descontar stock en Bodega al crear una venta.

## Flujo al crear una venta

Cuando se registra una venta, el servicio realiza este proceso:

1. Recibe los datos de la venta.
2. Envia una solicitud al microservicio Inventario para descontar stock.
3. Envia una solicitud al microservicio Bodega para descontar stock.
4. Si ambos microservicios responden correctamente, guarda la venta en la base de datos.
5. Si Inventario o Bodega falla, la venta no se registra.

## Modelo Venta

Campos principales:

- `idVenta`
- `fechaVenta`
- `totalVenta`
- `descuentoVenta`
- `estadoVenta`
- `idPerfume`
- `idSucursal`
- `cantidad`

## Endpoints

URL base:

```text
http://localhost:8094/api/v1/venta
```

### Crear venta

```http
POST /api/v1/venta
```

Ejemplo de JSON:

```json
{
  "fechaVenta": "2026-06-12",
  "totalVenta": 59990,
  "descuentoVenta": 0,
  "estadoVenta": "PAGADA",
  "idPerfume": 1,
  "idSucursal": 1,
  "cantidad": 2
}
```

Al crear la venta, se envia este JSON a Inventario y Bodega:

```json
{
  "idProducto": 1,
  "idSucursal": 1,
  "cantidad": 2,
  "motivo": "VENTA"
}
```

### Listar ventas

```http
GET /api/v1/venta
```

### Buscar venta por ID

```http
GET /api/v1/venta/{id}
```

### Actualizar venta

```http
PUT /api/v1/venta/{id}
```

### Eliminar venta

```http
DELETE /api/v1/venta/{id}
```

## Configuracion de microservicios externos

Las URLs de Inventario y Bodega se configuran en `src/main/resources/application.properties`:

```properties
microservices.inventario.descontar-stock-url=http://localhost:8092/api/v1/inventario/descontar-stock
microservices.bodega.descontar-stock-url=http://localhost:8093/api/v1/bodega/descontar-stock
```

Si los otros microservicios usan puertos o rutas diferentes, se deben modificar esas propiedades.

## Base de datos

Configuracion actual:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/venta_bd
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
```

## Ejecutar el proyecto

```bash
./mvnw spring-boot:run
```

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

## Compilar

```powershell
.\mvnw.cmd -DskipTests package
```
