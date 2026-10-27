package ar.edu.unju.escmi.dao;

import java.util.List;

import ar.edu.unju.escmi.entities.Producto;

public interface IProductoDao {
	public void guardar(Producto producto);
	public Producto buscarPorId(Long id);
	public List<Producto> listar();
	public void actualizar(Producto producto);
	public void eliminarLogico(Long id);
}
