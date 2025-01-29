package data;

import utility.Keyboard;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import database.Column;
import database.DatabaseConnectionException;
import database.DbAccess;
import database.InsufficientColumnNumberException;
import database.NoValueException;
import database.QUERY_TYPE;
import database.TableData;
import database.TableSchema;

import java.util.Iterator;
import java.util.ArrayList;
import example.Example;
import example.ExampleSizeException;

/** classe necessaria per definire il trainingset */
public class Data implements Serializable {

	private ArrayList<Example> data;
	private ArrayList<Double> target;
	private int numberOfExamples;
	private ArrayList<Attribute> explanatorySet;
	private ContinuousAttribute classAttribute;

	/**
	 * costruttore di data per estrarre la tabella da un file
	 * 
	 * @param fileName definisce il nome del file
	 * @throws TrainingDataException eccezione generata durante l'acquisizione
	 *                               sbagliata di elementi da un file
	 * @throws FileNotFoundException eccezione generata se non esiste un file con
	 *                               quel nome
	 */
	public Data(String fileName) throws TrainingDataException, FileNotFoundException {
		File inFile = new File(fileName);
		if (!inFile.exists()) {
			throw new TrainingDataException("file inesistente");
		}
		Scanner sc = new Scanner(inFile);
		String line = sc.nextLine();
		if (!line.contains("@schema")) {
			sc.close();
			throw new TrainingDataException("Errore nello schema");
		}
		String s[] = line.split(" ");// dividiamo la stringa di input in piu stringhe in base al carattere passato
										// come argomento

		// popolare explanatory Set

		explanatorySet = new ArrayList<>(new Integer(s[1]));// allochiamo un array della dimensione specificata accanto
															// a schema
		Integer i1 = new Integer(s[1]);
		short iAttribute = 0;
		boolean presenza = false;
		int i = 0;
		line = sc.nextLine();
		while (!line.contains("@data")) {
			s = line.split(" ");
			if (s[0].equals("@desc")) { // aggiungo l'attributo allo spazio descrittivo
										// @desc motor discrete
				i++;
				if (s[2].equals("discrete"))
					explanatorySet.add(new DiscreteAttribute(s[1], iAttribute));
				else if (s[2].equals("continuous"))

					explanatorySet.add(new ContinuousAttribute(s[1], iAttribute));
				else {
					sc.close();
					throw new TrainingDataException();
				}
			} else if (s[0].equals("@target")) {
				classAttribute = new ContinuousAttribute(s[1], iAttribute);// rappresenta l intestazione della colonna
																			// dipendente
				presenza = true;
			}
			iAttribute++;
			line = sc.nextLine();
		}
		if (!line.contains("@data") || presenza == false || i1 != i) {
			sc.close();
			throw new TrainingDataException("errore");
		}
		// avvalorare numero di esempi
		numberOfExamples = new Integer(line.split(" ")[1]);

		// popolare data e target
		data = new ArrayList<>(/* numberOfExamples */);
		target = new ArrayList<>(/* numberOfExamples */);

		short iRow = 0;
		while (sc.hasNextLine()) {
			Example e = new Example(explanatorySet.size());
			line = sc.nextLine();
			if (line.isEmpty()) {
				sc.close();
				throw new TrainingDataException("linea vuota");
			}
			// assumo che attributi siano tutti discreti
			s = line.split(","); // E,E,5,4, 0.28125095
			for (short jColumn = 0; jColumn < s.length - 1; jColumn++) {
				if (explanatorySet.get(jColumn) instanceof DiscreteAttribute)
					e.set(s[jColumn], jColumn);
				else {
					try {
						e.set(Double.parseDouble(s[jColumn]), jColumn);
						ContinuousAttribute s1 = (ContinuousAttribute) explanatorySet.get(jColumn);
						s1.setmin(Double.parseDouble(s[jColumn]));
						s1.setmax(Double.parseDouble(s[jColumn]));
					} catch (NumberFormatException e1) {
					}
				}
			}
			iRow++;
			data.add(e);
			target.add(new Double(s[s.length - 1]));
		}
		if (iRow != numberOfExamples) {
			sc.close();
			throw new TrainingDataException();
		}
		sc.close();
	}

	/**
	 * @return int
	 *         restitutisce il numero di variabili indipendenti
	 */
	public int getNumberOfExplanatoryAttributes() {
		return explanatorySet.size();
	}

