package ar.edu.unju.escmi.main;

import java.util.List;
import java.util.Scanner;

import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.IClienteDao;
import ar.edu.unju.escmi.dao.IFacturaDao;
import ar.edu.unju.escmi.dao.IProductoDao;
import ar.edu.unju.escmi.dao.imp.ClienteDaoImp;
import ar.edu.unju.escmi.dao.imp.FacturaDaoImp;
import ar.edu.unju.escmi.dao.imp.ProductoDaoImp;
import ar.edu.unju.escmi.entities.Cliente;
import ar.edu.unju.escmi.entities.DetalleFactura;
import ar.edu.unju.escmi.entities.Factura;
import ar.edu.unju.escmi.entities.Producto;

public class Main {

	private static final Scanner sc = new Scanner(System.in);

	private static final IClienteDao clienteDao = new ClienteDaoImp();
	private static final IProductoDao productoDao = new ProductoDaoImp();
	private static final IFacturaDao facturaDao = new FacturaDaoImp();

	private static final double TOTAL_MINIMO_FACTURAS = 900000;

	public static void main(String[] args) {
		int opcion = -1;
		try {
			do {
				mostrarMenu();
				opcion = leerEntero("Seleccione una opción: ");
				try {
					switch (opcion) {
						case 1 -> altaCliente();
						case 2 -> altaProducto();
						case 3 -> realizarVenta();
						case 4 -> buscarFactura();
						case 5 -> eliminarFactura();
						case 6 -> eliminarProducto();
						case 7 -> modificarCliente();
						case 8 -> modificarPrecioProducto();
						case 9 -> eliminarCliente();
						case 10 -> mostrarFacturas();
						case 11 -> mostrarClientes();
						case 12 -> mostrarFacturasMayoresA(TOTAL_MINIMO_FACTURAS);
						case 0 -> System.out.println("\nSaliendo del sistema...");
						default -> System.out.println("\nOpción inexistente. Intente nuevamente.");
					}
				} catch (Exception e) {
					System.out.println("\n[!] No se pudo completar la operación: " + obtenerMensajeDeError(e));
				}
			} while (opcion != 0);
		} finally {
			cerrarRecursos();
		}
	}

	private static void mostrarMenu() {
		System.out.println("\n===================== MENÚ PRINCIPAL =====================");
		System.out.println(" 1  - Alta de cliente");
		System.out.println(" 2  - Alta de producto");
		System.out.println(" 3  - Realizar la venta de productos (nueva factura)");
		System.out.println(" 4  - Buscar una factura por su número");
		System.out.println(" 5  - Eliminar una factura (baja lógica)");
		System.out.println(" 6  - Eliminar un producto (baja lógica)");
		System.out.println(" 7  - Modificar datos de un cliente");
		System.out.println(" 8  - Modificar el precio de un producto");
		System.out.println(" 9  - Eliminar un cliente (baja lógica)");
		System.out.println(" 10 - Mostrar todas las facturas");
		System.out.println(" 11 - Mostrar todos los clientes");
		System.out.println(" 12 - Mostrar las facturas que superen los $" + (long) TOTAL_MINIMO_FACTURAS);
		System.out.println(" 0  - Salir");
		System.out.println("==========================================================");
	}

	private static void altaCliente() {
		System.out.println("\n----- ALTA DE CLIENTE -----");
		String nombre = leerTexto("Nombre: ");
		String apellido = leerTexto("Apellido: ");
		String domicilio = leerTexto("Domicilio: ");
		int dni = leerEnteroPositivo("DNI: ");

		Cliente cliente = new Cliente(nombre, apellido, domicilio, dni);
		clienteDao.guardar(cliente);
		System.out.println("Cliente registrado correctamente: " + cliente);
	}

	private static void altaProducto() {
		System.out.println("\n----- ALTA DE PRODUCTO -----");
		String descripcion = leerTexto("Descripción: ");
		double precio = leerDoublePositivo("Precio unitario: ");

		Producto producto = new Producto(descripcion, precio);
		productoDao.guardar(producto);
		System.out.println("Producto registrado correctamente: " + producto);
	}

