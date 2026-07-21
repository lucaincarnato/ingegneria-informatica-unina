package entity;

import jakarta.persistence.*;

/**
 * Elemento del dominio che descrive un utente della biblioteca.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ruolo")
public class Utente {

	/**
	 * Chiave primaria della relativa entità nel database.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String cognome;
    /**
     * Email istituzionale dell'utente, consegnata al momento dell'associazione all'università.
     */
	private String emailIstituzionale;

	public Utente() {}

    /**
	 * Inserisce nel sistema un nuovo utente.
     * @param nome
     * @param cognome
     * @param emailIstituzionale email istituzionale dell'utente, consegnata al momento dell'associazione all'università.
     */
	public Utente(String nome, String cognome, String emailIstituzionale) {
		this.nome = nome;
		this.cognome = cognome;
		this.emailIstituzionale = emailIstituzionale;
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

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getEmailIstituzionale() {
		return emailIstituzionale;
	}

	public void setEmailIstituzionale(String emailIstituzionale) {
		this.emailIstituzionale = emailIstituzionale;
	}
}