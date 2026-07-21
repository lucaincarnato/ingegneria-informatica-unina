package entity;

import jakarta.persistence.*;

/**
 * Elemento del dominio che descrive l'unità atomica di prenotazione di una sala studio.
 */
@Entity
public class Postazione {

	/**
	 * Chiave primaria della relativa entità nel database.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    /**
     * Identificativo della postazione in una sala studio.
     */
	private int numeroPostazione;

	public Postazione() {}

    /**
	 * Crea una nuova postazione nella relativa sala studio.
     * @param numeroPostazione
     */
	public Postazione(int numeroPostazione) {
		this.numeroPostazione = numeroPostazione;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getNumeroPostazione() {
		return numeroPostazione;
	}

	public void setNumeroPostazione(int numeroPostazione) {
		this.numeroPostazione = numeroPostazione;
	}

	/**
	 * @param o riferimento all'oggetto con cui effettuare la comparazione.
	 * @return true se le due postazioni hanno lo stesso identificativo.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || o instanceof SalaStudio) { return false; }
		Postazione p = (Postazione) o;
		return java.util.Objects.equals(numeroPostazione, p.numeroPostazione);
	}

	/**
	 * @return codice hash sulla base del tipo dell'identificativo della postazione.
	 */
	@Override
	public int hashCode() {
		return java.util.Objects.hash(numeroPostazione);
	}

}