	private static void realizarVenta() {
		System.out.println("\n----- NUEVA VENTA (FACTURA) -----");

		List<Cliente> clientes = clienteDao.listar();
		if (clientes.isEmpty()) {
			System.out.println("No hay clientes registrados. Primero debe dar de alta un cliente (opción 1).");
			return;
		}
		mostrarListaClientes(clientes);
		Long idCliente = leerLong("Ingrese el id del cliente: ");
		Cliente cliente = clienteDao.buscarPorId(idCliente);
		if (cliente == null || !cliente.isEstado()) {
			System.out.println("No existe un cliente activo con el id " + idCliente + ".");
			return;
		}

		List<Producto> productos = productoDao.listar();
		if (productos.isEmpty()) {
			System.out.println("No hay productos registrados. Primero debe dar de alta un producto (opción 2).");
			return;
		}

		Factura factura = new Factura(cliente);
		boolean seguirCargando = true;
		while (seguirCargando) {
			mostrarListaProductos(productos);
			Long idProducto = leerLong("Ingrese el id del producto a vender: ");
			Producto producto = productoDao.buscarPorId(idProducto);
			if (producto == null || !producto.isEstado()) {
				System.out.println("No existe un producto activo con el id " + idProducto + ".");
			} else {
				int cantidad = leerEnteroPositivo("Cantidad: ");
				DetalleFactura detalle = new DetalleFactura(producto, cantidad);
				factura.agregarDetalle(detalle);
				System.out.println("  Agregado: " + detalle);
			}
			seguirCargando = leerConfirmacion("¿Desea agregar otro producto? (s/n): ");
		}

		if (factura.getDetalles().isEmpty()) {
			System.out.println("La factura no tiene productos cargados. Venta cancelada.");
			return;
		}

		facturaDao.guardar(factura);
		System.out.println("\nVenta registrada correctamente.");
		mostrarFacturaCompleta(factura);
	}

	private static void buscarFactura() {
		System.out.println("\n----- BUSCAR FACTURA -----");
		Long id = leerLong("Ingrese el número (id) de la factura: ");
		Factura factura = facturaDao.buscarPorId(id);
		if (factura == null) {
			System.out.println("No existe una factura con el número " + id + ".");
			return;
		}
		mostrarFacturaCompleta(factura);
	}

	private static void eliminarFactura() {
		System.out.println("\n----- ELIMINAR FACTURA -----");
		Long id = leerLong("Ingrese el número (id) de la factura a eliminar: ");
		facturaDao.eliminarLogico(id);
		System.out.println("Factura " + id + " eliminada (baja lógica) correctamente.");
	}

	private static void eliminarProducto() {
		System.out.println("\n----- ELIMINAR PRODUCTO -----");
		List<Producto> productos = productoDao.listar();
		if (productos.isEmpty()) {
			System.out.println("No hay productos activos para eliminar.");
			return;
		}
		mostrarListaProductos(productos);
		Long id = leerLong("Ingrese el id del producto a eliminar: ");
		productoDao.eliminarLogico(id);
		System.out.println("Producto " + id + " eliminado (baja lógica) correctamente.");
	}

	private static void modificarCliente() {
		System.out.println("\n----- MODIFICAR CLIENTE -----");
		List<Cliente> clientes = clienteDao.listar();
		if (clientes.isEmpty()) {
			System.out.println("No hay clientes activos para modificar.");
			return;
		}
		mostrarListaClientes(clientes);
		Long id = leerLong("Ingrese el id del cliente a modificar: ");
		Cliente cliente = clienteDao.buscarPorId(id);
		if (cliente == null || !cliente.isEstado()) {
			System.out.println("No existe un cliente activo con el id " + id + ".");
			return;
		}

		System.out.println("Deje el campo vacío y presione ENTER para conservar el valor actual.");
		cliente.setNombre(leerTextoConValorActual("Nombre", cliente.getNombre()));
		cliente.setApellido(leerTextoConValorActual("Apellido", cliente.getApellido()));
		cliente.setDomicilio(leerTextoConValorActual("Domicilio", cliente.getDomicilio()));

		clienteDao.actualizar(cliente);
		System.out.println("Cliente modificado correctamente: " + cliente);
	}

	private static void modificarPrecioProducto() {
		System.out.println("\n----- MODIFICAR PRECIO DE PRODUCTO -----");
		List<Producto> productos = productoDao.listar();
		if (productos.isEmpty()) {
			System.out.println("No hay productos activos para modificar.");
			return;
		}
		mostrarListaProductos(productos);
		Long id = leerLong("Ingrese el id del producto a modificar: ");
		Producto producto = productoDao.buscarPorId(id);
		if (producto == null || !producto.isEstado()) {
			System.out.println("No existe un producto activo con el id " + id + ".");
			return;
		}

		System.out.println("Precio actual: $" + producto.getPrecioUnitario());
		double nuevoPrecio = leerDoublePositivo("Nuevo precio unitario: ");
		producto.setPrecioUnitario(nuevoPrecio);

		productoDao.actualizar(producto);
		System.out.println("Precio modificado correctamente: " + producto);
	}

	private static void eliminarCliente() {
		System.out.println("\n----- ELIMINAR CLIENTE -----");
		List<Cliente> clientes = clienteDao.listar();
		if (clientes.isEmpty()) {
			System.out.println("No hay clientes activos para eliminar.");
			return;
		}
		mostrarListaClientes(clientes);
		Long id = leerLong("Ingrese el id del cliente a eliminar: ");
		clienteDao.eliminarLogico(id);
		System.out.println("Cliente " + id + " eliminado (baja lógica) correctamente.");
	}

