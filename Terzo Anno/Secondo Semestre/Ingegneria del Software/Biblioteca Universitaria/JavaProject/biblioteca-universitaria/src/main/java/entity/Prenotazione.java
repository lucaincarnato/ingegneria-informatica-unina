package entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

/**
 * Elemento del dominio che descrive la prenotazione di una sala studio da parte di uno studente.
 */
@Entity
public class Prenotazione {

	/**
	 * Chiave primaria della relativa entità nel database.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDate data;
	private LocalTime orarioInizio;
	private LocalTime orarioFine;
	@Enumerated(EnumType.STRING)
	private StatoPrenotazione stato;

    /**
     * Lato uno della relazione che associa uno studente ad una prenotazione.
     */
	@ManyToOne
	@JoinColumn(name = "studente_id")
	private Studente studente;

	/**
	 * Lato molti della relazione che associa una notifica ad uno studente.
	 */
	@OneToMany(mappedBy = "prenotazione", fetch = FetchType.EAGER)
	private List<Notifica> notifiche;

	/**
	 * Lato uno della relazione che associa una sala studio ad una prenotazione.
	 */
	@ManyToOne
	@JoinColumn(name = "salaStudio_id")
	private SalaStudio salaStudio;

	public Prenotazione() {}

    /**
	 * Crea una nuova prenotazione da parte di uno studente nei confronti di una sala studio.
     * @param data
     * @param orarioInizio
     * @param orarioFine
     * @param stato
     * @param salaStudio sala studio da prenotare.
     */
	public Prenotazione(LocalDate data, LocalTime orarioInizio, LocalTime orarioFine, StatoPrenotazione stato, SalaStudio salaStudio) {
		this.data = data;
		this.orarioInizio = orarioInizio;
		this.orarioFine = orarioFine;
		this.stato = stato;
		this.notifiche  = new ArrayList<>();
		this.salaStudio = salaStudio;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public LocalTime getOrarioInizio() {
		return orarioInizio;
	}

	public void setOrarioInizio(LocalTime orarioInizio) {
		this.orarioInizio = orarioInizio;
	}

	public LocalTime getOrarioFine() {
		return orarioFine;
	}

	public void setOrarioFine(LocalTime orarioFine) {
		this.orarioFine = orarioFine;
	}

	public StatoPrenotazione getStato() {
		return stato;
	}

	public void setStato(StatoPrenotazione stato) {
		this.stato = stato;
	}

	public Studente getStudente() {
		return studente;
	}

	public void setStudente(Studente studente) {
		this.studente = studente;
	}

	public List<Notifica> getNotifiche() {
		return notifiche;
	}

    /**
	 * Invia una nuova notifica non già inviata per conto della prenotazione.
     * @param n notifica da inviare per conto della prenotazione.
     */
	public void aggiungiNotifica(Notifica n) {
		if (n != null && !this.notifiche.contains(n)) {
			this.notifiche.add(n);
			n.setPrenotazione(this);
		}
	}

	public SalaStudio getSalaStudio() {
		return salaStudio;
	}

	public void setSalaStudio(SalaStudio salaStudio) {
		this.salaStudio = salaStudio;
	}
}