package entity;

import jakarta.persistence.*;

/**
 * Elemento di dominio che descrive una notifica inviata ad uno studente per conto di una prenotazione.
 */
@Entity
public class Notifica {

    /**
	 * Chiave primaria della relativa entità nel database.
     */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String contenuto;

    /**
     * Lato uno della relazione che associa una notifica ad uno studente.
     */
	@ManyToOne
	@JoinColumn(name = "studente_id")
	private Studente studente;
    /**
     * Lato uno della relazione che descrive l'invio di una notifica per conto di una prenotazione.
     */
	@ManyToOne
	@JoinColumn(name = "prenotazione_id")
	private Prenotazione prenotazione;

	public Notifica() {}

    /**
	 * Crea e invia una nuova notifica ad uno studente per conto di una prenotazione.
     * @param contenuto contenuto della notifica.
     * @param studente studente a cui è inviata la notifica.
     * @param prenotazione prenotazione per conto della quale si invia la notifica.
     */
	public Notifica(String contenuto, Studente studente, Prenotazione prenotazione) {
		this.contenuto = contenuto;
		this.studente = studente;
		this.prenotazione = prenotazione;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContenuto() {
		return contenuto;
	}

	public void setContenuto(String contenuto) {
		this.contenuto = contenuto;
	}

	public Studente getStudente() {
		return studente;
	}

    /**
	 * Reindirizza la notifica verso un altro studente.
     * @param studente studente a cui reindirizzare la notifica.
     */
	void setStudente(Studente studente) {
		this.studente = studente;
	}

	public Prenotazione getPrenotazione() {
		return prenotazione;
	}

    /**
	 * Reinvia la notifica per conto di una prenotazione diversa.
     * @param prenotazione prenotazione da cui reinviare la notifica.
     */
	void setPrenotazione(Prenotazione prenotazione) {
		this.prenotazione = prenotazione;
	}

}