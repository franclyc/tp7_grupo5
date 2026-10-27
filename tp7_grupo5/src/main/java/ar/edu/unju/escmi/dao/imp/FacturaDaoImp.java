package ar.edu.unju.escmi.dao.imp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.IFacturaDao;
import ar.edu.unju.escmi.entities.Factura;

public class FacturaDaoImp implements IFacturaDao {
	// Única fábrica de EntityManager de toda la aplicación (patrón Singleton)
	private EntityManagerFactory emf = EmfSingleton.getInstance().getEmf();

	// Al persistir la factura también se guardan sus detalles (CascadeType.ALL en Factura.detalles)
	@Override
	public void guardar(Factura factura) {
		EntityManager manager = emf.createEntityManager();
		try {
			manager.getTransaction().begin();
			manager.persist(factura);
			manager.getTransaction().commit();
		} catch (Exception e) {
			if (manager.getTransaction().isActive()) {
				manager.getTransaction().rollback();
			}
			throw new PersistenceException("No se pudo guardar la factura", e);
		} finally {
			manager.close();
		}
	}

	@Override
	public Factura buscarPorId(Long id) {
		EntityManager manager = emf.createEntityManager();
		try {
			return manager.find(Factura.class, id);
		} finally {
			manager.close();
		}
	}

	// Consulta dinámica: solo devuelve las facturas activas
	@Override
	public List<Factura> listar() {
		EntityManager manager = emf.createEntityManager();
		try {
			TypedQuery<Factura> query = manager.createQuery(
					"SELECT f FROM Factura f WHERE f.estado = true ORDER BY f.id", Factura.class);
			return query.getResultList();
		} finally {
			manager.close();
		}
	}

	@Override
	public void eliminarLogico(Long id) {
		EntityManager manager = emf.createEntityManager();
		try {
			manager.getTransaction().begin();
			Factura factura = manager.find(Factura.class, id);
			if (factura == null) {
				throw new IllegalArgumentException("No existe una factura con id " + id);
			}
			// La factura está administrada por el manager: el cambio se guarda al hacer commit
			factura.setEstado(false);
			manager.getTransaction().commit();
		} catch (Exception e) {
			if (manager.getTransaction().isActive()) {
				manager.getTransaction().rollback();
			}
			throw new PersistenceException("No se pudo eliminar la factura con id " + id, e);
		} finally {
			manager.close();
		}
	}

	// Consulta estática: usa la @NamedQuery "Factura.mayoresA" declarada en la entidad Factura
	@Override
	public List<Factura> listarMayoresA(double total) {
		EntityManager manager = emf.createEntityManager();
		try {
			TypedQuery<Factura> query = manager.createNamedQuery("Factura.mayoresA", Factura.class);
			query.setParameter("total", total);
			return query.getResultList();
		} finally {
			manager.close();
		}
	}

}
