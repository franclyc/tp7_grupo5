package ar.edu.unju.escmi.dao;

import java.util.List;

import ar.edu.unju.escmi.entities.Factura;

public interface IFacturaDao {
	public void guardar(Factura factura);
	public Factura buscarPorId(Long id);
	public List<Factura> listar();
	public void eliminarLogico(Long id);
	public List<Factura> listarMayoresA(double total);
}
