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
			// El mensaje útil suele estar en la última causa de la cadena de excepciones
			Throwable causa = e;
			while (causa.getCause() != null) {
				causa = causa.getCause();
			}
			System.out.println("No se pudo conectar con la base de datos.");
			System.out.println("Error: " + e.getMessage());
			System.out.println("Causa: " + causa.getClass().getSimpleName() + " - " + causa.getMessage());
		}
	}

}
