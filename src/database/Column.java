package database;

/**
 * rappresenta l intestazione e tipo di una colonna della tabella
 */
public class Column {
	private String name;
	private String type;

	Column(String name, String type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * restituisce il nome di una colonna
	 * 
	 * @return name rappresenta il nome di una colonna
	 */
	public String getColumnName() {
		return name;
	}

	/**
	 * verifica se il tipo della colonna è un numero
	 * 
	 * @return vero nel caso in cui sia un numero
	 */
	public boolean isNumber() {
		return type.equals("number");
	}

	/**
	 * permette di mostrare a video un istanza della classe Column
	 * 
	 * @return String rappresenta lo stato di una istanza della classe Column
	 */
	public String toString() {
		return name + ":" + type;
	}
}