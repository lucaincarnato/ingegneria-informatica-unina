package entity;

import jakarta.persistence.*;

/**
 * Elemento del dominio che descrive una porzione della sala studio adibita ad un particolare tipo di utilizzo.
 */
@Entity
public class Area {

    /**
     * Chiave primaria della relativa entità nel database.
     */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Enumerated(EnumType.STRING)
	private TipoArea tipoArea;

	public Area() {}

    /**
	 * Crea una nuova area in cui è possibile suddividere la sala studio.
     * @param tipoArea descrive il tipo di attività che è possibile fare in una determinata area.
     */
	public Area(TipoArea tipoArea) {
		this.tipoArea = tipoArea;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoArea getTipoArea() {
		return tipoArea;
	}

	public void setTipoArea(TipoArea tipoArea) {
		this.tipoArea = tipoArea;
	}

    /**
     * @param o riferimento all'oggetto con cui effettuare la comparazione.
     * @return true se le due aree sono dello stesso tipo.
     */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o instanceof Area a) {
			return java.util.Objects.equals(tipoArea, a.tipoArea);
		}
		return false;
	}

    /**
     * @return codice hash sulla base del tipo dell'area.
     */
	@Override
	public int hashCode() {
		return java.util.Objects.hash(tipoArea);
	}

}