	/**
	 * legge un Example utilizzato per leggere il query point dell'utente (metodo
	 * precedente alla versione con client e server)
	 * 
	 * @return Example nuova istanza di Example
	 *
	 */
	public Example readExample() {
		Example e = new Example(getNumberOfExplanatoryAttributes());
		int i = 0;
		for (Attribute a : explanatorySet) {
			if (a instanceof DiscreteAttribute) {
				System.out.print("Inserisci valore discreto X[" + i + "]:");
				e.set(Keyboard.readString(), i);
			} else {
				double x = 0.0;
				do {
					System.out.print("Inserisci valore continuo X[" + i + "]:");
					x = Keyboard.readDouble();
				} while (new Double(x).equals(Double.NaN));
				e.set(x, i);
			}
			i++;
		}
		return e;
	}

	/**
	 * costruttore di classe,inizializza un istanza di data da una tabella del
	 * database
	 * 
	 * @param db
	 *                  rappresenta la configurazione del db(numero di
	 *                  porta,username,password...)
	 * @param tableName
	 *                  nome della tabella da cui caricare l istanza di data
	 * @throws InsufficientColumnNumberException
	 *                                           viene lanciata in caso di mancanza
	 *                                           della variabile target o variabili
	 *                                           indipendenti
	 * @throws SQLException
	 *                                           viene lanciata nel caso di errori
	 *                                           dell SQL
	 * @throws NoValueException
	 *                                           viene lanciata quando i valori non
	 *                                           solo del tipo dichiarato nello
	 *                                           schema
	 */
	public Data(DbAccess db, String tableName)
			throws InsufficientColumnNumberException, SQLException, NoValueException {
		TableSchema tableSchema = new TableSchema(tableName, db);
		Iterator<Column> i = tableSchema.iterator();
		int y = 0;
		explanatorySet = new ArrayList<Attribute>(tableSchema.getNumberOfAttributes());
		TableData tabledata = new TableData(db, tableSchema);
		data = (ArrayList<Example>) tabledata.getExamples();
		target = (ArrayList<Double>) tabledata.getTargetValues();
		QUERY_TYPE a1 = QUERY_TYPE.MIN;
		QUERY_TYPE a2 = QUERY_TYPE.MAX;
		while (i.hasNext()) {
			Column column = i.next();
			if (column.isNumber()) {
				explanatorySet.add(new ContinuousAttribute(column.getColumnName(), y));
				((ContinuousAttribute) explanatorySet.get(y))
						.setmin(((Double) tabledata.getAggregateColumnValue(column, a1)));
				((ContinuousAttribute) explanatorySet.get(y))
						.setmax(((Double) tabledata.getAggregateColumnValue(column, a2)));
			} else
				explanatorySet.add(new DiscreteAttribute(column.getColumnName(), y));
			y++;
		}
		Column c = tableSchema.target();
		classAttribute = new ContinuousAttribute(c.getColumnName(), y);
		String query = "";
		query = "select count(*) from " + tableName;
		Statement statement = db.getConnection().createStatement();
		ResultSet resultset = statement.executeQuery(query);
		resultset.next();
		numberOfExamples = resultset.getInt(1);
	}

	/**
	 * metodo che ritorna lo stato dell'oggetto sottoforma di stringa
	 * 
	 * @return String
	 *         stringa che rappresenta lo stato di un oggetto
	 */
	public String toString() {
		return "array di example \n" + data.toString() + "\n" + "array colonna dipendente \n" + target.toString() + "\n"
				+ "numero esempi \n" + Integer.toString(numberOfExamples) + "\n" + "intestazioni\n"
				+ explanatorySet.toString() + "\n" + "intestazione dipendente \n" + classAttribute.toString();
	}

	private int partition(ArrayList<Double> key, int inf, int sup) throws ExampleSizeException {
		int i, j;

		i = inf;
		j = sup;
		int med = (inf + sup) / 2;

		Double x = key.get(med);

		data.get(inf).swap(data.get(med));

		double temp = target.get(inf);
		target.set(inf, target.get(med));
		target.set(med, temp);

		temp = key.get(inf);
		key.set(inf, key.get(med));
		key.set(med, temp);

		while (true) {

			while (i <= sup && key.get(i) <= x) {
				i++;

			}

			while (key.get(j) > x) {
				j--;

			}

			if (i < j) {
				data.get(i).swap(data.get(j));
				temp = target.get(i);
				target.set(i, target.get(j));
				target.set(j, temp);

				temp = key.get(i);
				key.set(i, key.get(j));
				key.set(j, temp);

			} else
				break;
		}
		data.get(inf).swap(data.get(j));

		temp = target.get(inf);
		target.set(inf, target.get(j));
		target.set(j, temp);

		temp = key.get(inf);
		key.set(inf, key.get(j));
		key.set(j, temp);
		return j;

	}

