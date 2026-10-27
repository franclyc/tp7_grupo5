package ar.edu.unju.escmi.dao;

import java.util.List;

import ar.edu.unju.escmi.entities.DetalleFactura;

public interface IDetalleFacturaDao {
	public void guardar(DetalleFactura detalle);
	public List<DetalleFactura> listarPorFactura(Long idFactura);
}
