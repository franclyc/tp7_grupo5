package ar.edu.unju.escmi.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "facturas")
public class Factura {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fac_id")
	private Long id;

	@Column(name = "fac_fecha", nullable = false)
	private LocalDate fecha;

	// Muchas facturas pertenecen a un cliente. Factura es la entidad propietaria (tiene la FK cli_id)
	@ManyToOne
	@JoinColumn(name = "cli_id", nullable = false)
	private Cliente cliente;

	@Column(name = "fac_domicilio", length = 100)
	private String domicilio;

	@Column(name = "fac_total")
	private double total;

	@Column(name = "fac_estado")
	private boolean estado;

	// Una factura tiene muchos detalles. El dueño de la relación es DetalleFactura (atributo "factura").
	// CascadeType.ALL: al guardar la factura se guardan también sus detalles.
	// EAGER: los detalles se traen junto con la factura para poder mostrarlos.
	@OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<DetalleFactura> detalles = new ArrayList<>();

	public Factura() {

	}

	public Factura(Cliente cliente) {
		this.fecha = LocalDate.now();
		this.cliente = cliente;
		this.domicilio = cliente.getDomicilio();
		this.total = 0;
		this.estado = true;
	}

	// Agrega el detalle manteniendo los dos lados de la relación y actualiza el total
	public void agregarDetalle(DetalleFactura detalle) {
		detalle.setFactura(this);
		this.detalles.add(detalle);
		this.total = calcularTotal();
	}

	public double calcularTotal() {
		double suma = 0;
		for (DetalleFactura detalle : detalles) {
			suma += detalle.getSubtotal();
		}
		return suma;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public List<DetalleFactura> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleFactura> detalles) {
		this.detalles = detalles;
	}

	@Override
	public String toString() {
		return "Factura [id=" + id + ", fecha=" + fecha + ", cliente=" + cliente.getNombre() + " "
				+ cliente.getApellido() + ", domicilio=" + domicilio + ", total=" + total + ", estado=" + estado + "]";
	}

}
