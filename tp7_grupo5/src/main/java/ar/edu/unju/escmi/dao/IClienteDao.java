package ar.edu.unju.escmi.dao;

import java.util.List;

import ar.edu.unju.escmi.entities.Cliente;

public interface IClienteDao {
	public void guardar(Cliente cliente);
	public Cliente buscarPorId(Long id);
	public List<Cliente> listar();
	public void actualizar(Cliente cliente);
	public void eliminarLogico(Long id);
}
