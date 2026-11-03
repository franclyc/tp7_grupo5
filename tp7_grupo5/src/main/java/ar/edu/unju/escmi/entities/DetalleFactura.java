package ar.edu.unju.escmi.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "detalles_factura")
public class DetalleFactura {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "det_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "prod_id", nullable = false)
	private Producto producto;

	@Column(name = "det_cantidad", nullable = false)
	private int cantidad;

	@Column(name = "det_precio_unitario", nullable = false)
	private double precioUnitario;

	@Column(name = "det_subtotal")
	private double subtotal;

	@ManyToOne
	@JoinColumn(name = "fac_id", nullable = false)
	private Factura factura;

	public DetalleFactura() {

	}

	public DetalleFactura(Producto producto, int cantidad) {
		this.producto = producto;
		this.cantidad = cantidad;
		this.precioUnitario = producto.getPrecioUnitario();
		this.subtotal = calcularSubtotal();
	}

	public double calcularSubtotal() {
		return cantidad * precioUnitario;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	@Override
	public String toString() {
		return "DetalleFactura [id=" + id + ", producto=" + producto.getDescripcion() + ", precioUnitario="
				+ precioUnitario + ", cantidad=" + cantidad + ", subtotal=" + subtotal + "]";
	}

}
