# tp7_grupo5

Trabajo Práctico 7 — **Programación Orientada a Objetos**
Tecnicatura en Informática · Esc. de Minas "Dr. Horacio Carrillo" · UNJu

Aplicación de consola en Java que registra la **venta de productos a un cliente**
mediante **facturas**, utilizando **JPA (Hibernate)** para la persistencia de los
objetos en una base de datos **MySQL**.

## Integrantes

| Nombre y apellido | Usuario de GitHub |
|-------------------|-------------------|
| Leonardo Vargas   | @franclyc         |
| _(completar)_     | _(completar)_     |
| _(completar)_     | _(completar)_     |

> Completar la tabla con el resto de las y los integrantes del grupo y sus
> usuarios de GitHub.

## Tecnologías

- Java 21
- Maven
- JPA con Hibernate 5.6.7
- MySQL 8

## Estructura del proyecto

```
tp7_grupo5/
└── tp7_grupo5/
    ├── pom.xml
    └── src/main/
        ├── java/ar/edu/unju/escmi/
        │   ├── config/      → EmfSingleton (patrón Singleton)
        │   ├── entities/    → Cliente, Producto, Factura, DetalleFactura
        │   ├── dao/         → interfaces del patrón DAO
        │   │   └── imp/     → implementaciones de los DAO
        │   └── main/        → Main (menú de opciones)
        └── resources/META-INF/persistence.xml
```

El [diagrama de clases](docs/diagrama-de-clases.md) modela las entidades y sus
relaciones.

## Configuración de la base de datos

El archivo `src/main/resources/META-INF/persistence.xml` apunta a una base
`tp7_grupo5` en `localhost:3306`. Cada integrante debe ajustar el usuario y la
contraseña de MySQL a los suyos:

```xml
<property name="javax.persistence.jdbc.user" value="root"/>
<property name="javax.persistence.jdbc.password" value="abc_123"/>
```

La base se crea sola la primera vez (`createDatabaseIfNotExist=true`) y Hibernate
genera o actualiza las tablas (`hibernate.hbm2ddl.auto=update`).

## Cómo ejecutar

1. Tener MySQL en ejecución.
2. Desde la carpeta `tp7_grupo5/` (la que contiene el `pom.xml`):

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass=ar.edu.unju.escmi.main.Main
```

También puede ejecutarse desde el IDE corriendo la clase
`ar.edu.unju.escmi.main.Main`.

## Menú de opciones

1. Alta de cliente
2. Alta de producto
3. Realizar la venta de productos (alta de una nueva factura)
4. Buscar una factura por su número y mostrar todos sus datos
5. Eliminar una factura (baja lógica)
6. Eliminar un producto (baja lógica)
7. Modificar datos de un cliente
8. Modificar el precio de un producto
9. Eliminar un cliente (baja lógica)
10. Mostrar todas las facturas
11. Mostrar todos los clientes
12. Mostrar las facturas que superen los $900.000
0. Salir