	private void quicksort(ArrayList<Double> key, int inf, int sup) throws ExampleSizeException {

		if (sup >= inf) {
			int pos;

			pos = partition(key, inf, sup);

			if ((pos - inf) < (sup - pos + 1)) {
				quicksort(key, inf, pos - 1);
				quicksort(key, pos + 1, sup);
			} else {
				quicksort(key, pos + 1, sup);
				quicksort(key, inf, pos - 1);
			}

		}

	}

	/**
	 * utilizzato per leggere il query point dell'utente
	 * 
	 * @param out utilizzato per scrivere nello stream del client
	 * @param in  utilizzato per leggere dallo stream del client
	 * @return Example query point
	 * @throws IOException            eccezione sollevata in caso di errore durante
	 *                                la comunicazione con l'utente
	 * @throws ClassNotFoundException eccezione sollevata quando la classe non è
	 *                                definita
	 * @throws ClassCastException     eccezione sollevata quando non è possibile
	 *                                effettuare il cast da un tipo ad un altro
	 */
	public Example readExample(ObjectOutputStream out, ObjectInputStream in)
			throws IOException, ClassNotFoundException, ClassCastException {
		Iterator<Attribute> iterator = explanatorySet.iterator();
		Example e = new Example(getNumberOfExplanatoryAttributes());
		while (iterator.hasNext()) {
			Attribute a = iterator.next();
			if (a instanceof ContinuousAttribute) {
				out.writeObject("@READDOUBLE");
			} else {
				out.writeObject("@READSTRING");
			}
		}
		out.writeObject("@ENDEXAMPLE");
		Iterator<Attribute> iteratore = explanatorySet.iterator();
		int i = 0;
		while (iteratore.hasNext()) {
			Attribute b = iteratore.next();
			if (b instanceof ContinuousAttribute) {

				e.set(((Double) in.readObject()), i);
			} else {
				e.set(((String) in.readObject()), i);
			}
			i++;
		}
		return e;
	}

	Example scaledExample(Example e) {
		Example e1 = new Example(getNumberOfExplanatoryAttributes());
		Iterator<Attribute> i = explanatorySet.iterator();
		int i1 = 0;
		while (i.hasNext()) {
			Attribute a = i.next();
			if (a instanceof ContinuousAttribute)
				e1.set(((ContinuousAttribute) a).scale((Double) e.get(i1)), i1);
			else
				e1.set(e.get(i1), i1);
			i1++;
		}
		return e1;
	}

	/**
	 * ritorna il valore della media in base al valore di k
	 * 
	 * @param e query point
	 * @param k definisce il numero di distanze diverse da prendere in
	 *          considerazione
	 * @return double
	 */
	public double avgClosest(Example e, int k) {
		ArrayList<Double> key = new ArrayList<>();
		Iterator<Example> iterator = data.iterator();
		while (iterator.hasNext()) {
			Example e1 = iterator.next();
			try {
				key.add(scaledExample(e1).distance(scaledExample(e)));
			} catch (ExampleSizeException exc) {
			}
		}
		quicksort(key, 0, key.size() - 1);
		double sum = 0;
		Iterator<Double> i = key.iterator();
		Iterator<Double> k1 = target.iterator();
		Double d1 = k1.next();
		double d = i.next();
		double cont = d;
		int j = 0;
		while (i.hasNext() && k >= 0) {
			if (cont != d) {
				k--;
				if (k > 0) {
					cont = d;
				}
			} else {
				sum += d1;
				j++;
				d = i.next();
				d1 = k1.next();
			}
		}
		if (!i.hasNext() && k > 0) {
			sum += d1;
			j++;
		}
		return sum / j;
	}

	/**
	 * main utilizzato per testare le funzionalità della classe in versioni
	 * precedenti
	 * 
	 * @param args
	 *             contiene comandi digitati dall utente da cmd
	 * @throws FileNotFoundException
	 *                               eccezione sollevata quando il file non viene
	 *                               trovato
	 */
	public static void main(String args[]) throws FileNotFoundException {
		try {
			new Data(new DbAccess(), "provac");
		} catch (NoValueException | DatabaseConnectionException
				| InsufficientColumnNumberException | SQLException e) {
		}

	}
}