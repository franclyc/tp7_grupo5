package ar.edu.unju.escmi.dao.imp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.IDetalleFacturaDao;
import ar.edu.unju.escmi.entities.DetalleFactura;

public class DetalleFacturaDaoImp implements IDetalleFacturaDao {
	// Única fábrica de EntityManager de toda la aplicación (patrón Singleton)
	private EntityManagerFactory emf = EmfSingleton.getInstance().getEmf();

	// Normalmente no hace falta: los detalles se guardan junto con la factura por la cascada.
	// Sirve para agregar un detalle a una factura que ya existe (el detalle debe tener su factura asignada).
	@Override
	public void guardar(DetalleFactura detalle) {
		EntityManager manager = emf.createEntityManager();
		try {
			manager.getTransaction().begin();
			manager.persist(detalle);
			manager.getTransaction().commit();
		} catch (Exception e) {
			if (manager.getTransaction().isActive()) {
				manager.getTransaction().rollback();
			}
			throw new PersistenceException("No se pudo guardar el detalle de factura", e);
		} finally {
			manager.close();
		}
	}

	@Override
	public List<DetalleFactura> listarPorFactura(Long idFactura) {
		EntityManager manager = emf.createEntityManager();
		try {
			TypedQuery<DetalleFactura> query = manager.createQuery(
					"SELECT d FROM DetalleFactura d WHERE d.factura.id = :idFactura ORDER BY d.id", DetalleFactura.class);
			query.setParameter("idFactura", idFactura);
			return query.getResultList();
		} finally {
			manager.close();
		}
	}

}
