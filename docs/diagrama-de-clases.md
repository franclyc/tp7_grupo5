# Diagrama de clases

Modelo de clases y relaciones del problema: el registro de una **factura**
resultado de la venta de **productos** a un **cliente**. GitHub renderiza el
diagrama automáticamente.

```mermaid
classDiagram
    class Cliente {
        -Long id
        -String nombre
        -String apellido
        -String domicilio
        -int dni
        -boolean estado
    }

    class Producto {
        -Long id
        -String descripcion
        -double precioUnitario
        -boolean estado
    }

    class Factura {
        -Long id
        -LocalDate fecha
        -String domicilio
        -double total
        -boolean estado
        +agregarDetalle(DetalleFactura detalle) void
        +calcularTotal() double
    }

    class DetalleFactura {
        -Long id
        -int cantidad
        -double subtotal
        +calcularSubtotal() double
    }

    Factura "muchas" --> "1" Cliente : cliente
    Factura "1" o-- "0..*" DetalleFactura : detalles
    DetalleFactura "muchos" --> "1" Producto : producto
```

## Relaciones

- **Factura → Cliente** (`@ManyToOne`): muchas facturas pueden pertenecer a un
  mismo cliente. La entidad **propietaria** es `Factura` (tiene la clave foránea
  `cli_id`).
- **Factura → DetalleFactura** (`@OneToMany(mappedBy = "factura")`): una factura
  está compuesta por muchos detalles. La relación es **bidireccional** y el lado
  propietario es `DetalleFactura`. Usa `CascadeType.ALL`, por lo que al guardar
  una factura se guardan también sus detalles, y `FetchType.EAGER` para traerlos
  junto con la factura.
- **DetalleFactura → Producto** (`@ManyToOne`): cada detalle hace referencia a un
  producto. La entidad propietaria es `DetalleFactura` (tiene la clave foránea
  `prod_id`).

El `subtotal` de cada `DetalleFactura` se calcula como
`cantidad * producto.precioUnitario`, y el `total` de la `Factura` es la suma de
los subtotales de sus detalles.
