package entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Elemento del dominio che descrive un utente studente dell'università che accede alle sale studio della biblioteca.
 */
@Entity
@DiscriminatorValue("STUDENTE")
public class Studente extends Utente {

    /**
     * Identificativo univoco che distingue gli studenti dell'università.
     */
	private String numeroMatricola;

    /**
     * Lato molti della relazione che associa ad uno studente le prenotazioni che ha effettuato.
     */
	@OneToMany(mappedBy = "studente", fetch = FetchType.EAGER)
	private List<Prenotazione> prenotazioni;

    /**
     * Lato molti della relazione che associa ad uno studente le notifiche che gli sono state inviate per conto di una prenotazione.
     */
	@OneToMany(mappedBy = "studente", fetch = FetchType.EAGER)
	private List<Notifica> notifiche;

	public Studente() {}

    /**
	 * Inserisce nel sistmea un nuovo studente.
     * @param nome
     * @param cognome
     * @param emailIstituzionale email istituzionale dello studente, consegnata al momento dell'immatricolazione.
     * @param numeroMatricola identificativo che distingue i vari studenti registrati a sistema.
     */
	public Studente(String nome, String cognome, String emailIstituzionale, String numeroMatricola) {
		super(nome, cognome, emailIstituzionale);
		this.numeroMatricola = numeroMatricola;
		this.prenotazioni = new ArrayList<>();
		this.notifiche = new ArrayList<>();
	}

	public String getNumeroMatricola() {
		return numeroMatricola;
	}

	public void setNumeroMatricola(String numeroMatricola) {
		this.numeroMatricola = numeroMatricola;
	}

	public List<Prenotazione> getPrenotazioni() {
		return prenotazioni;
	}

    /**
	 * Aggiunge una nuova prenotazione effettuata dallo studente.
     * @param p prenotazione effettuata dallo studente.
     */
	public void aggiungiPrenotazione(Prenotazione p) {
		if (p != null && !this.prenotazioni.contains(p)) {
			this.prenotazioni.add(p);
			p.setStudente(this);
		}
	}

	public List<Notifica> getNotifiche() {
		return notifiche;
	}

    /**
	 * Aggiunge una notifica inviata allo studente per conto di una prenotazione.
     * @param n notifica inviata allo studente per conto di una prenotazione.
     */
	public void aggiungiNotifica(Notifica n) {
		if (n != null && !this.notifiche.contains(n)) {
			this.notifiche.add(n);
			n.setStudente(this);
		}
	}}