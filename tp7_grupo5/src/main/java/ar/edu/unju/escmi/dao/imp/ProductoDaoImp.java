package ar.edu.unju.escmi.dao.imp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.IProductoDao;
import ar.edu.unju.escmi.entities.Producto;

public class ProductoDaoImp implements IProductoDao {
	// Única fábrica de EntityManager de toda la aplicación (patrón Singleton)
	private EntityManagerFactory emf = EmfSingleton.getInstance().getEmf();

	@Override
	public void guardar(Producto producto) {
		EntityManager manager = emf.createEntityManager();
		try {
			manager.getTransaction().begin();
			manager.persist(producto);
			manager.getTransaction().commit();
		} catch (Exception e) {
			if (manager.getTransaction().isActive()) {
				manager.getTransaction().rollback();
			}
			throw new PersistenceException("No se pudo guardar el producto", e);
		} finally {
			manager.close();
		}
	}

	@Override
	public Producto buscarPorId(Long id) {
		EntityManager manager = emf.createEntityManager();
		try {
			return manager.find(Producto.class, id);
		} finally {
			manager.close();
		}
	}

	// Solo devuelve los productos activos (los eliminados lógicamente tienen estado = false)
	@Override
	public List<Producto> listar() {
		EntityManager manager = emf.createEntityManager();
		try {
			TypedQuery<Producto> query = manager.createQuery(
					"SELECT p FROM Producto p WHERE p.estado = true ORDER BY p.descripcion", Producto.class);
			return query.getResultList();
		} finally {
			manager.close();
		}
	}

	@Override
	public void actualizar(Producto producto) {
		EntityManager manager = emf.createEntityManager();
		try {
			manager.getTransaction().begin();
			manager.merge(producto);
			manager.getTransaction().commit();
		} catch (Exception e) {
			if (manager.getTransaction().isActive()) {
				manager.getTransaction().rollback();
			}
			throw new PersistenceException("No se pudo modificar el producto", e);
		} finally {
			manager.close();
		}
	}

	@Override
	public void eliminarLogico(Long id) {
		EntityManager manager = emf.createEntityManager();
		try {
			manager.getTransaction().begin();
			Producto producto = manager.find(Producto.class, id);
			if (producto == null) {
				throw new IllegalArgumentException("No existe un producto con id " + id);
			}
			// El producto está administrado por el manager: el cambio se guarda al hacer commit
			producto.setEstado(false);
			manager.getTransaction().commit();
		} catch (Exception e) {
			if (manager.getTransaction().isActive()) {
				manager.getTransaction().rollback();
			}
			throw new PersistenceException("No se pudo eliminar el producto con id " + id, e);
		} finally {
			manager.close();
		}
	}

}