	private static void mostrarFacturas() {
		System.out.println("\n----- LISTADO DE FACTURAS -----");
		List<Factura> facturas = facturaDao.listar();
		if (facturas.isEmpty()) {
			System.out.println("No hay facturas registradas.");
			return;
		}
		for (Factura factura : facturas) {
			mostrarFacturaCompleta(factura);
		}
	}

	private static void mostrarClientes() {
		System.out.println("\n----- LISTADO DE CLIENTES -----");
		List<Cliente> clientes = clienteDao.listar();
		if (clientes.isEmpty()) {
			System.out.println("No hay clientes registrados.");
			return;
		}
		mostrarListaClientes(clientes);
	}

	private static void mostrarFacturasMayoresA(double total) {
		System.out.println("\n----- FACTURAS CON TOTAL MAYOR A $" + (long) total + " -----");
		List<Factura> facturas = facturaDao.listarMayoresA(total);
		if (facturas.isEmpty()) {
			System.out.println("No hay facturas que superen los $" + (long) total + ".");
			return;
		}
		for (Factura factura : facturas) {
			mostrarFacturaCompleta(factura);
		}
	}

	private static void mostrarListaClientes(List<Cliente> clientes) {
		System.out.println("Clientes:");
		for (Cliente cliente : clientes) {
			System.out.println("  " + cliente);
		}
	}

	private static void mostrarListaProductos(List<Producto> productos) {
		System.out.println("Productos:");
		for (Producto producto : productos) {
			System.out.println("  " + producto);
		}
	}

	private static void mostrarFacturaCompleta(Factura factura) {
		System.out.println("\n" + factura);
		System.out.println("  Detalles:");
		if (factura.getDetalles() == null || factura.getDetalles().isEmpty()) {
			System.out.println("    (sin detalles)");
		} else {
			for (DetalleFactura detalle : factura.getDetalles()) {
				System.out.println("    " + detalle);
			}
		}
	}

	private static int leerEntero(String mensaje) {
		while (true) {
			System.out.print(mensaje);
			String linea = sc.nextLine().trim();
			try {
				return Integer.parseInt(linea);
			} catch (NumberFormatException e) {
				System.out.println("  Valor inválido. Ingrese un número entero.");
			}
		}
	}

	private static int leerEnteroPositivo(String mensaje) {
		while (true) {
			int valor = leerEntero(mensaje);
			if (valor > 0) {
				return valor;
			}
			System.out.println("  El valor debe ser mayor a cero.");
		}
	}

	private static Long leerLong(String mensaje) {
		while (true) {
			System.out.print(mensaje);
			String linea = sc.nextLine().trim();
			try {
				return Long.parseLong(linea);
			} catch (NumberFormatException e) {
				System.out.println("  Valor inválido. Ingrese un número entero.");
			}
		}
	}

	private static double leerDoublePositivo(String mensaje) {
		while (true) {
			System.out.print(mensaje);
			String linea = sc.nextLine().trim().replace(",", ".");
			try {
				double valor = Double.parseDouble(linea);
				if (Double.isFinite(valor) && valor > 0) {
					return valor;
				}
				System.out.println("  El valor debe ser un número mayor a cero.");
			} catch (NumberFormatException e) {
				System.out.println("  Valor inválido. Ingrese un número (por ejemplo 1500.50).");
			}
		}
	}

	private static String leerTexto(String mensaje) {
		while (true) {
			System.out.print(mensaje);
			String linea = sc.nextLine().trim();
			if (!linea.isEmpty()) {
				return linea;
			}
			System.out.println("  El dato no puede quedar vacío.");
		}
	}

	private static String leerTextoConValorActual(String etiqueta, String valorActual) {
		System.out.print(etiqueta + " [" + valorActual + "]: ");
		String linea = sc.nextLine().trim();
		return linea.isEmpty() ? valorActual : linea;
	}

	private static boolean leerConfirmacion(String mensaje) {
		while (true) {
			System.out.print(mensaje);
			String linea = sc.nextLine().trim().toLowerCase();
			if (linea.equals("s") || linea.equals("si") || linea.equals("sí")) {
				return true;
			}
			if (linea.equals("n") || linea.equals("no")) {
				return false;
			}
			System.out.println("  Responda 's' (sí) o 'n' (no).");
		}
	}

	private static String obtenerMensajeDeError(Exception e) {
		Throwable causa = e;
		while (causa.getCause() != null) {
			causa = causa.getCause();
		}
		String mensaje = causa.getMessage();
		return (mensaje != null) ? mensaje : causa.getClass().getSimpleName();
	}

	private static void cerrarRecursos() {
		try {
			EmfSingleton.getInstance().cerrar();
		} catch (Exception e) {
		}
	}

}
