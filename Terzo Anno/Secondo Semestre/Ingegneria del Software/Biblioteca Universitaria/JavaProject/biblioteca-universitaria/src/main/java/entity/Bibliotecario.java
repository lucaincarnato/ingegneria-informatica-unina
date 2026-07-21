package entity;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;

/**
 * Elemento del dominio che descrive un utente impiegato della biblioteca che gestisce determinate sale studio.
 */
@Entity
@DiscriminatorValue("BIBLIOTECARIO")
public class Bibliotecario extends Utente {

    /**
     * Identificativo univoco che distingue i vari bibliotecari registrati a sistema.
     */
	private String codiceIdentificativo;

    /**
     * Lato molti dell'associazione che descrive la gestione di diverse sale da parte del bibliotecario.
     */
	@OneToMany(mappedBy = "bibliotecario", fetch = FetchType.EAGER)
	private Set<SalaStudio> saleGestite;

	public Bibliotecario() {}

    /**
	 * Inserisce nel sistema un nuovo bibliotecario.
     * @param nome
     * @param cognome
     * @param emailIstituzionale email istituzionale del bibliotecario, consegnata al momento dell'assunzione.
     * @param codiceIdentificativo identificativo univoco che distingue i vari bibliotecari registrati a sistema.
     */
	public Bibliotecario(String nome, String cognome, String emailIstituzionale, String codiceIdentificativo) {
		super(nome, cognome, emailIstituzionale);
		this.codiceIdentificativo = codiceIdentificativo;
		this.saleGestite = new HashSet<>();
	}

	public String getCodiceIdentificativo() {
		return codiceIdentificativo;
	}

	public void setCodiceIdentificativo(String codiceIdentificativo) {
		this.codiceIdentificativo = codiceIdentificativo;
	}

	public Set<SalaStudio> getSaleGestite() {
		return saleGestite;
	}

    /**
	 * Aggiunge una nuova sala studio in gestione al bibliotecario laddove egli non la gestisca già.
     * @param s nuova sala studio da aggiungere.
     */
	public void aggiungiSala(SalaStudio s) {
		if (s != null && !this.saleGestite.contains(s)) {
			this.saleGestite.add(s);
			s.setBibliotecario(this);
		}
	}

}