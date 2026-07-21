package entity;

import jakarta.persistence.*;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * Elemento del dominio che descrive una sala studio gestita da un bibliotecario e prenotabile da uno studente.
 */
@Entity
public class SalaStudio {

	/**
	 * Chiave primaria della relativa entità nel database.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String descrizione;
	private LocalTime orariApertura;
	private LocalTime orariChiusura;
    /**
     * Numero di postazioni della sala studio, individuando il numero di studneti che possono effettuare una prenotazione.
     */
	private int numeroPostazioni;

    /**
     * Lato uno della relazione che associa la gestione di una sala studio ad un bibliotecario.
     */
	@ManyToOne
	@JoinColumn(name = "bibliotecario_id")
	private Bibliotecario bibliotecario;

    /**
     * Lato molti della relazione che associa ad una sala studio delle postazioni.
     */
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "postazione_id", nullable = false)
	private Set<Postazione> postazioni;

    /**
     * Lato molti della relazione che divide una sala studio in aree.
     */
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "area_id", nullable = false)
	private Set<Area> aree;

    /**
     * Lato molti della relazione che associa ad una sala studio delle prenotazioni.
     */
	@OneToMany(mappedBy = "salaStudio", fetch = FetchType.EAGER)
	private List<Prenotazione> prenotazioni;

	public SalaStudio() {}

    /**
	 * Crea una nuova sala studio già predisposta all'utilizzo.
     * @param nome
     * @param descrizione
     * @param orariApertura
     * @param orariChiusura
     * @param numeroPostazioni
     */
	public SalaStudio(String nome, String descrizione, LocalTime orariApertura, LocalTime orariChiusura, int numeroPostazioni) {
		this.nome = nome;
		this.orariApertura = orariApertura;
		this.orariChiusura = orariChiusura;
		this.descrizione = descrizione;
		this.numeroPostazioni = numeroPostazioni;
		this.postazioni = new HashSet<>();
		this.aree = new HashSet<>();
		this.prenotazioni = new ArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public LocalTime getOrariApertura() {
		return orariApertura;
	}

	public void setOrariApertura(LocalTime orariApertura) {
		this.orariApertura = orariApertura;
	}

	public LocalTime getOrariChiusura() {
		return orariChiusura;
	}

	public void setOrariChiusura(LocalTime orariChiusura) {
		this.orariChiusura = orariChiusura;
	}

	public int getNumeroPostazioni() {
		return numeroPostazioni;
	}

	public void setNumeroPostazioni(int numeroPostazioni) {
		this.numeroPostazioni = numeroPostazioni;
	}

	public Bibliotecario getBibliotecario() {
		return bibliotecario;
	}

	void setBibliotecario(Bibliotecario bibliotecario) { // Visibilità package
		this.bibliotecario = bibliotecario;
	}

	public Set<Postazione> getPostazioni() {
		return postazioni;
	}

    /**
	 * Aggiunge una nuova postazione alla sala studio.
     * @param numeroPostazione identificativo della postazione da aggiungere.
     */
	public void aggiungiPostazione(int numeroPostazione) {
		postazioni.add(new Postazione(numeroPostazione));
	}

	public Set<Area> getAree() {
		return aree;
	}

    /**
	 * Suddivide la sala studio creando una nuova area.
     * @param tipoArea tipo dell'area in cui viene suddivisa la sala studio.
     */
	public void aggiungiArea(TipoArea tipoArea) {
		aree.add(new Area(tipoArea));
	}

	public List<Prenotazione> getPrenotazioni() {
		return this.prenotazioni;
	}

    /**
	 * Aggiunge una nuova prenotazione alla sala, laddove non è già stata aggiunta.
     * @param p prenotazione da aggiungere.
     */
	public void aggiungiPrenotazione(Prenotazione p) {
		if (p != null && !this.prenotazioni.contains(p)) {
			this.prenotazioni.add(p);
			p.setSalaStudio(this);
		}
	}

	/**
	 * @param o riferimento all'oggetto con cui effettuare la comparazione.
	 * @return true se le due sale hanno lo stesso nome.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o instanceof SalaStudio s) {
            return java.util.Objects.equals(nome, s.nome);
		}
		return false;
	}

	/**
	 * @return codice hash sulla base del nome della sala.
	 */
	@Override
	public int hashCode() {
		return java.util.Objects.hash(nome);
	}

}