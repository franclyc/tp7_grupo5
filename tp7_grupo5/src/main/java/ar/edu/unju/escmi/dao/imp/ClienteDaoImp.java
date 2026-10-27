package ar.edu.unju.escmi.dao.imp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.IClienteDao;
import ar.edu.unju.escmi.entities.Cliente;

public class ClienteDaoImp implements IClienteDao {
	// Única fábrica de EntityManager de toda la aplicación (patrón Singleton)
	private EntityManagerFactory emf = EmfSingleton.getInstance().getEmf();

	@Override
	public void guardar(Cliente cliente) {
		EntityManager manager = emf.createEntityManager();
		try {
			manager.getTransaction().begin();
			manager.persist(cliente);
			manager.getTransaction().commit();
		} catch (Exception e) {
			if (manager.getTransaction().isActive()) {
				manager.getTransaction().rollback();
			}
			throw new PersistenceException("No se pudo guardar el cliente", e);
		} finally {
			manager.close();
		}
	}

	@Override
	public Cliente buscarPorId(Long id) {
		EntityManager manager = emf.createEntityManager();
		try {
			return manager.find(Cliente.class, id);
		} finally {
			manager.close();
		}
	}

	// Solo devuelve los clientes activos (los eliminados lógicamente tienen estado = false)
	@Override
	public List<Cliente> listar() {
		EntityManager manager = emf.createEntityManager();
		try {
			TypedQuery<Cliente> query = manager.createQuery(
					"SELECT c FROM Cliente c WHERE c.estado = true ORDER BY c.apellido, c.nombre", Cliente.class);
			return query.getResultList();
		} finally {
			manager.close();
		}
	}

	@Override
	public void actualizar(Cliente cliente) {
		EntityManager manager = emf.createEntityManager();
		try {
			manager.getTransaction().begin();
			manager.merge(cliente);
			manager.getTransaction().commit();
		} catch (Exception e) {
			if (manager.getTransaction().isActive()) {
				manager.getTransaction().rollback();
			}
			throw new PersistenceException("No se pudo modificar el cliente", e);
		} finally {
			manager.close();
		}
	}

	@Override
	public void eliminarLogico(Long id) {
		EntityManager manager = emf.createEntityManager();
		try {
			manager.getTransaction().begin();
			Cliente cliente = manager.find(Cliente.class, id);
			if (cliente == null) {
				throw new IllegalArgumentException("No existe un cliente con id " + id);
			}
			// El cliente está administrado por el manager: el cambio se guarda al hacer commit
			cliente.setEstado(false);
			manager.getTransaction().commit();
		} catch (Exception e) {
			if (manager.getTransaction().isActive()) {
				manager.getTransaction().rollback();
			}
			throw new PersistenceException("No se pudo eliminar el cliente con id " + id, e);
		} finally {
			manager.close();
		}
	}

}
