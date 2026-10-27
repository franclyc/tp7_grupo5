package ar.edu.unju.escmi.main;

import javax.persistence.EntityManagerFactory;

import ar.edu.unju.escmi.config.EmfSingleton;

public class Main {

	public static void main(String[] args) {
		// Por ahora solo levantamos la unidad de persistencia: conecta con MySQL y Hibernate crea las tablas
		try {
			EntityManagerFactory emf = EmfSingleton.getInstance().getEmf();
			System.out.println("Conexión OK con la unidad de persistencia: " + emf.isOpen());
			emf.close();
		} catch (Exception e) {
			System.out.println("No se pudo conectar con la base de datos: " + e.getMessage());
		}
	}

}
